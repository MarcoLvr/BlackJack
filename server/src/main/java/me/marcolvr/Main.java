package me.marcolvr;

import me.marcolvr.game.logic.BlackJackGame;
import me.marcolvr.network.ServerConnection;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //TODO: args
        BlackJackServer server = new BlackJackServer(24512);
    }
}