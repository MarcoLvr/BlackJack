package me.marcolvr.network.packet.serverbound;

import lombok.Getter;
import lombok.Setter;
import me.marcolvr.network.Connection;

@Getter
@Setter
public class ServerboundNACK implements ServerboundPacket{

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
