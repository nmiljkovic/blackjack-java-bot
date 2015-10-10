package me.nemanjamiljkovic.blackjack.Bot;

public interface BotInterface {
	/**
	 * Get the bot name to be used online.
	 * 
	 * @return
	 */
	public String getBotName();

	/**
	 * Bet at the start of the hand.
	 * 
	 * @param minimum
	 *            Minimum allowed bet. If you bet less than this, your bet will
	 *            be ignored.
	 * @param maximum
	 *            Maximum allowed bet. If you bet more than this, your bet will
	 *            be ignored.
	 * @param money
	 *            Available bot funds.
	 * @return
	 */
	public int bet(int minimum, int maximum, int money);

	/**
	 * Analyze the current hand and dealer's face up card, and return an Action.
	 * If you return an invalid action, it will count as standing.
	 * 
	 * @param money
	 *            Available bot funds.
	 * @param hand
	 *            Bot hand.
	 * @param dealerFaceUpCard
	 *            Dealer's face up card.
	 * @return
	 */
	public Action play(int money, Hand hand, Card dealerFaceUpCard);
}
