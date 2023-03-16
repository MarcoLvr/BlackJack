package me.marcolvr;

import lombok.Getter;

import java.io.IOException;

public class Main {

    @Getter
    private static BlackJackServer blackJackServer;
    public static void main(String[] args) throws IOException {
        //TODO: args
        blackJackServer = new BlackJackServer(24512);
    }
}