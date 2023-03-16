package me.marcolvr.network.packet.serverbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.marcolvr.network.Connection;

@AllArgsConstructor
@Getter
@Setter

public class ServerboundACK implements ServerboundPacket{

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
