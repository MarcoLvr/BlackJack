package me.marcolvr.network.packet.clientbound;

import lombok.Getter;

@Getter
public class ClientboundGameUpdate implements ClientboundPacket {


    private String selection;
    @Override
    public byte getId() {
        return 0x04; //4
    }

}
