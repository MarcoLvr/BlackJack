package me.marcolvr.network.packet.serverbound;

import me.marcolvr.network.Connection;

public class ServerboundFicheAction implements ServerboundPacket{
    @Override
    public byte getId() {
        return 0x05; //5
    }

    @Override
    public void send(Connection con) {
        con.sendPacket(this);
    }
}
