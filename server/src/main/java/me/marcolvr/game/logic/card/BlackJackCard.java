package me.marcolvr.game.logic.card;


import lombok.Getter;

@Getter
public class BlackJackCard {
    private CardSeed seed;
    private int value;

    private final String name;

    public BlackJackCard(CardSeed seed, int value){
        StringBuilder generatedName = new StringBuilder();
        switch (value){
            case 1 -> generatedName.append("Asso di ");
            case 11 -> generatedName.append("Jack di ");
            case 12 -> generatedName.append("Regina di ");
            case 13 -> generatedName.append("Re di ");
            default -> {
                generatedName.append(value);
                generatedName.append(" di ");
            }
        }

        switch (seed){
            case RED_DIAMONDS, BLACK_DIAMONDS -> generatedName.append("Quadri");
            case RED_CLUBS, BLACK_CLUBS -> generatedName.append("Fiori");
            case RED_HEARTS, BLACK_HEARTS ->  generatedName.append("Cuori");
            case RED_SPADES, BLACK_SPADES ->  generatedName.append("Picche");
        }
        name=generatedName.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlackJackCard that)) return false;
        return value == that.value && seed == that.seed;
    }

    @Override
    public String toString() {
        return name;
    }
}
