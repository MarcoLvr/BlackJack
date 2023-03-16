package me.marcolvr.network.packet.serverbound;

import me.marcolvr.network.packet.Packet;
import me.marcolvr.network.packet.clientbound.ClientboundPacket;

public interface ServerboundPacket extends Packet<ClientboundPacket, ServerboundPacket> {
}
