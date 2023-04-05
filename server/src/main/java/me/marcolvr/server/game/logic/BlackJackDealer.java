package me.marcolvr.server.game.logic;

import lombok.Getter;
import me.marcolvr.server.game.BlackJackRoom;
import me.marcolvr.server.game.logic.card.BlackJackCard;
import me.marcolvr.server.game.player.BlackJackPlayer;
import me.marcolvr.utils.Pair;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

@Getter
public class BlackJackDealer {
    private List<BlackJackCard> cards;

    public int getCardsValue(){
        int val = 0;
        for (BlackJackCard card : cards) {
            val+=card.getValue();
        }
        return val;
    }

    public BlackJackDealer() {
        reset();
    }

    public void reset(){
        cards=new ArrayList<>();
    }

    public void addCard(BlackJackCard card){
        cards.add(card);

    }

    public void setCards(List<BlackJackCard> cards) {
        this.cards.addAll(cards);
    }



}
