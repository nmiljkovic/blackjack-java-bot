package me.nemanjamiljkovic.blackjack.Bot;

import me.nemanjamiljkovic.blackjack.Bot.Card.Suit;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Communicator {
    private Socket socket;
    private BotInterface bot;

    private BufferedReader in;
    private PrintWriter out;

    private Communicator(Socket socket, BotInterface bot) {
        this.socket = socket;
        this.bot = bot;

        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            this.changeName();

            this.run();
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            try {
                in.close();
            } catch (Exception exception) {
            }
        }
    }

    /**
     * Create a communicator which connects to the server via a TCP socket, and
     * runs the provided bot.
     * <p>
     * Blocks until disconnection.
     *
     * @param socket
     * @param bot
     * @return
     */
    public static Communicator createAndRun(Socket socket, BotInterface bot) {
        return new Communicator(socket, bot);
    }

    /**
     * Create a communicator which connects to the server via a TCP socket, and
     * runs the provided bot.
     * <p>
     * Blocks until disconnection.
     */
    private void run() throws IOException {
        while (socket.isConnected()) {
            String stringMessage = in.readLine();
            if (stringMessage == null) {
                return;
            }

            try {
                JSONObject message = new JSONObject(stringMessage);
                this.parseMessage(message);
            } catch (JSONException exception) {
                continue;
            }
        }
    }

    /**
     * Parse the input message.
     * <p>
     * Only 2 messages are handled: request_bet and request_action.
     *
     * @param message
     */
    private void parseMessage(JSONObject message) {
        String alias = message.optString("alias", null);
        JSONObject data = message.optJSONObject("data");
        if (data == null) {
            return;
        }

        switch (alias) {
            case "request_bet":
                this.requestBet(data);
                break;
            case "request_action":
                this.requestAction(data);
                break;
        }
    }

    /**
     * Runs bet request logic and responds to the server with the decision.
     *
     * @param data
     */
    private void requestBet(JSONObject data) {
        int minimum = data.optInt("minimumBet", 0);
        int maximum = data.optInt("maximumBet", 100);
        int money = data.optInt("money", 0);

        int bet = this.bot.bet(minimum, maximum, money);

        JSONObject response = new JSONObject();
        response.put("amount", bet);
        this.sendMessage("bet", response);
    }

    /**
     * Runs hand action login and responds to the server with the decision.
     *
     * @param data
     */
    private void requestAction(JSONObject data) {
        int money = data.optInt("money");
        Hand hand = this.handFromInput(data.getJSONObject("playerHand"));
        Card dealerFaceUpCard = this.cardFromInput(data.getJSONObject("dealerCard"));

        Action action = this.bot.play(money, hand, dealerFaceUpCard);

        JSONObject response = new JSONObject();
        response.put("action", action.toString().toLowerCase());
        this.sendMessage("action", response);
    }

    /**
     * Read the json card object. Expected structure:
     * <p>
     * { "rank": 1, "suit": "S" }
     * <p>
     * The rank should be a number between 1-14, excluding 11. The suit should
     * be one of S (Spades), C (Clubs), H (Hearts), D (Diamonds).
     *
     * @param card
     * @return
     */
    private Card cardFromInput(JSONObject card) {
        if (card == null) {
            return null;
        }

        int rank = card.optInt("rank", 1);
        String suit = card.optString("suit", "S");

        switch (suit) {
            case "S":
                return new Card(rank, Suit.SPADES);
            case "C":
                return new Card(rank, Suit.CLUBS);
            case "H":
                return new Card(rank, Suit.HEARTS);
            case "D":
                return new Card(rank, Suit.DIAMONDS);
            default:
                throw new RuntimeException("Invalid suit received from response");
        }
    }

    /**
     * Read the json hand object. Expected structure:
     * <p>
     * { "value": 21, "canSplitHand": false, "cards": [{ "rank": 1, "suit": "S"
     * }, { "rank": 12, "suit": "S" }] }
     *
     * @param hand
     * @return
     */
    private Hand handFromInput(JSONObject hand) {
        if (hand == null) {
            return null;
        }

        int value = hand.getInt("value");
        boolean canSplitHand = hand.getBoolean("canSplitHand");
        List<Card> cards = new ArrayList<>();

        JSONArray array = hand.getJSONArray("cards");

        for (int i = 0; i < array.length(); i++) {
            JSONObject card = array.getJSONObject(i);
            cards.add(this.cardFromInput(card));
        }

        return new Hand(value, canSplitHand, cards);
    }

    /**
     * Sends a message to the server requesting a name change.
     */
    private void changeName() {
        String newName = this.bot.getBotName();
        JSONObject data = new JSONObject();
        data.put("newName", newName);
        this.sendMessage("set_name", data);
    }

    /**
     * Sends a message to the server in the following format:
     * <p>
     * { "alias": "alias", "data": { ... } }
     *
     * @param alias
     * @param data
     */
    private void sendMessage(String alias, JSONObject data) {
        JSONObject response = new JSONObject();
        response.put("alias", alias);
        response.put("data", data);
        out.println(response.toString());
        out.flush();
    }
}
