package me.marcolvr.network.packet.serverbound;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.clientbound.ClientboundPacket;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ServerboundUsername implements ServerboundPacket {

    private String username;
    @Override
    public byte getId() {
        return 0x02; //2
    }

}
