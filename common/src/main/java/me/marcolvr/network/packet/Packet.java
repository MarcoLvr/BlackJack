package me.marcolvr.network.packet;

import me.marcolvr.network.Connection;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public interface Packet<S extends Packet,R extends Packet> extends Serializable {

    byte getId();

    void send(Connection<S,R> con);

}
