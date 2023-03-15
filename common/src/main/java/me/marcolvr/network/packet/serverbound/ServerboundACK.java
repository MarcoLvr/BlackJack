package me.marcolvr.network.packet.serverbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.marcolvr.network.Connection;

@AllArgsConstructor
@Getter
public class ServerboundACK implements ServerboundPacket{

    private int options;
    @Override
    public byte getId() {
        return 0x00; //0
    }

    @Override
    public void send(Connection con) {
        con.sendPacket(this);
    }
}
