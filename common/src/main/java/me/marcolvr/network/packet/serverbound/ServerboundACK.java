package me.marcolvr.network.packet.serverbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.clientbound.ClientboundPacket;

@AllArgsConstructor
@Getter
@Setter

public class ServerboundACK implements ServerboundPacket{

    private byte referredPacketId;
    @Override
    public byte getId() {
        return 0x00; //0
    }

}
