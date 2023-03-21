package me.marcolvr.network.packet;

import me.marcolvr.utils.Pair;

import java.io.*;

public class PacketHandler {


    public Pair<Byte, Packet> handle(ByteArrayInputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream stream = new ObjectInputStream(in);
        Packet p = (Packet) stream.readObject();
        return new Pair<>(p.getId(), p);
    }

    public byte[] toByteArray(Packet p) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(stream);
        objectOutputStream.writeObject(p);
        return stream.toByteArray();
    }


}
