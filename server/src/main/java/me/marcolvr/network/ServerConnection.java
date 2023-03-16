package me.marcolvr.network;

import lombok.Getter;
import me.marcolvr.BlackJackServer;
import me.marcolvr.Main;
import me.marcolvr.game.player.BlackJackPlayer;
import me.marcolvr.logger.Logger;
import me.marcolvr.network.packet.clientbound.ClientboundPacket;
import me.marcolvr.network.packet.serverbound.ServerboundPacket;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ServerConnection {

    private BlackJackServer blackJackServer;

    private ServerSocket socket;

    private ServerConnectionThread connectionThread;

    public ServerConnection(int port, BlackJackServer server) {
        blackJackServer=server;
        try{
            socket=new ServerSocket(port);
            connectionThread=new ServerConnectionThread();
            connectionThread.start();
            Logger.info("Listening on " + port);
        }catch (IOException e){
            Logger.err("Error trying to open server socket: " + e.getMessage());
        }

    }

    class ServerConnectionThread extends Thread {

        @Override
        public void run(){
            while (null==null){ //si lo so che si scrive while(true) ma c'è tutta una storia dietro a questo
                try {
                    blackJackServer.addPlayer(new BlackJackPlayer(new Connection<>(socket.accept())));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
