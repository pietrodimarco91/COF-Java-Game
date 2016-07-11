package controller;

import java.util.Iterator;
import java.util.List;

/**
 * This class represents an iterator for the Players used to manage the turns in the match
 * @author Riccardo
 *
 */
public class PlayerTurnIterator implements Iterator<Player> {

	private int numberOfPlayers;
	
	private int currentPlayerTurn;
	
	private List<Player> players;
	
	public PlayerTurnIterator(List<Player> players) {
		this.currentPlayerTurn=0;
		this.numberOfPlayers=players.size();
		this.players=players;
	}
	
	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public Player next() {
		if(currentPlayerTurn==numberOfPlayers-1) {
			currentPlayerTurn=0;
			return players.get(currentPlayerTurn);
		}
		currentPlayerTurn++;
		return players.get(currentPlayerTurn);
	}
	
	/**
	 * This method is used to check whether the specified player is the last player in the array of players
	 * @param player
	 * @return true if it's the last one, false otherwise
	 */
	public boolean isLastPlayer(Player player) {
		return players.get(players.size()-1)==player;
	}
	
}
