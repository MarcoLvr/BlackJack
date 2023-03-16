package me.marcolvr.network;

import lombok.AllArgsConstructor;
import me.marcolvr.BlackJackServer;
import me.marcolvr.game.player.BlackJackPlayer;
import me.marcolvr.network.packet.clientbound.ClientboundHeartbeat;
import me.marcolvr.network.packet.clientbound.ClientboundPacket;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HeartbeatTask extends Thread{

    private BlackJackServer server;

    private List<BlackJackPlayer> secondChance;

    public HeartbeatTask(BlackJackServer server){
        this.server=server;
        secondChance=new ArrayList<>();
    }

    @Override
    public void run(){
        while (null==null){
            Iterator<BlackJackPlayer> iterator = server.getPlayers().stream().iterator();
            while (iterator.hasNext()){
                BlackJackPlayer player = iterator.next();
                if(System.currentTimeMillis()-player.getConnection().getLastSentPacket()>5000){
                    if(!player.getConnection().sendPacket(new ClientboundHeartbeat())){
                        if(secondChance.contains(player)){
                            server.disconnect(player, "Timed out");
                            secondChance.remove(player);
                        }else{
                            secondChance.add(player);
                        }
                    };
                }
            }
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
