package me.marcolvr.network.packet.serverbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.clientbound.ClientboundPacket;

@Getter
@Setter
@AllArgsConstructor
public class ServerboundRoom implements ServerboundPacket{

    private String room;

    @Override
    public byte getId() {
        return 0x03; //3
    }

    @Override
    public void send(Connection<ClientboundPacket, ServerboundPacket> con) {
        con.sendPacket(this);
    }
}
