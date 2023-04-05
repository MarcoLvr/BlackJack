package me.marcolvr.client.graphics.panel;

import me.marcolvr.client.BlackJackClient;
import me.marcolvr.client.graphics.component.LvrButton;
import me.marcolvr.client.graphics.component.LvrLabel;
import me.marcolvr.client.graphics.component.LvrTextField;

import javax.swing.*;
import java.awt.*;

public class UsernamePanel extends JPanel {

    public UsernamePanel(final BlackJackClient client, boolean retry, GridBagLayout layout) {
        setLayout(layout);
        GridBagConstraints constraints=new GridBagConstraints();
        constraints.insets=new Insets(10,10,10,10);
        setSize(1280, 720);
        constraints.anchor=GridBagConstraints.CENTER;
        LvrLabel usernameLabel = new LvrLabel("Inserisci il tuo username");
        usernameLabel.setForeground(Color.WHITE);

        LvrLabel alreadyUsed = new LvrLabel("Username già usato!");
        alreadyUsed.setForeground(Color.WHITE);
        if(!retry){
            alreadyUsed.setVisible(false);
        }

        LvrTextField usernameTextField = new LvrTextField(16);

        LvrButton usernameButton = new LvrButton("Avanti");

        usernameButton.onComponentEvent(event ->{
            client.offerUsername(usernameTextField.getText());
        });
        setOpaque(true);
        setBackground(new Color(173,38,38));
        constraints.gridy=1;
        add(usernameLabel, constraints);
        constraints.gridy=2;
        add(alreadyUsed, constraints);
        constraints.gridy=3;
        add(usernameTextField, constraints);
        constraints.gridy=4;
        add(usernameButton, constraints);
        setVisible(true);

    }
}
