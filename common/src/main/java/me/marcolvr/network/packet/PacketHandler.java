package me.marcolvr.network.packet;

import me.marcolvr.network.packet.serverbound.ServerboundACK;
import me.marcolvr.utils.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PacketHandler {


    public Pair<Byte, Packet> handle(ByteArrayInputStream in) throws IOException, ClassNotFoundException {
        byte[] packetId = new byte[1];
        in.read(packetId, 0, packetId.length);
        ObjectInputStream stream = new ObjectInputStream(in);
        in.close();
        return new Pair<>(packetId[0], (Packet) stream.readObject());
    }

    public byte[] toByteArray(Packet p) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream);
        stream.write(p.getId());
        objectOutputStream.writeObject(p);
        return stream.toByteArray();
    }


}
