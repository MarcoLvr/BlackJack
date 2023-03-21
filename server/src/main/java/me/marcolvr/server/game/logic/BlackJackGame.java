package me.marcolvr.server.game.logic;

import me.marcolvr.server.game.logic.card.BlackJackCard;
import me.marcolvr.server.game.logic.card.CardSeed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class BlackJackGame {
    private List<BlackJackCard> mazzo;

    public BlackJackGame(){
        mazzo=new ArrayList<>();
        Arrays.stream(CardSeed.values()).toList().forEach(value ->{
            for (int i = 1; i <= 13; i++) {
                mazzo.add(new BlackJackCard(value, i));
            }
        });
        shuffle();
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

    @Override
    public String toString() {
        return "BlackJackGame{" +
                "mazzo=" + mazzo +
                '}';
    }
}
