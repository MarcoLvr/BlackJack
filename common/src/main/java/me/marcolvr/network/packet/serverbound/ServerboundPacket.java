package me.marcolvr.network.packet.serverbound;

import me.marcolvr.network.packet.Packet;
import me.marcolvr.network.packet.clientbound.ClientboundPacket;

import java.util.Map;
import java.util.Optional;

public interface ServerboundPacket extends Packet {

    Map<Byte, Class<?>> packetMap = Map.of(
            (byte)0x02, ServerboundUsername.class,
            (byte)0x03, ServerboundRoom.class,
            (byte)0x04, ServerboundPlayAction.class,
            (byte)0x05, ServerboundFicheAction.class
    );

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

    static <T> T read(ServerboundPacket packet){
        try{
            Class<?> cPacket = packetMap.get(packet.getId());
            return (T) cPacket.cast(packet);
        }catch (ClassCastException e){
            throw new ClassCastException(packet.getClass().getSimpleName() + " is not in the packetMap");
        }
    }
}
