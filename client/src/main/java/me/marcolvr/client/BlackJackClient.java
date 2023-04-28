package me.marcolvr.client;

import lombok.Getter;
import lombok.extern.java.Log;
import me.marcolvr.client.cli.BlackJackCli;
import me.marcolvr.client.cli.BlackJackGui;
import me.marcolvr.logger.Logger;
import me.marcolvr.client.network.ServerConnection;
import me.marcolvr.network.packet.clientbound.*;
import me.marcolvr.network.packet.serverbound.*;

import java.io.IOException;
import java.net.Socket;

@Getter
public class BlackJackClient {

    private String username;
    private String room;
    private ServerConnection connection;
    private final boolean cli;
    private BlackJackInterface ginterface;

    public BlackJackClient(String host, int port, boolean cli){
        this.cli=cli;
        try {
            connection=new ServerConnection(new Socket(host, port));
        } catch (IOException e) {
            Logger.err("Error initializing socket: " + e.getMessage());
            System.exit(1);
        }
        //Register packet callbacks
        registerCallbacks();

        //starting cli/gui client
        if(cli){
            ginterface=new BlackJackCli(this);
        }else{
            ginterface=new BlackJackGui(this);
        }

    }

    public void offerUsername(String username){
        if(connection.sendPacket(ServerboundPacket.username(username)))
            this.username=username;
    }

    public void offerRoom(String room){
        if(connection.sendPacket(ServerboundPacket.room(room)))
            this.room=room;
    }

    public void offerFiches(int fiches){
       getConnection().sendPacket(ServerboundPacket.ficheAction(Math.abs(fiches)*-1));
    }

    private void registerCallbacks(){
        connection.onConnectionError((e)->{
            Logger.err("An error occurred with Connection. Closing client");
            System.exit(2);
        });
        connection.onPacketReceive((byte) 0, (packet)->{
            ClientboundACK ack = (ClientboundACK) packet;
            Logger.info("ACK: packet=" + ack.getReferredPacketId());
            if(!connection.isLastPackedConfirmed()) return;
            if (ack.getReferredPacketId() == 2) {
                ginterface.requestRoom(false);
            }
        });
        connection.onPacketReceive((byte) 127, (packet)->{
            ClientboundNACK nack = (ClientboundNACK) packet;
            Logger.info("NACK: packet=" + nack.getReferredPacketId());
            if(!connection.isLastPackedConfirmed()) return;
            switch (nack.getReferredPacketId()){
                case 2 ->{
                    ginterface.requestUsername(true);
                }
                case 3 ->{
                    ginterface.requestRoom(true);
                }
            }
        });
        connection.onPacketReceive((byte) 2, (packet) ->{
            ClientboundLobbyUpdate p = (ClientboundLobbyUpdate) packet;
            Logger.info("Lobby update: players=" + p.getPlayers() + " starting=" + p.isStarting() + " time=" + p.getStartSeconds());
            ginterface.updateLobbyStatus(p.getPlayers(), p.isStarting(), p.getStartSeconds());
        });
        connection.onPacketReceive((byte) 4, (packet) ->{
            ClientboundGameUpdate p = (ClientboundGameUpdate) packet;
            Logger.info("Game update: selection=" + p.getSelection() + " state=" + p.getState());
            ginterface.gameUpdate(p.getSelection(), p.getState());
        });
        connection.onPacketReceive((byte) 3, (packet) ->{
            ClientboundPlayerUpdate p = (ClientboundPlayerUpdate) packet;
            Logger.info("Player update: username=" + p.getUsername() + " fiches=" +p.getFiches() + " cards=" + p.getCards() + " cardsValue=" + p.getCardsValue());
            ginterface.playerUpdate(p.getUsername(), p.getFiches(), p.getCards(), p.getCardsValue());
        });

        connection.onPacketReceive((byte) 6, (packet)->{
            ClientboundGameEnd end = (ClientboundGameEnd) packet;
            Logger.info("Game End: state=" + end.getState());
            ginterface.gameEnd(end.getState());
        });

    }

    public void offerAction(String action){
        connection.sendPacket(ServerboundPacket.playAction(action.equals("1")));

    }
}
