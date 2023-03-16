package me.marcolvr;

import lombok.Getter;
import me.marcolvr.game.BlackJackRoom;
import me.marcolvr.game.player.BlackJackPlayer;
import me.marcolvr.logger.Logger;
import me.marcolvr.network.HeartbeatTask;
import me.marcolvr.network.ServerConnection;

import java.util.ArrayList;
import java.util.List;

@Getter
public class BlackJackServer {

    private final int port;
    private final ServerConnection serverConnection;
    private final HeartbeatTask heartbeatTask;
    private final List<BlackJackPlayer> players;
    private final List<BlackJackRoom> rooms;

    public BlackJackServer(int port){
        this.port=port;
        serverConnection=new ServerConnection(port, this);
        players=new ArrayList<>();
        rooms=new ArrayList<>();
        heartbeatTask=new HeartbeatTask(this);
        heartbeatTask.start();
        Logger.info("BlackJack Server started!");
    }

    public void addPlayer(BlackJackPlayer player){
        players.add(player);
        Logger.info(player.getConnection().getAddress() + " connected");
    }

    public boolean setPlayerUsername(BlackJackPlayer player, String username){
        if(players.stream().anyMatch(p -> p.getUsername()!=null && p.getUsername().equalsIgnoreCase(username))) return false;
        player.setUsername(username);
        Logger.info(player.getConnection().getAddress() + " username is " + player.getUsername());
        return true;
    }

    public boolean joinRoom(BlackJackPlayer player, String roomId){
        BlackJackRoom room = rooms.stream().filter(r -> r.getId().equalsIgnoreCase(roomId)).findFirst().orElse(null);
        boolean res;
        if(room==null){
            room = new BlackJackRoom(roomId.replaceAll(" ", ""), player);
            rooms.add(room);
            player.setRoom(room);
            return true;
        }
        res = room.addPlayer(player);
        if(res) player.setRoom(room);
        return res;

    }

    public void disconnect(BlackJackPlayer player, String reason){
        player.getConnection().close();
        if(player.getRoom()!=null) player.getRoom().removePlayer(player);
        players.remove(player);
        Logger.info((player.getUsername() != null ? player.getUsername() : player.getConnection().getAddress()) + " disconnected. Reason: " + reason);
    }

}
