package me.marcolvr.network.packet.clientbound;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ClientboundPlayerUpdate implements ClientboundPacket {

    private String username;
    private int fiches;
    private int cardsValue;
    private int cards;

    @Override
    public byte getId() {
        return 0x03; //3
    }
}
