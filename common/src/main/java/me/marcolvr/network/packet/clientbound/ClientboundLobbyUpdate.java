package me.marcolvr.network.packet.clientbound;

import me.marcolvr.network.Connection;

public class ClientboundLobbyUpdate implements ClientboundPacket {
    @Override
    public byte getId() {
        return 0x01; //1
    }

    @Override
    public void send(Connection con) {
        con.sendPacket(this);
    }
}
