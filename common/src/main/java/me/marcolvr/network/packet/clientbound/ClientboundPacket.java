package me.marcolvr.network.packet.clientbound;

import me.marcolvr.network.packet.Packet;

public interface ClientboundPacket extends Packet {

    enum GameEndState { LOSE, TIE, WIN }
    enum GameUpdateState { INIT, FICHES_REQUEST, START, PLAYER_ACTION }
    static ClientboundACK ACK(byte referredPacketId){
        return new ClientboundACK(referredPacketId);
    }

    static ClientboundDealerUpdate dealerUpdate(int cards, int cardsValue, byte lastCardId){
        return new ClientboundDealerUpdate(cards, cardsValue, lastCardId);
    }

    static ClientboundGameEnd gameEnd(GameEndState state){
        return new ClientboundGameEnd(state.ordinal());
    }

    static ClientboundGameUpdate gameUpdate(GameUpdateState state, String username){
        return new ClientboundGameUpdate(state.ordinal(), username);
    }

    static ClientboundHeartbeat heartbeat(){
        return new ClientboundHeartbeat();
    }

    static ClientboundLobbyUpdate lobbyUpdate(boolean starting, int startSeconds, int players){
        return new ClientboundLobbyUpdate(starting, startSeconds, players);
    }

    static ClientboundNACK NACK(byte referredPacketId) {
        return new ClientboundNACK(referredPacketId);
    }

    static ClientboundPlayerUpdate playerUpdate(String username, int fiches, int cardsValue, int cards){
        return new ClientboundPlayerUpdate(username, fiches, cardsValue, cards);
    }



}
