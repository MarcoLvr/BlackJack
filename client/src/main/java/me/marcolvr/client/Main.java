package me.marcolvr.client;

import lombok.Getter;

import java.io.IOException;

public class Main {

    @Getter
    private static BlackJackClient client;
    public static void main(String[] args) throws IOException, InterruptedException {
        client = new BlackJackClient("localhost", 24512, true);
    }
}