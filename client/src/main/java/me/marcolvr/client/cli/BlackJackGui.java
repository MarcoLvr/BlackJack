package me.marcolvr.client.cli;

import me.marcolvr.client.BlackJackClient;
import me.marcolvr.client.BlackJackInterface;
import me.marcolvr.client.graphics.panel.*;

import javax.swing.*;
import java.awt.*;

public class BlackJackGui extends JFrame implements BlackJackInterface{

    private BlackJackClient client;
    private GridBagLayout layout;

    private LobbyPanel lobbyPanel;

    private GamePanel gamePanel;

    public BlackJackGui(BlackJackClient client){
        this.client=client;
        setSize(1280, 720);
        layout=new GridBagLayout();
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("BlackJack v1.0");
        requestUsername(false);
        setVisible(true);
    }

    @Override
    public void requestUsername(boolean retry) {
        JPanel panel = new UsernamePanel(client, retry, layout);
        getContentPane().removeAll();
        add(panel);
        validate();

    }

    @Override
    public void requestRoom(boolean retry) {
        JPanel panel = new RoomPanel(client, retry, layout);
        getContentPane().removeAll();
        add(panel);
        validate();
    }

    @Override
    public void requestFiches(boolean cancel) {
        lobbyPanel=null;
        JPanel panel = new FichesPanel(client, cancel, layout);
        getContentPane().removeAll();
        add(panel);
        validate();
    }

    @Override
    public boolean isWaitingForInput() {
        return false;
    }

    @Override
    public void requestAction() {
        gamePanel.reqAction();
    }

    @Override
    public void updateLobbyStatus(int players, boolean starting, int time) {
        setTitle("BlackJack v1.0 - " + client.getUsername());
        if(lobbyPanel==null){
            lobbyPanel=new LobbyPanel(client, layout);
            getContentPane().removeAll();
            add(lobbyPanel);
            validate();
        }
        lobbyPanel.updateData(players, starting, time);
    }

    @Override
    public void playerUpdate(String name, int fiches, int cards, int cardsValue) {
        gamePanel.setPlayerData(name, fiches, cards, cardsValue);
    }

    @Override
    public void gameUpdate(String selection, int state) {
        switch (state){
            case 0 ->{
                requestFiches(false);
            }
            case 1 ->{
                if(isWaitingForInput()){
                    requestFiches(true);
                }
                gamePanel=new GamePanel(client, layout);
                getContentPane().removeAll();
                add(gamePanel);
                validate();
            }
            case 2 ->{
                System.out.println(selection.equalsIgnoreCase(client.getUsername()));
                if(selection.equalsIgnoreCase(client.getUsername())) {
                    requestAction();
                }else{
                    gamePanel.noReqAction();
                }
            }
        }
    }

    @Override
    public void gameEnd(int state) {
        switch (state){
            case 0 ->{
                gamePanel.setWinstate("Hai perso! Hai superato 21");
            }
            case 1 ->{
                gamePanel.setWinstate("Hai pareggiato col banco!");
            }
            case 2 ->{
                gamePanel.setWinstate("Hai vinto! Il banco ha un valore più basso!");
            }
        }
    }
}
