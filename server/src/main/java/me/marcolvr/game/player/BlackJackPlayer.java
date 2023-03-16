package me.marcolvr.game.player;

import lombok.Getter;
import lombok.Setter;
import me.marcolvr.Main;
import me.marcolvr.game.BlackJackRoom;
import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.clientbound.ClientboundACK;
import me.marcolvr.network.packet.clientbound.ClientboundNACK;
import me.marcolvr.network.packet.clientbound.ClientboundPacket;
import me.marcolvr.network.packet.serverbound.ServerboundPacket;
import me.marcolvr.network.packet.serverbound.ServerboundRoom;
import me.marcolvr.network.packet.serverbound.ServerboundUsername;

@Getter
@Setter
public class BlackJackPlayer {

    private Connection<ServerboundPacket, ClientboundPacket> connection;
    private String username;
    private BlackJackRoom room;

    public BlackJackPlayer(Connection<ServerboundPacket, ClientboundPacket> connection){
        this.connection = connection;
        connection.onPacketReceive((byte) 2, (packet)->{
            if(username!=null || !Main.getBlackJackServer().setPlayerUsername(this, ((ServerboundUsername) packet).getUsername())){
                connection.sendPacket(new ClientboundNACK((byte) 0x02));
            }else{
                connection.sendPacket(new ClientboundACK((byte) 0x02));
            }
        });

        connection.onPacketReceive((byte) 3, (packet)->{
            if(room!=null || !Main.getBlackJackServer().joinRoom(this, ((ServerboundRoom) packet).getRoom())){
                connection.sendPacket(new ClientboundNACK((byte) 0x03));
            }else{
                connection.sendPacket(new ClientboundACK((byte) 0x03));
            }
        });

    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof BlackJackPlayer p)) return false;
        return username == null ? connection.getAddress().equals(p.connection.getAddress()) : username.equalsIgnoreCase(p.username);
    }
}
