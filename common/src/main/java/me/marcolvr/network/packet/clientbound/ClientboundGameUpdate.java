package me.marcolvr.network.packet.clientbound;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ClientboundGameUpdate implements ClientboundPacket {

    private int state;
    private String selection;
    @Override
    public byte getId() {
        return 0x04; //4
    }

}
