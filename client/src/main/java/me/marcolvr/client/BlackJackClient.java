package me.marcolvr.client;

import lombok.Getter;
import lombok.extern.java.Log;
import me.marcolvr.client.cli.BlackJackCli;
import me.marcolvr.logger.Logger;
import me.marcolvr.client.network.ServerConnection;
import me.marcolvr.network.packet.clientbound.*;
import me.marcolvr.network.packet.serverbound.ServerboundPlayAction;
import me.marcolvr.network.packet.serverbound.ServerboundRoom;
import me.marcolvr.network.packet.serverbound.ServerboundUsername;

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
        }

    }

    private void lobbyAction(ClientboundLobbyUpdate packet){
        if(!packet.isStarting() && packet.getStartSeconds()==0) {
            System.out.println("In attesa di giocatori. Attualmente " + packet.getPlayers());
            return;
        }
        if(packet.isStarting()){
            if(packet.getStartSeconds()%10==0 || packet.getStartSeconds()<10){
                System.out.println("Avvio in " + packet.getStartSeconds());
            }
            return;
        }
        System.out.println("Avvio annullato");
    }

    public void offerUsername(String username){
        if(connection.sendPacket(new ServerboundUsername(username)))
            this.username=username;
    }

    public void offerRoom(String room){
        if(connection.sendPacket(new ServerboundRoom(room)))
            this.room=room;
    }

    private void registerCallbacks(){
        connection.onConnectionError((e)->{
            Logger.err("An error occurred with Connection. Closing client");
            System.exit(2);
        });
        connection.onPacketReceive((byte) 0, (packet)->{
            ClientboundACK ack = (ClientboundACK) packet;
            Logger.info("Received ACK of packet " + ack.getReferredPacketId()); //TODO: togli
            if(!connection.isLastPackedConfirmed()) return;
            switch (ack.getReferredPacketId()){
                case 2 ->{
                    Logger.info("Username accepted");
                    ginterface.requestRoom(false);
                }
                case 3 ->{
                    Logger.info("Joined the room");
                }
            }
        });
        connection.onPacketReceive((byte) 127, (packet)->{
            ClientboundNACK nack = (ClientboundNACK) packet;
            Logger.info("Received NACK of packet " + nack.getReferredPacketId()); //TODO: togli
            if(!connection.isLastPackedConfirmed()) return;
            switch (nack.getReferredPacketId()){
                case 2 ->{
                    Logger.info("Username refused");
                    ginterface.requestUsername(true);
                }
                case 3 ->{
                    Logger.info("Room unavailable");
                    ginterface.requestRoom(true);
                }
            }
        });
        connection.onPacketReceive((byte) 2, (packet) ->{
            ClientboundLobbyUpdate p = (ClientboundLobbyUpdate) packet;
            lobbyAction(p);
        });
        connection.onPacketReceive((byte) 4, (packet) ->{
            ClientboundGameUpdate p = (ClientboundGameUpdate) packet;
            switch (p.getState()){
                case 0 ->{
                    Logger.info("Game started");
                    System.out.println("Si inizia!");
                    System.out.println("Giocatori:");
                    ginterface.requestFiches(false);
                }
                case 1->{
                    if(ginterface.isWaitingForInput()){
                        ginterface.requestFiches(true);
                    }
                }
            }
        });
        connection.onPacketReceive((byte) 3, (packet) ->{
            ClientboundPlayerUpdate p = (ClientboundPlayerUpdate) packet;
            System.out.println(p.getUsername() + ": Ha " + p.getFiches() + " fiches e ha " + p.getCards() + " con valore totale di " + p.getCardsValue());
            Logger.info("Player update: " + p.getUsername() + " fiches: " +p.getFiches() + " totalCards: " + p.getCards() + " cardsValue: " + p.getCardsValue());
        });
        connection.onPacketReceive((byte) 4, (packet) ->{
            ClientboundGameUpdate p = (ClientboundGameUpdate) packet;
            Logger.info("Game update: " + p.getSelection() + " " + p.getState());
            switch (p.getState()){
                case 3 ->{
                    if(p.getSelection().equals(username)){
                        ginterface.requestAction();
                    }
                }
            }
        });

        connection.onPacketReceive((byte) 6, (packet)->{
            ClientboundGameEnd end = (ClientboundGameEnd) packet;
            switch (end.getState()){
                case 0 ->{
                    System.out.println("Hai perso! Hai superato 21");
                }
                case 1 ->{
                    System.out.println("Hai pareggiato col banco!");
                }
                case 2 ->{
                    System.out.println("Hai vinto! Il banco ha un valore più basso!");
                }
            }
        });

    }

    public void offerAction(String action){
        connection.sendPacket(new ServerboundPlayAction(action.equals("1")));

    }
}
