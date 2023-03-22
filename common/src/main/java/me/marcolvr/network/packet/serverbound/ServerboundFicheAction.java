package me.marcolvr.network.packet.serverbound;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.clientbound.ClientboundPacket;

@Getter
@AllArgsConstructor
public class ServerboundFicheAction implements ServerboundPacket{

    private int fiches;
    @Override
    public byte getId() {
        return 0x05; //5
    }

}
