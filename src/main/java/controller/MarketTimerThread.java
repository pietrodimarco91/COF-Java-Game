package controller;

import server.view.cli.ServerOutputPrinter;

public class MarketTimerThread implements Runnable {

	private static final int waitingTime=60000;
	private MatchHandler match;

	public MarketTimerThread(MatchHandler match) {
		this.match=match;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(waitingTime);
			match.startMarketBuyTime();
			match.setGameStatus(5);
			Thread.sleep(waitingTime);
			match.rewindTurns();
			match.setGameStatus(3);
		} catch (InterruptedException e) {
			ServerOutputPrinter.printLine(e.getMessage());
		}
	}

	
}
