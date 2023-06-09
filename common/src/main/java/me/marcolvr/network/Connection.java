package me.marcolvr.network;

import lombok.Getter;
import me.marcolvr.network.packet.Packet;
import me.marcolvr.network.packet.PacketHandler;
import me.marcolvr.utils.Pair;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.*;
import java.util.function.Consumer;

public class Connection<R extends Packet, S extends Packet> {

    private ConnectionThread connThread;
    @Getter
    private PacketHandler handler;
    @Getter
    private long lastSentPacket;

    private Map<Byte, List<Consumer<R>>> packetActions;

    private Consumer<Exception> errorAction;

    public Connection(Socket socket){
        handler=new PacketHandler();
        packetActions=new HashMap<>();
        try{
            connThread=new ConnectionThread(socket);
            connThread.start();
        }catch (Exception e){
            e.printStackTrace();
        }
        lastSentPacket=0;
    }

    public boolean isClosed(){
        return connThread.socket.isClosed() || !connThread.socket.isConnected();
    }

    public void onPacketReceive(Byte packetId, Consumer<R> action){
        if(!packetActions.containsKey(packetId)) packetActions.put(packetId, new ArrayList<>());
        packetActions.get(packetId).add(action);
    }

    public void onConnectionError(Consumer<Exception> action){
        errorAction=action;
    }

    public void clearPacketActions(Byte packetId){
        packetActions.remove(packetId);
    }


    public boolean sendPacket(S p){
        try {
            connThread.send(handler.toByteArray(p));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public String getAddress(){
        return connThread.socket.getRemoteSocketAddress().toString();
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

        private final Socket socket;
        private final BufferedInputStream inputStream;
        public ConnectionThread(Socket sock) throws IOException {
            this.socket=sock;
            socket.setSoTimeout(5000);
            socket.setKeepAlive(true);
            inputStream=new BufferedInputStream(socket.getInputStream());
        }

        public void send(byte[] data) throws IOException {
            ByteBuffer buf = ByteBuffer.allocate(4+data.length);
            buf.putInt(data.length);
            buf.put(data);
            socket.getOutputStream().write(buf.array());
            lastSentPacket=System.currentTimeMillis();
        }



        @Override
        public void run(){
            while (socket.isConnected() && !socket.isClosed()){
                try{
                    if(inputStream.available()!=0){
                        byte[] length = inputStream.readNBytes(4);
                        ByteBuffer buf = ByteBuffer.wrap(length);
                        int len = buf.getInt();
                        Pair<Byte, R> data = (Pair<Byte, R>) handler.handle(new ByteArrayInputStream(inputStream.readNBytes(len)));
                        if(packetActions.containsKey(data.getFirst()))
                            packetActions.get(data.getFirst()).forEach(action -> action.accept(data.getSecond()));
                    }
                }catch (IOException | ClassNotFoundException e){
                    if(errorAction!=null) errorAction.accept(e);
                }
                try {
                    sleep(1);
                } catch (InterruptedException ignored) {}
            }
        }
    }
}
