package me.marcolvr.server.game.player;

import me.marcolvr.server.BlackJackServer;
import me.marcolvr.server.Main;
import me.marcolvr.logger.Logger;
import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.clientbound.ClientboundACK;
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
            if(!BlackJackServer.getInstance().createBlackJackPlayer(this, ((ServerboundUsername) packet).getUsername())){
                sendPacket(ClientboundPacket.createNACK((byte) 0x02));
            }else{
                sendPacket(ClientboundPacket.createACK((byte) 0x02));
            }
        });
    }


}
