package me.marcolvr.network.packet.clientbound;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientboundGameUpdate implements ClientboundPacket {

    private int state;
    private String selection;
    @Override
    public byte getId() {
        return 0x04; //4
    }

}
