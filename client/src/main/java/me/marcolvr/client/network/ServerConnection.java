package me.marcolvr.client.network;

import lombok.Getter;
import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.clientbound.ClientboundACK;
import me.marcolvr.network.packet.clientbound.ClientboundNACK;
import me.marcolvr.network.packet.clientbound.ClientboundPacket;
import me.marcolvr.network.packet.serverbound.ServerboundPacket;

import java.net.Socket;

@Getter
public class ServerConnection extends Connection<ClientboundPacket, ServerboundPacket> {

    private byte lastSentPackedId;
    private boolean lastPackedConfirmed;
    public ServerConnection(Socket socket) {
        super(socket);
        lastPackedConfirmed=true;
        onPacketReceive((byte) 0, (packet)->{
            ClientboundACK ack = (ClientboundACK) packet;
            if(ack.getReferredPacketId()!=lastSentPackedId) return;
            lastPackedConfirmed=true;
        });
        onPacketReceive((byte) 127, (packet)->{
            ClientboundNACK nack = (ClientboundNACK) packet;
            if(nack.getReferredPacketId()!=lastSentPackedId) return;
            lastPackedConfirmed=true;
        });
    }

    @Override
    public boolean sendPacket(ServerboundPacket p) {
        if(!lastPackedConfirmed) return false;
        boolean res = super.sendPacket(p);
        if(!res) return false;
        lastSentPackedId=p.getId();
        lastPackedConfirmed=false;
        return true;
    }


}

