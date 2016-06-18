package controller;

import server.view.cli.ServerOutputPrinter;

public class TurnTimerThread implements Runnable {
	
	private static final int waitingTime=60000;
	private MatchHandler match;
	private int playerId;
	
	public TurnTimerThread(MatchHandler match,int playerId) {
		this.match=match;
		this.playerId=playerId;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(waitingTime);
			notifyEndOfTurn();
		} catch (InterruptedException e) {
			ServerOutputPrinter.printLine(e.getMessage());
		}
	}
	
	public void notifyEndOfTurn() {
		match.notifyEndOfTurn(playerId);
	}
	
}
