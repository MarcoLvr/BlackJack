package me.marcolvr.client.graphics.panel;

import me.marcolvr.client.BlackJackClient;
import me.marcolvr.client.graphics.component.LvrButton;
import me.marcolvr.client.graphics.component.LvrLabel;
import me.marcolvr.client.graphics.component.LvrTextField;

import javax.swing.*;
import java.awt.*;

public class UsernamePanel extends JPanel {

    public UsernamePanel(final BlackJackClient client, boolean retry) {
        setSize(1280, 720);
        LvrLabel usernameLabel = new LvrLabel("Inserisci il tuo username");
        usernameLabel.setLocation(100, 100);

        LvrTextField usernameTextField = new LvrTextField();
        usernameLabel.setLocation(100, 200);

        LvrButton usernameButton = new LvrButton("Avanti");
        usernameButton.setLocation(100, 300);

        usernameButton.onComponentEvent(event ->{
            client.offerUsername(usernameTextField.getText());
        });
        setOpaque(true);
        setBackground(new Color(173,38,38));
        add(usernameLabel);
        add(usernameTextField);
        add(usernameButton);
        setVisible(true);

    }
}
