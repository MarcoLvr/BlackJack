package me.marcolvr.network.packet.clientbound;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.serverbound.ServerboundPacket;

@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class ClientboundHeartbeat implements ClientboundPacket{
    @Override
    public byte getId() {
        return 0x01; //1
    }

}
