package me.nemanjamiljkovic.blackjack.Bot;

public class SimpleBot implements BotInterface {

    @Override
    public int bet(int minimum, int maximum, int money) {
        int randomBet = ((int) (Math.random() * (maximum - minimum)) + minimum);

        return Math.min(randomBet, money);
    }

    @Override
    public Action play(int money, Hand hand, Card dealerFaceUpCard) {
        if (hand.getValue() >= 9 && hand.getValue() <= 11) {
            return Action.DOUBLE_DOWN;
        }

        if (hand.canSplit() && hand.getValue() > 8) {
            return Action.SPLIT;
        }

        if (hand.getValue() < 16) {
            return Action.HIT;
        }

        return Action.STAND;
    }

    @Override
    public String getBotName() {
        return "SimpleBot";
    }

}
