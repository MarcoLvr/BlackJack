package me.marcolvr.game;

import lombok.Getter;
import me.marcolvr.BlackJackServer;
import me.marcolvr.Main;
import me.marcolvr.game.logic.BlackJackGame;
import me.marcolvr.game.player.BlackJackPlayer;
import me.marcolvr.logger.Logger;

import java.util.ArrayList;
import java.util.List;

public class BlackJackRoom {

    @Getter
    private final String id;
    @Getter
    private List<BlackJackPlayer> players;
    @Getter
    private BlackJackGame logic;

    private BlackJackRoomTicker ticker;

    private int state;

    public BlackJackRoom(String id, BlackJackPlayer creator){
        this.id=id;
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
        return true;
    }

    public boolean removePlayer(BlackJackPlayer player){
        boolean res = players.remove(player);
        if(res) Logger.info("[" + id + "] " + player.getUsername() +" left the room.");
        return res;
    }

    private void tick(){

    }

    class BlackJackRoomTicker extends Thread{

        @Override
        public void run(){
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
