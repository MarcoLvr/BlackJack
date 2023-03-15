package me.marcolvr;

import me.marcolvr.game.BlackJackGame;
import me.marcolvr.network.packet.Packet;
import me.marcolvr.network.packet.PacketHandler;
import me.marcolvr.network.packet.serverbound.ServerboundACK;
import me.marcolvr.utils.Pair;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        BlackJackGame game = new BlackJackGame();

        ServerboundACK ack = new ServerboundACK();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(1);
        ObjectOutputStream out = new ObjectOutputStream(outputStream);
        out.writeObject(ack);

        PacketHandler handler = new PacketHandler();
        Pair<Integer, Packet> hnad = handler.handle(new ByteArrayInputStream(outputStream.toByteArray()));
        System.out.printf(hnad.getSecond().getClass().getSimpleName());

    }
}