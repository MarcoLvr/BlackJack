package me.marcolvr.client.cli;

import me.marcolvr.client.BlackJackClient;
import me.marcolvr.client.BlackJackInterface;
import me.marcolvr.network.packet.serverbound.ServerboundFicheAction;
import me.marcolvr.scanner.StreamScanner;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class BlackJackCli implements BlackJackInterface {

    private StreamScanner s;
    private BlackJackClient client;

    private boolean asyncInputWait;
    public BlackJackCli(BlackJackClient client){
        this.client=client;
        asyncInputWait =false;
        s=new StreamScanner(System.in);
        requestUsername(false);
    }

    @Override
    public void requestUsername(boolean retry){
        if(retry) System.out.println("L'username inserito non è disponibile.");
        System.out.print("Inserisci l'username: ");
        try{
            client.offerUsername(s.nextLine());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void requestRoom(boolean retry) {
        if (retry) System.out.println("La stanza non è al momento disponibile.");
        System.out.print("Inserisci la stanza: ");
        try {
            client.offerRoom(s.nextLine());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestFiches(boolean cancel) {
        if(cancel){
            asyncInputWait=false;
            System.out.println("\nTroppo tardi. Il server ha scommesso per te 2000 fiches o quante ne sono rimaste");
            return;
        }
        Thread req = new Thread(()->{
            asyncInputWait=true;
            System.out.print("Quanti fiches scommetti? Inserisci il valore: ");
            int scommessa = 0;
            do {
                while (asyncInputWait && !s.hasNext()) {}
                if(!asyncInputWait) return;
                try{
                    scommessa=Integer.parseInt(s.nextLine());
                }catch (Exception e){}
            }while (scommessa==0);
            asyncInputWait=false;
            client.getConnection().sendPacket(new ServerboundFicheAction(Math.abs(scommessa)*-1));
            System.out.println("Hai scommesso " + Math.abs(scommessa)*-1);
        });
        req.start();

    }

    @Override
    public void requestAction(){
        Thread req = new Thread(()->{
            asyncInputWait=true;
            System.out.print("Che vuoi fare? 1 per aggiungere, altro per skippare: ");
            while (asyncInputWait && !s.hasNext()) {}
            if(!asyncInputWait) return;
            client.offerAction(s.nextLine());
            asyncInputWait=false;
        });
        req.start();
    }

    @Override
    public boolean isWaitingForInput() {
        return asyncInputWait;
    }


}
