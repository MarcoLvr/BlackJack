package me.marcolvr.server.network;

import lombok.Getter;
import me.marcolvr.server.BlackJackServer;
import me.marcolvr.server.game.player.PlayerConnection;
import me.marcolvr.logger.Logger;

import java.io.IOException;
import java.net.ServerSocket;

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
            while (true){
                try {
                    new PlayerConnection(socket.accept());
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
