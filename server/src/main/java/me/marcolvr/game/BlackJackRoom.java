package me.marcolvr.game;

import lombok.Getter;
import me.marcolvr.BlackJackServer;
import me.marcolvr.Main;
import me.marcolvr.game.logic.BlackJackGame;
import me.marcolvr.game.player.BlackJackPlayer;
import me.marcolvr.logger.Logger;
import me.marcolvr.network.packet.clientbound.ClientboundLobbyUpdate;

import java.util.ArrayList;
import java.util.List;

public class BlackJackRoom {

    private final int START_SECONDS = 30;
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

    private void tick(){
        if(state==1){
            if(players.size()<MIN_PLAYERS) {
                state=0;
                return;
            }
            time--;
            if(time==0){
                state=2;
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
                tick();
                try {
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
