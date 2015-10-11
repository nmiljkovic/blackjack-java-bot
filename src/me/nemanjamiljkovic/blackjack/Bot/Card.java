package me.nemanjamiljkovic.blackjack.Bot;

public class Card {
    public static final int RANK_ACE = 1;
    public static final int RANK_JACK = 12;
    public static final int RANK_QUEEN = 13;
    public static final int RANK_KING = 14;
    private int rank;
    private Suit suit;

    public Card(int rank, Suit suit) {
        if (rank == 11) {
            rank = 1;
        }

        if (rank < 1 || rank > 14) {
            throw new IllegalArgumentException("Rank must be in the range of 1-14");
        }

        this.rank = rank;
        this.suit = suit;
    }

    public int getRank() {
        return this.rank;
    }

    public Suit getSuit() {
        return this.suit;
    }

    public int getValue() {
        if (this.getRank() >= 10) {
            return 10;
        }

        return this.getRank();
    }

    public enum Suit {
        HEARTS, CLUBS, SPADES, DIAMONDS
    }
}
