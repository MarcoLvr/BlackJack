package me.marcolvr.server.game.logic;

import me.marcolvr.server.game.logic.card.BlackJackCard;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Getter
public class BlackJackDealer {
    private List<BlackJackCard> carte;

    public BlackJackDealer() {
        carte=new ArrayList<>();
    }


}
