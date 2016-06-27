package controller;

import server.view.cli.ServerOutputPrinter;

public class MarketTimerThread implements Runnable {

	/**
	 * This is the TOTAL time for selling in the market
	 */
	private static final int SELL_WAITING_TIME = 30000;

	/**
	 * This is the TOTAL time for buying in the market, and it is proportional
	 * to the number of players in the match
	 */
	private int BUY_WAITING_TIME;

	private MatchHandler match;

	public MarketTimerThread(MatchHandler match, int numberOfPlayers) {
		this.match = match;
		BUY_WAITING_TIME = 30000 * numberOfPlayers + 1000;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(SELL_WAITING_TIME);
			match.startMarketBuyTime();
			Thread.sleep(BUY_WAITING_TIME);
			match.startTurns();
		} catch (InterruptedException e) {
			ServerOutputPrinter.printLine(e.getMessage());
			Thread.currentThread().interrupt();
		}
	}
}
