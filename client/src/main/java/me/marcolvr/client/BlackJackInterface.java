package me.marcolvr.client;

public interface BlackJackInterface {

    void requestUsername(boolean retry);

    void requestRoom(boolean retry);

    void requestFiches(boolean cancel);

    boolean isWaitingForInput();

    void requestAction();

    void updateLobbyStatus(int players, boolean starting, int time);

    void playerUpdate(String name, int fiches, int cards, int cardsValue);

    void gameUpdate(String selection, int state);

    void gameEnd(int state);
}
