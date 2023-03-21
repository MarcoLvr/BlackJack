package me.marcolvr.network.packet.serverbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.clientbound.ClientboundPacket;

@Getter
@Setter
@AllArgsConstructor

public class ServerboundNACK implements ServerboundPacket{

    private byte referredPacketId;
    @Override
    public byte getId() {
        return 0x7F; //127
    }

}
