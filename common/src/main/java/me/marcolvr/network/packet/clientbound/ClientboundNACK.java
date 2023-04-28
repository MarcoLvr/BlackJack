package me.marcolvr.network.packet.clientbound;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.serverbound.ServerboundPacket;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ClientboundNACK implements ClientboundPacket{

    private byte referredPacketId;
    @Override
    public byte getId() {
        return 0x7F; //127
    }

}
