package me.marcolvr.network.packet.clientbound;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClientboundDealerUpdate implements ClientboundPacket {

    private int cardsValue;
    private int cards;

    @Override
    public byte getId() {
        return 0x05; //5
    }

}
