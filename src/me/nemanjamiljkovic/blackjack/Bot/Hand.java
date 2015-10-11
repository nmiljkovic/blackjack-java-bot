package me.nemanjamiljkovic.blackjack.Bot;

import java.util.Collections;
import java.util.List;

public class Hand {
    private int value;
    private List<Card> cards;
    private boolean canSplitHand;

    public Hand(int value, boolean canSplitHand, List<Card> cards) {
        this.value = value;
        this.canSplitHand = canSplitHand;
        this.cards = cards;
    }

    public Hand(List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(this.cards);
    }

    public boolean canSplit() {
        return this.canSplitHand;
    }

    public boolean isBlackjack() {
        return this.cards.size() == 2 && this.is21();
    }

    public boolean is21() {
        return this.getValue() == 21;
    }

    public int getValue() {
        return this.value;
    }
}
