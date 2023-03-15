package me.marcolvr.network;

import lombok.Getter;
import me.marcolvr.logger.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ServerConnection {

    private ServerSocket socket;

    private ServerConnectionThread connectionThread;

    private List<Connection> clients;

    public ServerConnection(int port) {
        try{
            socket=new ServerSocket(port);
            clients=new ArrayList<>();
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
                    clients.add(new Connection(socket.accept()));
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
