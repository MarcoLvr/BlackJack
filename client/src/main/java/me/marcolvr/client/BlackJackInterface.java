package me.marcolvr.client;

public interface BlackJackInterface {

    void requestUsername(boolean retry);

    void requestRoom(boolean retry);

    void requestFiches(boolean cancel);

    boolean isWaitingForInput();
}
