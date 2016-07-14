package controller;

import server.view.cli.ServerOutputPrinter;

/**
 * This timer set up the time
 */

public class TurnTimerThread implements Runnable {
	
	private static final int waitingTime=90000;
	private MatchHandler match;
	private Player player;
	
	public TurnTimerThread(MatchHandler match,Player player) {
		this.match=match;
		this.player=player;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(waitingTime);
			match.notifyEndOfTurn(player);
		} catch (InterruptedException e) {
			ServerOutputPrinter.printLine(e.getMessage());
			Thread.currentThread().interrupt();
		}
	}
}
