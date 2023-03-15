package me.marcolvr.network.packet.serverbound;

import lombok.Getter;
import lombok.Setter;
import me.marcolvr.network.Connection;

@Getter
@Setter
public class ServerboundUsername implements ServerboundPacket {

    private String username;
    @Override
    public byte getId() {
        return 0x02; //2
    }

    @Override
    public void send(Connection con) {
        con.sendPacket(this);
    }
}
