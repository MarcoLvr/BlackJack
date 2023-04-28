package me.marcolvr.client.cli;

import me.marcolvr.client.BlackJackClient;
import me.marcolvr.client.BlackJackInterface;
import me.marcolvr.network.packet.serverbound.ServerboundFicheAction;
import me.marcolvr.network.packet.serverbound.ServerboundPacket;
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
            client.getConnection().sendPacket(ServerboundPacket.ficheAction(Math.abs(scommessa)*-1));
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
    public void updateLobbyStatus(int players, boolean starting, int time) {
        if(!starting && time==0) {
            System.out.println("In attesa di giocatori. Attualmente " + players);
            return;
        }
        if(starting){
            if(time%10==0 || time<10){
                System.out.println("Avvio in " + time);
            }
            return;
        }
        System.out.println("Avvio annullato");
    }

    @Override
    public void playerUpdate(String name, int fiches, int cards, int cardsValue) {
        System.out.println(name + ": Ha " + fiches + " fiches e ha " + cards + " con valore totale di " + cardsValue);
    }

    @Override
    public void gameUpdate(String selection, int state) {
        switch (state){
            case 0 ->{
                System.out.println("Si inizia!");
                System.out.println("Giocatori:");
                requestFiches(false);
            }
            case 1 ->{
                if(isWaitingForInput()){
                    requestFiches(true);
                }
            }
            case 3 ->{
                if(selection.equals(client.getUsername())) {
                    requestAction();
                }
            }
        }
    }

    @Override
    public void gameEnd(int state) {
        switch (state){
            case 0 ->{
                System.out.println("Hai perso! Hai superato 21");
            }
            case 1 ->{
                System.out.println("Hai pareggiato col banco!");
            }
            case 2 ->{
                System.out.println("Hai vinto! Il banco ha un valore più basso!");
            }
        }
    }

    @Override
    public boolean isWaitingForInput() {
        return asyncInputWait;
    }


}
