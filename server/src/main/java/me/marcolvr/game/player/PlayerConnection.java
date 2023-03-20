package me.marcolvr.game.player;

import me.marcolvr.Main;
import me.marcolvr.logger.Logger;
import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.clientbound.ClientboundACK;
import me.marcolvr.network.packet.clientbound.ClientboundLobbyUpdate;
import me.marcolvr.network.packet.clientbound.ClientboundNACK;
import me.marcolvr.network.packet.clientbound.ClientboundPacket;
import me.marcolvr.network.packet.serverbound.ServerboundPacket;
import me.marcolvr.network.packet.serverbound.ServerboundUsername;

import java.net.Socket;

public class PlayerConnection extends Connection<ServerboundPacket, ClientboundPacket>{

    public PlayerConnection(Socket socket){
        super(socket);
        Logger.info(getAddress() + " connected");
        onPacketReceive((byte) 2, (packet)->{
            if(!Main.getBlackJackServer().createBlackJackPlayer(this, ((ServerboundUsername) packet).getUsername())){
                sendPacket(new ClientboundNACK((byte) 0x02));
            }else{
                sendPacket(new ClientboundACK((byte) 0x02));
            }
        });
    }


}
