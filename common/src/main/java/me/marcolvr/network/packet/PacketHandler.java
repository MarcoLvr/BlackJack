package me.marcolvr.network.packet;

import me.marcolvr.network.packet.serverbound.ServerboundACK;
import me.marcolvr.utils.Pair;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class PacketHandler {


    public Pair<Integer, Packet> handle(ByteArrayInputStream in) throws IOException, ClassNotFoundException {
        byte[] packetId = new byte[1];
        in.read(packetId, 0, packetId.length);
        ObjectInputStream stream = new ObjectInputStream(in);
        return new Pair<>((int)packetId[0], (Packet) stream.readObject());
    }


}
