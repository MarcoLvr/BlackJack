package me.marcolvr.server;

import lombok.Getter;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        new BlackJackServer(24512);
    }
}