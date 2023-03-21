package me.marcolvr.network.packet;

import me.marcolvr.network.Connection;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

public interface Packet extends Serializable {

    byte getId();

}
