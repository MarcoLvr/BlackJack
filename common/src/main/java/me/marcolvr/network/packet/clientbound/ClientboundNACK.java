package me.marcolvr.network.packet.clientbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.marcolvr.network.Connection;

@Getter
@Setter
@AllArgsConstructor

public class ClientboundNACK implements ClientboundPacket{

    private byte referredPacketId;
    @Override
    public byte getId() {
        return 0x7F; //127
    }

    @Override
    public void send(Connection con) {
        con.sendPacket(this);
    }
}
