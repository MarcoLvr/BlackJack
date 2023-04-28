package me.marcolvr.server;

import lombok.Getter;
import me.marcolvr.network.packet.clientbound.ClientboundPacket;
import me.marcolvr.server.game.BlackJackRoom;
import me.marcolvr.server.network.HeartbeatTask;
import me.marcolvr.server.network.ServerConnection;
import me.marcolvr.server.game.player.BlackJackPlayer;
import me.marcolvr.server.game.player.PlayerConnection;
import me.marcolvr.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
public class BlackJackServer {

    @Getter
    private static BlackJackServer instance;

    private final int port;
    private final ServerConnection serverConnection;
    private final HeartbeatTask heartbeatTask;
    private final List<BlackJackPlayer> players;
    private final List<BlackJackRoom> rooms;

    public BlackJackServer(int port){
        instance=this;
        this.port=port;
        serverConnection=new ServerConnection(port, this);
        players=new ArrayList<>();
        rooms=new ArrayList<>();
        heartbeatTask=new HeartbeatTask(this);
        heartbeatTask.start();
        Logger.info("BlackJack Server started!");
    }

    public boolean createBlackJackPlayer(PlayerConnection connection, String username){
        if(username.isEmpty() || username.equalsIgnoreCase("banco")) return false;
        if(players.stream().anyMatch(p -> p.getUsername()!=null && p.getUsername().equalsIgnoreCase(username))) return false;
        BlackJackPlayer player = new BlackJackPlayer(connection, username);
        players.add(player);
        Logger.info(player.getConnection().getAddress() + " logged in as " + player.getUsername());
        return true;
    }

    public boolean joinRoom(BlackJackPlayer player, String roomId){
        if(roomId.isEmpty()) return false;
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

    public void multicast(ClientboundPacket packet, String roomId){
        Optional<BlackJackRoom> room = rooms.stream().filter(r -> r.getId().equalsIgnoreCase(roomId)).findFirst();
        if(room.isEmpty()) return;
        multicast(packet, room.get());
    }

    public void multicast(ClientboundPacket packet, BlackJackRoom room){
        assert room != null;
        room.getPlayers().forEach(player -> {
            player.getConnection().sendPacket(packet);
        });
    }

}
