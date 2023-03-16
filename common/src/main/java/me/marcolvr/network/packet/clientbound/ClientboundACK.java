package me.marcolvr.network.packet.clientbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.marcolvr.network.Connection;

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
    public void send(Connection con) {
        con.sendPacket(this);
    }
}
