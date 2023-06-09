package me.marcolvr.network.packet.clientbound;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import me.marcolvr.network.Connection;
import me.marcolvr.network.packet.serverbound.ServerboundPacket;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class ClientboundLobbyUpdate implements ClientboundPacket {

    private boolean starting;
    private int startSeconds;
    private int players;
    @Override
    public byte getId() {
        return 0x02; //1
    }
}
