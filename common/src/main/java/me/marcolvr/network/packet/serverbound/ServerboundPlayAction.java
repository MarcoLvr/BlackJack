package me.marcolvr.network.packet.serverbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.clientbound.ClientboundPacket;

@Getter
@AllArgsConstructor
public class ServerboundPlayAction implements ServerboundPacket{

    public boolean addCard;

    @Override
    public byte getId() {
        return 0x04; //4
    }

}
