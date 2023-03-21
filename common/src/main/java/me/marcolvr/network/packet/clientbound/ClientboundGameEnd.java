package me.marcolvr.network.packet.clientbound;

import lombok.Getter;

@Getter
public class ClientboundGameEnd implements ClientboundPacket {


    private String winner;
    @Override
    public byte getId() {
        return 0x06; //6
    }

}
