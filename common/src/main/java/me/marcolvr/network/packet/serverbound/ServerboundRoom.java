package me.marcolvr.network.packet.serverbound;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ServerboundRoom implements ServerboundPacket{

    private String room;

    @Override
    public byte getId() {
        return 0x03; //3
    }

}
