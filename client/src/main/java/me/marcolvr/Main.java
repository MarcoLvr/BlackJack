package me.marcolvr;

import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.clientbound.ClientboundACK;
import me.marcolvr.network.packet.clientbound.ClientboundNACK;
import me.marcolvr.network.packet.clientbound.ClientboundPacket;
import me.marcolvr.network.packet.serverbound.ServerboundPacket;
import me.marcolvr.network.packet.serverbound.ServerboundRoom;
import me.marcolvr.network.packet.serverbound.ServerboundUsername;

import java.io.IOException;
import java.net.Socket;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Connection<ClientboundPacket, ServerboundPacket> conn = new Connection<>(new Socket("localhost", 24512));
        conn.onPacketReceive((byte) 0, (packet)->{
            System.out.println("ACK of " + ((ClientboundACK) packet).getReferredPacketId());
        });
        conn.onPacketReceive((byte) 127, (packet)->{
            System.out.println("NACK of " + ((ClientboundNACK) packet).getReferredPacketId());
        });
        conn.sendPacket(new ServerboundUsername("MarcoLvr"));
        Thread.sleep(1000);
        conn.sendPacket(new ServerboundRoom("test"));
    }
}