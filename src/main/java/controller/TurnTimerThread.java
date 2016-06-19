package controller;

import server.view.cli.ServerOutputPrinter;

public class TurnTimerThread implements Runnable {
	
	private static final int waitingTime=30000;
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
			match.notifyEndOfTurn(playerId);
		} catch (InterruptedException e) {
			ServerOutputPrinter.printLine(e.getMessage());
		}
	}
}
