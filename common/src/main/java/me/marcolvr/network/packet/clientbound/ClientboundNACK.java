package me.marcolvr.network.packet.clientbound;

import lombok.Getter;
import lombok.Setter;
import me.marcolvr.network.Connection;

@Getter
@Setter
public class ClientboundNACK implements ClientboundPacket{

    private int options;
    @Override
    public byte getId() {
        return 0x7F; //127
    }

    @Override
    public void send(Connection con) {
        con.sendPacket(this);
    }
}
