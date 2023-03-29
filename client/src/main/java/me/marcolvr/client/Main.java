package me.marcolvr.client;

import lombok.Getter;

import java.io.IOException;

public class Main {

    @Getter
    private static BlackJackClient client;
    public static void main(String[] args) throws IOException, InterruptedException {

        String ip = "localhost";
        boolean cli = false;
        for (String arg : args) {
            if(arg.toLowerCase().startsWith("-serverip=")){
                ip=arg.toLowerCase().substring(10);
            }
            if(arg.toLowerCase().equals("-usecli")){
                cli=true;
            }
        }
        client = new BlackJackClient(ip, 24512, cli);
    }
}