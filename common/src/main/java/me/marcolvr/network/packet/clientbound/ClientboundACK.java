package me.marcolvr.network.packet.clientbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.serverbound.ServerboundPacket;

@Getter
@Setter
@AllArgsConstructor
public class ClientboundACK implements ClientboundPacket {

    private byte referredPacketId;
    @Override
    public byte getId() {
        return 0x00; //0
    }

    @Override
    public void send(Connection<ServerboundPacket, ClientboundPacket> con) {
        con.sendPacket(this);
    }
}
