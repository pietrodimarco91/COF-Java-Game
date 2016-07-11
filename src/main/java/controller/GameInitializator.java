package controller;

import model.Board;
import server.view.cli.ServerOutputPrinter;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by pietro on 14/06/16.
 */
public class GameInitializator extends Thread {

	private static final Logger logger = Logger.getLogger(GameInitializator.class.getName());
	private static final int SECONDS_TO_START = 10;
	private MatchHandler match;
	private int minimumNumberOfPlayers;
	private int id;
	private Board board;
	private int[] configParameters;

	public GameInitializator(int id, int[] configParameters, MatchHandler match,
			int minumumNumberOfPlayers) {
		this.id = id;
		this.configParameters = configParameters;
		this.match = match;
		this.minimumNumberOfPlayers = minumumNumberOfPlayers;
	}

	@Override
	public void run() {
		gameInitialization();
	}

	private void gameInitialization() {
		waitingForPlayers();
		countdown();
		setDefinitiveNumberOfPlayers();
		boardInitialization();
		showPlayersInGame();
	}

	private void waitingForPlayers() {
		match.setGameStatus(GameStatusConstants.WAIT_FOR_PLAYERS);
		ServerOutputPrinter.printLine("[MATCH " + id + "] Game Status changed to 'Waiting for players'");
		sendMessageToClient("[MATCH " + id + "] Currently waiting for players...", match.getPlayers().get(0).getId());
		while (match.getPlayers().size() < this.minimumNumberOfPlayers) {
			// Match starts with at least two players
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, "ERROR TRYING TO SLEEP!", e);
				Thread.currentThread().interrupt();
			}
			match.sendListOfPlayers();
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "ERROR TRYING TO SLEEP!", e);
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * NEEDS JAVADOC
	 */
	public void countdown() {
		for (int i = SECONDS_TO_START; i > 0; i--) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, "ERROR TRYING TO SLEEP!", e);
				Thread.currentThread().interrupt();
			}
			for (Player player : match.getPlayers()) {
				try {
					player.getConnector().sendToClient(new Packet("MATCH STARTING IN: " + i + "\n"));
					match.sendListOfPlayers();
				} catch (RemoteException e) {
					logger.log(Level.FINEST, "Error: couldn't write to client\n", e);
				}
			}
		}
		match.setGameStatus(GameStatusConstants.MAP_CONFIG);
	}

	/**
	 * NEEDS JAVADOC
	 */
	public void setDefinitiveNumberOfPlayers() {
		configParameters[0] = match.getPlayers().size();
		match.setNumberOfPlayers(match.getPlayers().size());
	}

	/**
	 * This method is invoked to initialize the board before a match starts. The
	 * parameters are set by the first player that joins the match.
	 */
	public void boardInitialization() {
		board = new Board(configParameters[0], configParameters[1], configParameters[2], configParameters[3],
				configParameters[4]);
		match.setBoard(board);
		PubSub.notifyAllClients(match.getPlayers(), "Board correctly initialized!", board);
		ServerOutputPrinter.printLine("[MATCH " + id + "] Game Status changed to 'Map Configuration'");
	}

	public void showPlayersInGame() {
		String string = "";
		string += "Players in game:\n";
		for (Player player : match.getPlayers()) {
			string += player.getNickName() + " : ID " + player.getId() + "\n";
		}
		PubSub.notifyAllClients(match.getPlayers(), string, board);
		for(Player player:match.getPlayers()){
			match.updateClient(player.getId());
		}
			
		
	}

	public void sendMessageToClient(String s, int playerId) {
		String message = "[SERVER] " + s;
		try {
			match.getPlayers().get(playerId).getConnector().sendToClient(new Packet(message));
		} catch (RemoteException e) {
			ServerOutputPrinter.printLine(e.getMessage());
		}
	}
}
