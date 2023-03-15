package me.marcolvr.game.player;

import lombok.Getter;
import lombok.Setter;
import me.marcolvr.network.Connection;

@Getter
@Setter
public class BlackJackPlayer {

    private Connection conn;
    private String username;
    private String room;

    public BlackJackPlayer(Connection conn){
        this.conn=conn;
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof BlackJackPlayer p)) return false;
        return username == null ? conn.getInetAddress().equals(p.conn.getInetAddress()) : username.equalsIgnoreCase(p.username);
    }
}
