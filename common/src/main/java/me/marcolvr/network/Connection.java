package me.marcolvr.network;

import lombok.Getter;
import me.marcolvr.network.packet.Packet;
import me.marcolvr.network.packet.PacketHandler;
import me.marcolvr.utils.Pair;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class Connection {

    private ConnectionThread connThread;
    @Getter
    private PacketHandler handler;

    private Map<Byte, List<Consumer<Packet>>> packetActions;

    public Connection(Socket socket){
        handler=new PacketHandler();
        packetActions=new HashMap<>();
        connThread=new ConnectionThread(socket);
        connThread.start();
    }

    public void onPacketReceive(byte packetId, Consumer<Packet> action){
        if(packetActions.containsKey(packetId)) packetActions.put(packetId, new ArrayList<>());
        packetActions.get(packetId).add(action);
    }

    public void clearPacketActions(byte packetId){
        packetActions.remove(packetId);
    }


    public void sendPacket(Packet p){
        try {
            connThread.send(handler.toByteArray(p));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public InetAddress getInetAddress(){
        return connThread.socket.getInetAddress();
    }

    public void close(){
        try {
            connThread.socket.close();
            connThread.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Getter
    class ConnectionThread extends Thread{

        private Socket socket;
        public ConnectionThread(Socket sock){
            this.socket=sock;
        }

        public void send(byte[] data) throws IOException {
            socket.getOutputStream().write(data);
        }


        @Override
        public void run(){
            while (null==null){
                if(!socket.isConnected() || socket.isClosed()) break;
                try{
                    if(socket.getInputStream().available()!=0){
                        Pair<Byte, Packet> data = handler.handle(new ByteArrayInputStream(socket.getInputStream().readNBytes(socket.getInputStream().available())));
                        if(packetActions.containsKey(data.getFirst()))
                            packetActions.get(data.getFirst()).forEach(action -> action.accept(data.getSecond()));
                    }
                }catch (IOException | ClassNotFoundException e){
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
