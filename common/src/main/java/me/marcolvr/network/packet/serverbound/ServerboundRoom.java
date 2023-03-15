package me.marcolvr.network.packet.serverbound;

import lombok.Getter;
import lombok.Setter;
import me.marcolvr.network.Connection;

@Getter
@Setter
public class ServerboundRoom implements ServerboundPacket{

    private String room;
    @Override
    public byte getId() {
        return 0x03; //3
    }

    @Override
    public void send(Connection con) {
        con.sendPacket(this);
    }
}
