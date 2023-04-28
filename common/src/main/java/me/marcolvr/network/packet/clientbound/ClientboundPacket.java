package me.marcolvr.network.packet.clientbound;

import me.marcolvr.network.packet.Packet;
import me.marcolvr.network.packet.serverbound.ServerboundPacket;

public interface ClientboundPacket extends Packet {

    enum GameEndState { LOSE, TIE, WIN }
    enum GameUpdateState { INIT, FICHES_REQUEST, START, PLAYER_ACTION }
    static ClientboundACK createACK(byte referredPacketId){
        return new ClientboundACK(referredPacketId);
    }

    static ClientboundDealerUpdate createDealerUpdate(int cards, int cardsValue, byte lastCardId){
        return new ClientboundDealerUpdate(cards, cardsValue, lastCardId);
    }

    static ClientboundGameEnd createGameEnd(GameEndState state){
        return new ClientboundGameEnd(state.ordinal());
    }

    static ClientboundGameUpdate createGameUpdate(GameUpdateState state, String username){
        return new ClientboundGameUpdate(state.ordinal(), username);
    }

    static ClientboundHeartbeat createHeartbeat(){
        return new ClientboundHeartbeat();
    }

    static ClientboundLobbyUpdate createLobbyUpdate(boolean starting, int startSeconds, int players){
        return new ClientboundLobbyUpdate(starting, startSeconds, players);
    }

    static ClientboundNACK createNACK(byte referredPacketId) {
        return new ClientboundNACK(referredPacketId);
    }

    static ClientboundPlayerUpdate createPlayerUpdate(String username, int fiches, int cardsValue, int cards){
        return new ClientboundPlayerUpdate(username, fiches, cardsValue, cards);
    }



}
