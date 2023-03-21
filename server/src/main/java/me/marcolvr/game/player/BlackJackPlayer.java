package me.marcolvr.game.player;

import lombok.Getter;
import lombok.Setter;
import me.marcolvr.Main;
import me.marcolvr.game.BlackJackRoom;
import me.marcolvr.network.packet.clientbound.ClientboundACK;
import me.marcolvr.network.packet.clientbound.ClientboundLobbyUpdate;
import me.marcolvr.network.packet.clientbound.ClientboundNACK;
import me.marcolvr.network.packet.serverbound.ServerboundRoom;

@Getter
@Setter
public class BlackJackPlayer {

    private final PlayerConnection connection;
    private final String username;
    private BlackJackRoom room;
    private int fiches;
    private int lastTransaction;

    public BlackJackPlayer(PlayerConnection connection, String username){
        this.connection = connection;
        this.username=username;
        connection.onConnectionError((e)->{
            Main.getBlackJackServer().disconnect(this, "Connection Error: " + e.getMessage());
        });
        connection.onPacketReceive((byte) 3, (packet)->{
            if(room!=null || !Main.getBlackJackServer().joinRoom(this, ((ServerboundRoom) packet).getRoom())){
                connection.sendPacket(new ClientboundNACK((byte) 0x03));
            }else{
                connection.sendPacket(new ClientboundACK((byte) 0x03));
                connection.sendPacket(new ClientboundLobbyUpdate(room.getState()==1, room.getTime(), room.getPlayers().size()));
            }
        });

    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof BlackJackPlayer p)) return false;
        return username == null ? connection.getAddress().equals(p.connection.getAddress()) : username.equalsIgnoreCase(p.username);
    }
}
