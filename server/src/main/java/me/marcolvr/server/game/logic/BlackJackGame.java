package me.marcolvr.server.game.logic;

import lombok.Getter;
import me.marcolvr.server.game.logic.card.BlackJackCard;
import me.marcolvr.server.game.logic.card.CardSeed;
import me.marcolvr.server.game.player.BlackJackPlayer;
import me.marcolvr.utils.Pair;

import java.util.*;

@Getter
public class BlackJackGame {
    private Stack<BlackJackCard> mazzo;

    private BlackJackDealer dealer;

    public BlackJackGame(){
        dealer=new BlackJackDealer();
        init();
    }

    public void init(){
        mazzo=new Stack<>();
        Arrays.stream(CardSeed.values()).toList().forEach(value ->{
            for (int i = 1; i <= 13; i++) {
                mazzo.add(new BlackJackCard(value, i));
            }
        });
        shuffle();
        dealer.reset();
        Pair<BlackJackCard, BlackJackCard> dealerCards = givePlayerStartCards();
        dealer.addCard(dealerCards.getFirst());
        dealer.addCard(dealerCards.getSecond());
    }

    public void shuffle(){
        double sel = Math.random();
        if(sel>0.5){
            List<BlackJackCard> copy = new ArrayList<>(mazzo);
            mazzo.clear();
            Random r = new Random();
            while (!copy.isEmpty()){
                mazzo.add(copy.remove(r.nextInt(copy.size())));
            }
        }else {
            for (int i = 0; i < 104; i++) {
                int random = (int)(Math.random()*104.0);
                BlackJackCard copia = mazzo.get(i);
                mazzo.add(random, copia);
                mazzo.remove(0);
            }
        }
    }

    public Pair<BlackJackCard, BlackJackCard> givePlayerStartCards() {
        return new Pair<>(mazzo.pop(),mazzo.pop());
    }

    public BlackJackCard givePlayerRandomCard() {
        return mazzo.pop();
    }






    @Override
    public String toString() {
        return "BlackJackGame{" +
                "mazzo=" + mazzo +
                '}';
    }
}
