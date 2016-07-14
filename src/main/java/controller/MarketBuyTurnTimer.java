package controller;

import server.view.cli.ServerOutputPrinter;
/**
 * 
 *Thread class used to handle the market buy turn timer
 *
 */
public class MarketBuyTurnTimer implements Runnable {
	/**
	 * Static final variable used to set the time 
	 */
	private static final int WAITING_TIME=30000;
	/**
	 * Player to attribute the timer
	 */
	private Player player;
	
	/**
	 * reference to MatchHandler
	 */
	private MatchHandler match;
	/**
	 * 
	 * Default constructor
	 */
	public MarketBuyTurnTimer(Player player,MatchHandler match) {
		this.player=player;
		this.match=match;
	}
	@Override
	public void run() {
		try {
			Thread.sleep(WAITING_TIME);
			match.nextMarketBuyTurn(player);
		} catch (InterruptedException e) {
			ServerOutputPrinter.printLine(e.getMessage());
			Thread.currentThread().interrupt();
		}
	}

}
