package me.marcolvr.network.packet.clientbound;

import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.serverbound.ServerboundPacket;

public class ClientboundGameUpdate implements ClientboundPacket {
    @Override
    public byte getId() {
        return 0x03; //3
    }

    @Override
    public void send(Connection<ServerboundPacket, ClientboundPacket> con) {
        con.sendPacket(this);
    }
}
