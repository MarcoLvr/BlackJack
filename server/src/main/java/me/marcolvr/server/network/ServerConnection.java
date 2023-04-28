package me.marcolvr.server.network;

import lombok.Getter;
import me.marcolvr.server.BlackJackServer;
import me.marcolvr.server.game.player.PlayerConnection;
import me.marcolvr.logger.Logger;
import me.marcolvr.server.logger.LogMessages;

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
            LogMessages.listening(port);
        }catch (IOException e){
            LogMessages.sockError(e.getMessage());
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
