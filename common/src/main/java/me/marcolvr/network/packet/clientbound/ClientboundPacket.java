package me.marcolvr.network.packet.clientbound;

import me.marcolvr.network.packet.Packet;
import me.marcolvr.network.packet.serverbound.ServerboundPacket;

public interface ClientboundPacket extends Packet<ServerboundPacket, ClientboundPacket> {


}
