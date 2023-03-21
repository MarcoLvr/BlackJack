package me.marcolvr.server.network;

import me.marcolvr.server.BlackJackServer;
import me.marcolvr.server.game.player.BlackJackPlayer;
import me.marcolvr.network.packet.clientbound.ClientboundHeartbeat;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HeartbeatTask extends Thread{

    private BlackJackServer server;

    public HeartbeatTask(BlackJackServer server){
        this.server=server;
    }

    @Override
    public void run(){
        List<BlackJackPlayer> toDisconnect = new ArrayList<>();
        while (null==null){
            toDisconnect.clear();
            Iterator<BlackJackPlayer> iterator = server.getPlayers().iterator();
            while (iterator.hasNext()){
                BlackJackPlayer player = iterator.next();
                if(System.currentTimeMillis()-player.getConnection().getLastSentPacket()>5000){
                    if(!player.getConnection().sendPacket(new ClientboundHeartbeat())){
                        toDisconnect.add(player);
                    };
                }
            }
            toDisconnect.forEach(blackJackPlayer -> {
                server.disconnect(blackJackPlayer, "Timed out");
            });
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
