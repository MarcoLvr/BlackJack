package me.marcolvr.server.game;

import lombok.Getter;
import me.marcolvr.network.packet.clientbound.ClientboundGameUpdate;
import me.marcolvr.network.packet.clientbound.ClientboundPlayerUpdate;
import me.marcolvr.server.Main;
import me.marcolvr.server.game.logic.BlackJackGame;
import me.marcolvr.server.game.player.BlackJackPlayer;
import me.marcolvr.logger.Logger;
import me.marcolvr.network.packet.clientbound.ClientboundLobbyUpdate;

import java.util.ArrayList;
import java.util.List;

public class BlackJackRoom {

    private final int START_SECONDS = 5;
    private final int MIN_PLAYERS = 2;

    @Getter
    private final String id;
    @Getter
    private List<BlackJackPlayer> players;
    @Getter
    private BlackJackGame logic;

    private BlackJackRoomTicker ticker;

    @Getter
    private int state;

    @Getter
    private int time;

    public BlackJackRoom(String id, BlackJackPlayer creator){
        this.id=id;
        state=0;
        players=new ArrayList<>();
        logic=new BlackJackGame();
        ticker=new BlackJackRoomTicker();
        Logger.info("[" + id + "] Room created.");
        addPlayer(creator);
        ticker.start();
    }

    public boolean isFull(){
        return players.size()==5;
    }

    public boolean addPlayer(BlackJackPlayer player){
        if(players.contains(player) || isFull() || state>1) {
            Logger.info("[" + id + "] " + player.getUsername() +" tried to join room but at the moment it isn't accessible.");
            return false;
        }
        players.add(player);
        Logger.info("[" + id + "] " + player.getUsername() +" joined the room.");
        if(players.size()==MIN_PLAYERS){
            time=START_SECONDS;
            state=1;
        }
        return true;
    }

    public boolean removePlayer(BlackJackPlayer player){
        boolean res = players.remove(player);
        if(res) Logger.info("[" + id + "] " + player.getUsername() +" left the room.");
        return res;
    }

    private void tick() throws InterruptedException {
        if(state==1){
            if(players.size()<MIN_PLAYERS) {
                state=0;
                players.forEach(player -> {
                    player.getConnection().sendPacket(new ClientboundLobbyUpdate(state==1, time, players.size()));
                });
                time=0;
                return;
            }
            time--;
            if(time==0){
                state=2;
                players.forEach(player -> {
                    player.allowTransactions(false);
                    player.getConnection().sendPacket(new ClientboundGameUpdate(0, null));
                });
                Thread.sleep(10000);
                players.forEach(player -> {
                    player.getConnection().sendPacket(new ClientboundGameUpdate(1, null));
                    if(!player.isLastTransactionFromClient() || player.getLastTransaction()>=0){
                        int fiches = player.makeFichesTransaction(2000, false,false);
                        if(fiches==-1) {
                            player.makeFichesTransaction(player.getFiches(), false, false);
                        }
                    }
                });
                players.forEach(player ->
                        players.forEach(pts ->
                                pts.getConnection().sendPacket(new ClientboundPlayerUpdate(player.getUsername(), player.getFiches(), -1,0))));
                state=3;
                return;
            }
            players.forEach(player -> player.getConnection().sendPacket(new ClientboundLobbyUpdate(state==1, time, players.size())));
        }

    }

    class BlackJackRoomTicker extends Thread{

        @Override
        public void run(){
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!players.isEmpty()){
                try {
                    tick();
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Logger.info("[" + id + "] Room empty. Deleting it");
            Main.getBlackJackServer().getRooms().remove(BlackJackRoom.this);
            interrupt();
        }
    }

    @Override
    public boolean equals(Object o){
        if((!(o instanceof BlackJackRoom r))) return false;
        return id.equalsIgnoreCase(r.id);
    }


}
