package me.marcolvr.network.packet.clientbound;

import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.serverbound.ServerboundPacket;

public class ClientboundPlayerUpdate implements ClientboundPacket {
    @Override
    public byte getId() {
        return 0x02; //2
    }

    @Override
    public void send(Connection<ServerboundPacket, ClientboundPacket> con) {
        con.sendPacket(this);
    }
}
