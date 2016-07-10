package controller;

import server.view.cli.ServerOutputPrinter;

public class TurnTimerThread implements Runnable {
	
	private static final int waitingTime=180000;
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
