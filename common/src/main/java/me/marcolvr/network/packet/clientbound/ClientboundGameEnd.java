package me.marcolvr.network.packet.clientbound;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ClientboundGameEnd implements ClientboundPacket {


    private int state;
    @Override
    public byte getId() {
        return 0x06; //6
    }

}
