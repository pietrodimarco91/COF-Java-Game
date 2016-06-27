package controller;

import server.view.cli.ServerOutputPrinter;

public class MarketBuyTurnTimer implements Runnable {

	private static final int WAITING_TIME=30000;
	private int playerId;
	private MatchHandler match;
	
	public MarketBuyTurnTimer(int playerId,MatchHandler match) {
		this.playerId=playerId;
		this.match=match;
	}
	@Override
	public void run() {
		try {
			Thread.sleep(WAITING_TIME);
			match.nextMarketBuyTurn(playerId);
		} catch (InterruptedException e) {
			ServerOutputPrinter.printLine(e.getMessage());
			Thread.currentThread().interrupt();
		}
	}

}
