package me.marcolvr.server.game.player;

import me.marcolvr.server.BlackJackServer;
import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.clientbound.ClientboundPacket;
import me.marcolvr.network.packet.serverbound.ServerboundPacket;
import me.marcolvr.network.packet.serverbound.ServerboundUsername;
import me.marcolvr.server.logger.LogMessages;

import java.net.Socket;

public class PlayerConnection extends Connection<ServerboundPacket, ClientboundPacket>{

    public PlayerConnection(Socket socket){
        super(socket);
        LogMessages.newConnection(getAddress());
        onPacketReceive((byte) 2, (packet)->{
            if(!BlackJackServer.getInstance().createBlackJackPlayer(this, ((ServerboundUsername) packet).getUsername())){
                sendPacket(ClientboundPacket.NACK((byte) 0x02));
            }else{
                sendPacket(ClientboundPacket.ACK((byte) 0x02));
            }
        });
    }


}
