package me.marcolvr.network.packet.clientbound;

import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.serverbound.ServerboundPacket;

public class ClientboundHeartbeat implements ClientboundPacket{
    @Override
    public byte getId() {
        return 0x01; //1
    }

    @Override
    public void send(Connection con) {
        con.sendPacket(this);
    }
}
