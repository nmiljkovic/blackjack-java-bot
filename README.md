Blackjack Java Bot
==================

This is a framework for coding Blackjack bots.
It connects to the PHP server from [proof/blackjack-php-server](https://github.io/proof/blackjack-php-server).

One of the servers is hosted on [blackjack.nemanjamiljkovic.me](http://blackjack.nemanjamiljkovic.me).

Example bot
-----------

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

To run the bot:

    package me.nemanjamiljkovic.blackjack;
    
    import java.net.Socket;
    
    import me.nemanjamiljkovic.blackjack.Bot.Communicator;
    import me.nemanjamiljkovic.blackjack.Bot.SimpleBot;
    
    public class Main {
        public static void main(String[] args) {
            try {
                Socket socket = new Socket("blackjack.nemanjamiljkovic.me", 8000);
                
                Communicator.createAndRun(socket, new SimpleBot());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
