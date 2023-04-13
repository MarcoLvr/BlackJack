package me.marcolvr.server.game;

import lombok.Getter;
import me.marcolvr.network.packet.clientbound.ClientboundGameEnd;
import me.marcolvr.network.packet.clientbound.ClientboundGameUpdate;
import me.marcolvr.network.packet.clientbound.ClientboundPlayerUpdate;
import me.marcolvr.server.Main;
import me.marcolvr.server.game.logic.BlackJackDealer;
import me.marcolvr.server.game.logic.BlackJackGame;
import me.marcolvr.server.game.logic.card.BlackJackCard;
import me.marcolvr.server.game.player.BlackJackPlayer;
import me.marcolvr.logger.Logger;
import me.marcolvr.network.packet.clientbound.ClientboundLobbyUpdate;
import me.marcolvr.utils.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

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

    private Stack<BlackJackPlayer> rotation;

    private BlackJackPlayer currentRotating;

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
                startRound();
                return;
            }
            players.forEach(player -> player.getConnection().sendPacket(new ClientboundLobbyUpdate(state==1, time, players.size())));
        }

    }

    public void rotationNext(){
        if(rotation.isEmpty()){
            BlackJackDealer dealer = logic.getDealer();
            if(dealer.getCardsValue()<14){
                dealer.addCard(logic.givePlayerRandomCard());
            }
            players.forEach(player -> {
                if(player.getCardsValue()>21) return;
                if(dealer.getCardsValue()>21 || player.getCardsValue()>dealer.getCardsValue()){
                    player.getConnection().sendPacket(new ClientboundGameEnd(2));
                    player.setFiches(player.getFiches()+(Math.abs(player.getLastTransaction())*2));
                    return;
                }
                if(player.getCardsValue()==dealer.getCardsValue()){
                    player.getConnection().sendPacket(new ClientboundGameEnd(1));
                    player.setFiches(player.getFiches()+Math.abs(player.getLastTransaction()));
                    return;
                }
                player.getConnection().sendPacket(new ClientboundGameEnd(2));
                player.setFiches(player.getFiches()-Math.abs(player.getLastTransaction()));
            });
            try {
                Thread.sleep(5000);
                startRound();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        currentRotating = rotation.pop();
        if(!currentRotating.isOnline()){
            players.forEach(pts ->{
                pts.getConnection().sendPacket(new ClientboundPlayerUpdate(currentRotating.getUsername(), -1, 0,0));
            });
            rotationNext();
            return;
        }
        players.forEach(pts ->{
            pts.getConnection().sendPacket(new ClientboundGameUpdate(state, currentRotating.getUsername()));
        });
    }

    public void rotationAction(BlackJackPlayer player, boolean addCard){
        if(!currentRotating.equals(player)) return;
        if(addCard){
            player.getCards().add(logic.givePlayerRandomCard());
        }
        players.forEach(pts ->{
            pts.getConnection().sendPacket(new ClientboundPlayerUpdate(player.getUsername(), player.getFiches(), player.getCardsValue(),player.getCards().size()));
        });
        if(player.getCardsValue()>21){
            player.getConnection().sendPacket(new ClientboundGameEnd(0));
        }else{
            if(addCard){
                players.forEach(pts ->{
                    pts.getConnection().sendPacket(new ClientboundGameUpdate(state, currentRotating.getUsername()));
                });
                rotation.add(rotation.size(), player);
            }
        }
        rotationNext();
    }

    public void startRound() throws InterruptedException {
        logic.init();
        rotation=new Stack<>();
        rotation.addAll(players);

        //
        players.forEach(player -> {
            player.setCards(new ArrayList<>());
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
        Thread.sleep(1);
        for(BlackJackPlayer player : players){
            player.getCards().add(logic.givePlayerRandomCard());
            player.getConnection().sendPacket(new ClientboundPlayerUpdate("Banco", 0, logic.getDealer().getCardsValue(), logic.getDealer().getCards().size()));
            players.forEach(pts ->{
                pts.getConnection().sendPacket(new ClientboundPlayerUpdate(player.getUsername(), player.getFiches(), player.getCardsValue(), player.getCards().size()));
            });
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        Thread.sleep(1);
        state=3;
        rotationNext();
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
