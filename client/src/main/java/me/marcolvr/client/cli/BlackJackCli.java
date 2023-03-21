package me.marcolvr.client.cli;

import me.marcolvr.client.BlackJackClient;
import me.marcolvr.client.BlackJackInterface;

import java.util.Scanner;

public class BlackJackCli implements BlackJackInterface {

    private Scanner s;
    private BlackJackClient client;
    public BlackJackCli(BlackJackClient client){
        this.client=client;
        s=new Scanner(System.in);
        requestUsername(false);
    }

    @Override
    public void requestUsername(boolean retry){
        if(retry) System.out.println("L'username inserito non è disponibile.");
        System.out.print("Inserisci l'username: ");
        client.offerUsername(s.nextLine());
    }

    @Override
    public void requestRoom(boolean retry) {
        if(retry) System.out.println("La stanza non è al momento disponibile.");
        System.out.print("Inserisci la stanza: ");
        client.offerRoom(s.nextLine());
    }
}
