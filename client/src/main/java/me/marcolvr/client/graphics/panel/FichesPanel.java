package me.marcolvr.client.graphics.panel;

import me.marcolvr.client.BlackJackClient;
import me.marcolvr.client.graphics.component.LvrButton;
import me.marcolvr.client.graphics.component.LvrLabel;
import me.marcolvr.client.graphics.component.LvrTextField;

import javax.swing.*;
import java.awt.*;

public class FichesPanel extends JPanel {

    public FichesPanel(final BlackJackClient client, boolean retry, GridBagLayout layout) {
        setLayout(layout);
        GridBagConstraints constraints=new GridBagConstraints();
        constraints.insets=new Insets(10,10,10,10);
        setSize(1280, 720);
        constraints.anchor=GridBagConstraints.CENTER;
        LvrLabel usernameLabel = new LvrLabel("Quante fiches scommetti?");
        usernameLabel.setForeground(Color.WHITE);

        LvrLabel alreadyUsed = new LvrLabel("Fiches non disponibili");
        alreadyUsed.setForeground(Color.WHITE);
        if(!retry){
            alreadyUsed.setVisible(false);
        }

        LvrTextField usernameTextField = new LvrTextField(3);

        LvrButton usernameButton = new LvrButton("Punta");

        usernameButton.onComponentEvent(event ->{
            try{
                int fiches = Integer.parseInt(usernameTextField.getText());
                client.offerFiches(fiches);
                usernameTextField.setEnabled(false);
                usernameButton.setEnabled(false);
            }catch (Exception e){
                alreadyUsed.setText("Numero non valido!");
                alreadyUsed.setVisible(true);
                validate();
            }
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
