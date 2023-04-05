package me.marcolvr.client.graphics.panel;

import me.marcolvr.client.BlackJackClient;
import me.marcolvr.client.Main;
import me.marcolvr.client.graphics.component.LvrButton;
import me.marcolvr.client.graphics.component.LvrLabel;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GamePanel extends JPanel {

    private List<PlayerPanel> players;

    private PlayerPanel banco;

    private LvrButton addCardBtn, skipBtn;

    private LvrLabel winstate;


    public GamePanel(final BlackJackClient client, GridBagLayout layout) {
        players =new ArrayList<>();
        setLayout(layout);
        GridBagConstraints constraints=new GridBagConstraints();
        constraints.gridwidth=7;
        constraints.gridheight=7;
        constraints.insets=new Insets(10,10,10,10);
        setSize(1280, 720);
        constraints.anchor=GridBagConstraints.CENTER;

        winstate=new LvrLabel("Hai Perso!");
        winstate.setSize(10, 100);
        winstate.setFont(new Font("arial", Font.BOLD, 20));
        winstate.setVisible(false);

        addCardBtn = new LvrButton("Aggiungi carta");
        addCardBtn.setEnabled(false);
        addCardBtn.setSize(50, 10);

        skipBtn= new LvrButton("Apposto così");
        skipBtn.setEnabled(false);
        skipBtn.setSize(50, 10);

        addCardBtn.onComponentEvent(event ->{
            client.offerAction("1");
        });

        skipBtn.onComponentEvent(event ->{
            client.offerAction("0");
        });

        setOpaque(true);
        setBackground(new Color(173,38,38));

        regenPlayers();

        setVisible(true);

    }

    public synchronized void regenPlayers(){
        removeAll();
        GridBagConstraints constraints=new GridBagConstraints();
        constraints.insets=new Insets(10,10,10,10);
        setSize(1280, 720);
        constraints.anchor=GridBagConstraints.CENTER;
        constraints.gridy=1;
        constraints.gridx=1;
        add(winstate, constraints);
        constraints.gridy=2;
        if(banco!=null) add(banco, constraints);
        constraints.gridy=3;
        for (int i = 1; i <= players.size(); i++) {
            constraints.gridx=i;
            add(players.get(i-1), constraints);
        }
        constraints.gridy=5;
        constraints.gridx=1;
        add(addCardBtn, constraints);
        constraints.gridx=2;
        add(skipBtn, constraints);
        System.out.println("refreshato");
        validate();
    }

    public void reqAction(){
        addCardBtn.setEnabled(true);
        skipBtn.setEnabled(true);
    }

    public void noReqAction(){
        addCardBtn.setEnabled(true);
        skipBtn.setEnabled(true);
    }

    public void setWinstate(String state){
        winstate.setText(state);
        winstate.setVisible(true);
        skipBtn.setEnabled(false);
        addCardBtn.setEnabled(false);
    }

    public void setPlayerData(String username, int fiches, int cards, int cardsValue){
        if(!username.equalsIgnoreCase(Main.getClient().getUsername())){
            skipBtn.setEnabled(false);
            addCardBtn.setEnabled(false);
        }
        Optional<PlayerPanel> panel;
        if(username.equalsIgnoreCase("Banco")){
            if(banco==null){
                banco=new PlayerPanel("Banco", fiches, cards, cardsValue);
            }
            panel=Optional.of(banco);
        }else{
            panel= players.stream().filter(p -> p.getName().equalsIgnoreCase(username)).findFirst();
        }
        if(panel.isEmpty()){
            panel=Optional.of(
                    new PlayerPanel(username, fiches, cards, cardsValue));
            players.add(panel.get());
            regenPlayers();
        }
        panel.get().changeData(username, fiches, cards, cardsValue);
    }

    class PlayerPanel extends JPanel{

        private LvrLabel username;
        private LvrLabel fiches;
        private LvrLabel cards;
        private LvrLabel cardsValue;

        public PlayerPanel(String username, int fiches, int cards, int cardsValue){
            setName(username);
            setLayout(new GridBagLayout());
            GridBagConstraints constraints = new GridBagConstraints();
            constraints.insets=new Insets(5,5,5,5);
            this.username=new LvrLabel(username);
            this.username.setFont(new Font("arial", Font.BOLD, 20));
            this.username.setForeground(Color.WHITE);
            this.fiches=new LvrLabel("Fiches: " + fiches);
            this.fiches.setFont(new Font("arial", Font.PLAIN, 18));
            this.fiches.setForeground(Color.WHITE);
            if(username.equalsIgnoreCase("Banco")){
                this.fiches.setVisible(false);
            }

            this.cards=new LvrLabel("Carte: " + cards);
            this.cards.setFont(new Font("arial", Font.PLAIN, 18));
            this.cards.setForeground(Color.WHITE);

            this.cardsValue=new LvrLabel("Valore totale: " + cardsValue);
            this.cardsValue.setFont(new Font("arial", Font.PLAIN, 18));
            this.cardsValue.setForeground(Color.WHITE);

            constraints.anchor=GridBagConstraints.CENTER;
            constraints.gridy=1;
            add(this.username, constraints);
            constraints.gridy=2;
            add(this.fiches, constraints);
            constraints.gridy=3;
            add(this.cards, constraints);
            constraints.gridy=4;
            add(this.cardsValue, constraints);
            setOpaque(false);
            setBackground(null);
            setVisible(true);
        }

        public void changeData(String username, int fiches, int cards, int cardsValue){
            this.username.setText(username);
            this.fiches.setText("Fiches: " + fiches);
            this.cards.setText("Carte: " + cards);
            this.cardsValue.setText("Valore totale: " + cardsValue);
        }
    }
}
