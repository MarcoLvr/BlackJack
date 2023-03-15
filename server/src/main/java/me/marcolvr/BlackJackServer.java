package me.marcolvr;

import me.marcolvr.game.player.BlackJackPlayer;
import me.marcolvr.network.ServerConnection;

import java.util.ArrayList;
import java.util.List;

public class BlackJackServer {

    private int port;
    private ServerConnection serverConnection;
    private List<BlackJackPlayer> players;

    public BlackJackServer(int port){
        this.port=port;
        serverConnection=new ServerConnection(port);
        players=new ArrayList<>();
    }

    public void addPlayer(BlackJackPlayer player){
        players.add(player);
    }
}
