package me.marcolvr.game.player;

import lombok.Getter;
import lombok.Setter;
import me.marcolvr.Main;
import me.marcolvr.game.BlackJackRoom;
import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.clientbound.ClientboundACK;
import me.marcolvr.network.packet.clientbound.ClientboundLobbyUpdate;
import me.marcolvr.network.packet.clientbound.ClientboundNACK;
import me.marcolvr.network.packet.clientbound.ClientboundPacket;
import me.marcolvr.network.packet.serverbound.ServerboundPacket;
import me.marcolvr.network.packet.serverbound.ServerboundRoom;
import me.marcolvr.network.packet.serverbound.ServerboundUsername;

@Getter
@Setter
public class BlackJackPlayer {

    private final PlayerConnection connection;
    private final String username;
    private BlackJackRoom room;

    public BlackJackPlayer(PlayerConnection connection, String username){
        this.connection = connection;
        this.username=username;
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
