package me.marcolvr.network.packet.clientbound;

import me.marcolvr.network.Connection;

public class ClientboundPlayerUpdate implements ClientboundPacket {
    @Override
    public byte getId() {
        return 0x02; //2
    }

    @Override
    public void send(Connection con) {
        con.sendPacket(this);
    }
}