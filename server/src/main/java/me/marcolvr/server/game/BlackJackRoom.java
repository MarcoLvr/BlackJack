package me.marcolvr.server.game;

import lombok.Getter;
import me.marcolvr.network.packet.clientbound.*;
import me.marcolvr.server.BlackJackServer;
import me.marcolvr.server.game.logic.BlackJackDealer;
import me.marcolvr.server.game.logic.BlackJackGame;
import me.marcolvr.server.game.player.BlackJackPlayer;
import me.marcolvr.server.logger.LogMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class BlackJackRoom {

    private static final int START_SECONDS = 30;
    private static final int MIN_PLAYERS = 2;

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
        LogMessages.roomCreated(id);
        addPlayer(creator);
        ticker.start();
    }

    public boolean isFull(){
        return players.size()==4;
    }

    public boolean addPlayer(BlackJackPlayer player){
        if(players.contains(player) || isFull() || state>1) {
            LogMessages.playerFailedToJoin(id, player.getUsername());
            return false;
        }
        players.add(player);
        LogMessages.playerJoined(id, player.getUsername());
        if(players.size()==MIN_PLAYERS){
            time=START_SECONDS;
            state=1;
        }
        return true;
    }

    public boolean removePlayer(BlackJackPlayer player){
        boolean res = players.remove(player);
        if(res) LogMessages.playerLeft(id, player.getUsername());
        return res;
    }

    private void tick() throws InterruptedException {
        if(state==1){
            if(players.size()<MIN_PLAYERS) {
                state=0;
                BlackJackServer.getInstance().multicast(ClientboundPacket.lobbyUpdate(false, time, players.size()), this);
                time=0;
                return;
            }
            time--;
            if(time==0){
                state=2;
                startRound();
                return;
            }
            BlackJackServer.getInstance().multicast(ClientboundPacket.lobbyUpdate(true, time, players.size()), this);
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
                    player.getConnection().sendPacket(ClientboundPacket.gameEnd(ClientboundPacket.GameEndState.WIN));
                    player.setFiches(player.getFiches()+(Math.abs(player.getLastTransaction())*2));
                    return;
                }
                if(player.getCardsValue()==dealer.getCardsValue()){
                    player.getConnection().sendPacket(ClientboundPacket.gameEnd(ClientboundPacket.GameEndState.TIE));
                    player.setFiches(player.getFiches()+Math.abs(player.getLastTransaction()));
                    return;
                }
                player.getConnection().sendPacket(ClientboundPacket.gameEnd(ClientboundPacket.GameEndState.LOSE));
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
            BlackJackServer.getInstance().multicast(ClientboundPacket.playerUpdate(currentRotating.getUsername(), -1, 0, 0), this);
            rotationNext();
            return;
        }
        BlackJackServer.getInstance().multicast(ClientboundPacket.gameUpdate(ClientboundPacket.GameUpdateState.PLAYER_ACTION, currentRotating.getUsername()), this);
    }

    public void rotationAction(BlackJackPlayer player, boolean addCard){
        if(!currentRotating.equals(player)) return;
        if(addCard){
            player.getCards().add(logic.givePlayerRandomCard());
        }
        BlackJackServer.getInstance().multicast(ClientboundPacket.playerUpdate(player.getUsername(), player.getFiches(), player.getCardsValue(),player.getCards().size()), this);
        if(player.getCardsValue()>21){
            player.getConnection().sendPacket(ClientboundPacket.gameEnd(ClientboundPacket.GameEndState.LOSE));
        }else{
            if(addCard){
                BlackJackServer.getInstance().multicast(ClientboundPacket.gameUpdate(ClientboundPacket.GameUpdateState.PLAYER_ACTION, currentRotating.getUsername()), this);
                rotation.add(rotation.size(), player);
            }
        }
        rotationNext();
    }

    public void startRound() throws InterruptedException {
        logic.init();
        rotation=new Stack<>();
        rotation.addAll(players);
        players.forEach(player -> {
            player.setCards(new ArrayList<>());
            player.allowTransactions(false);
            player.getConnection().sendPacket(ClientboundPacket.gameUpdate(ClientboundPacket.GameUpdateState.INIT, null));
        });
        Thread.sleep(10000);
        players.forEach(player -> {
            player.getConnection().sendPacket(ClientboundPacket.gameUpdate(ClientboundPacket.GameUpdateState.START, null));
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
            //TODO: implement clientside
            //OLD: new ClientboundPlayerUpdate("Banco", 0, logic.getDealer().getCardsValue(), logic.getDealer().getCards().size())
            BlackJackServer.getInstance().multicast(ClientboundPacket.playerUpdate("Banco", 0, logic.getDealer().getCardsValue(), logic.getDealer().getCards().size()), this);
            // player.getConnection().sendPacket(ClientboundPacket.dealerUpdate(logic.getDealer().getCardsValue(), logic.getDealer().getCards().size(), logic.getDealer().lastCardValue()));
            BlackJackServer.getInstance().multicast(ClientboundPacket.playerUpdate(player.getUsername(), player.getFiches(), player.getCardsValue(), player.getCards().size()), this);
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
            LogMessages.roomEmpty(id);
            BlackJackServer.getInstance().getRooms().remove(BlackJackRoom.this);
            interrupt();
        }
    }

    @Override
    public boolean equals(Object o){
        if((!(o instanceof BlackJackRoom r))) return false;
        return id.equalsIgnoreCase(r.id);
    }


}
