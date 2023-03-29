package me.marcolvr.client.cli;

import me.marcolvr.client.BlackJackClient;
import me.marcolvr.client.BlackJackInterface;
import me.marcolvr.client.graphics.panel.RoomPanel;
import me.marcolvr.client.graphics.panel.UsernamePanel;

import javax.swing.*;
import java.awt.*;

public class BlackJackGui extends JFrame implements BlackJackInterface{

    private BlackJackClient client;

    public BlackJackGui(BlackJackClient client){
        this.client=client;
        setSize(1280, 720);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        requestUsername(false);
        setVisible(true);
    }

    @Override
    public void requestUsername(boolean retry) {
        JPanel panel = new UsernamePanel(client, retry);
        getContentPane().removeAll();
        add(panel);
        validate();

    }

    @Override
    public void requestRoom(boolean retry) {
        JPanel panel = new RoomPanel(client, retry);
        removeAll();
        add(panel);
    }

    @Override
    public void requestFiches(boolean cancel) {

    }

    @Override
    public boolean isWaitingForInput() {
        return false;
    }

    @Override
    public void requestAction() {

    }
}
