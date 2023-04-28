package me.marcolvr.network.packet.serverbound;

import me.marcolvr.network.packet.Packet;
import me.marcolvr.network.packet.clientbound.ClientboundPacket;

public interface ServerboundPacket extends Packet {

    static ServerboundFicheAction ficheAction(int fiches){
        return new ServerboundFicheAction(fiches);
    }

    static ServerboundPlayAction playAction(boolean addCard){
        return new ServerboundPlayAction(addCard);
    }

    static ServerboundRoom room(String room){
        return new ServerboundRoom(room);
    }

    static ServerboundUsername username(String username){
        return new ServerboundUsername(username);
    }
}
