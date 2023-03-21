package me.marcolvr.network.packet.serverbound;

import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.clientbound.ClientboundPacket;

public class ServerboundFicheAction implements ServerboundPacket{
    @Override
    public byte getId() {
        return 0x05; //5
    }

}
