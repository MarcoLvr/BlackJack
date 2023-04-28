package me.marcolvr.network.packet.clientbound;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ClientboundDealerUpdate implements ClientboundPacket {

    private int cardsValue;
    private int cards;
    private byte lastCardId;

    @Override
    public byte getId() {
        return 0x05; //5
    }

}
