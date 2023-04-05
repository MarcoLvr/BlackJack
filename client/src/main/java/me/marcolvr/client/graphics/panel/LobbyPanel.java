package me.marcolvr.client.graphics.panel;

import me.marcolvr.client.BlackJackClient;
import me.marcolvr.client.graphics.component.LvrButton;
import me.marcolvr.client.graphics.component.LvrLabel;
import me.marcolvr.client.graphics.component.LvrTextField;

import javax.swing.*;
import java.awt.*;

public class LobbyPanel extends JPanel {

    private LvrLabel statusLabel;
    private LvrLabel playersLabel;

    public LobbyPanel(final BlackJackClient client, GridBagLayout layout) {
        setLayout(layout);
        GridBagConstraints constraints=new GridBagConstraints();
        constraints.insets=new Insets(10,10,10,10);
        setSize(1280, 720);
        constraints.anchor=GridBagConstraints.CENTER;

        statusLabel=new LvrLabel("Attesa di giocatori");
        statusLabel.setFont(new Font("arial", Font.BOLD, 50));
        statusLabel.setForeground(Color.WHITE);

        playersLabel=new LvrLabel("Giocatori: 1");
        playersLabel.setFont(new Font("arial", Font.ITALIC, 18));
        playersLabel.setForeground(Color.WHITE);

        setOpaque(true);
        setBackground(new Color(173,38,38));
        constraints.gridy=1;
        add(statusLabel, constraints);
        constraints.gridy=2;
        add(playersLabel, constraints);
        setVisible(true);

    }

    public void updateData(int players, boolean starting, int time){
        if(starting){
            statusLabel.setText("Avvio in " + time + " secondi");
        }
        playersLabel.setText("Giocatori: " + players);
    }
}
