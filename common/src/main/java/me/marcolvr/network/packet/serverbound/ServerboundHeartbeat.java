package me.marcolvr.network.packet.serverbound;

import me.marcolvr.network.Connection;

public class ServerboundHeartbeat implements ServerboundPacket{
    @Override
    public byte getId() {
        return 0x01; //1
    }

    @Override
    public void send(Connection con) {
        con.sendPacket(this);
    }
}
