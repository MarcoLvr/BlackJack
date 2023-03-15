package me.marcolvr.network.packet.serverbound;

import me.marcolvr.network.Connection;

public class ServerboundPlayAction implements ServerboundPacket{
    @Override
    public byte getId() {
        return 0x04; //4
    }

    @Override
    public void send(Connection con) {
        con.sendPacket(this);
    }
}
