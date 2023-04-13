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
                mazzo.add(new BlackJackCard(value, Math.min(i, 10)));
            }
        });
        shuffle();
        dealer.reset();
        dealer.addCard(givePlayerRandomCard());
    }

    public void shuffle(){
        Collections.shuffle(mazzo);
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
