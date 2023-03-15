package me.marcolvr.network.packet.clientbound;

import me.marcolvr.network.Connection;

public class ClientboundGameUpdate implements ClientboundPacket {
    @Override
    public byte getId() {
        return 0x03; //3
    }

    @Override
    public void send(Connection con) {
        con.sendPacket(this);
    }
}
