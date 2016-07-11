package controller;

import server.view.cli.ServerOutputPrinter;

public class MarketBuyTurnTimer implements Runnable {

	private static final int WAITING_TIME=30000;
	private Player player;
	private MatchHandler match;
	
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
