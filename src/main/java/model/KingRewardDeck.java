package model;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Pietro Di Marco on 14/05/16. Enumeration of bonus.
 */
public class KingRewardDeck {

	/**
	 * The KingReward pile.
	 */
	private Queue<Tile> deck;

	/**
	 * This constructor create the deck with 5 KingRewardTile.
	 */
	public KingRewardDeck() {
		deck = new LinkedList<Tile>();
		TileFactory factory = new ConcreteTileFactory();
		deck.add(factory.createKingRewardTile(35));
		deck.add(factory.createKingRewardTile(30));
		deck.add(factory.createKingRewardTile(25));
		deck.add(factory.createKingRewardTile(20));
		deck.add(factory.createKingRewardTile(15));
	}

	/**
	 * @return the head of the queue, it's used when a player wins a
	 *         KingRewardTile.
	 * @throws NoMoreBonusException if the queue of king reward tile is already empty
	 */
	public Tile getKingReward() throws NoMoreBonusException {
		if (!deck.isEmpty())
			return deck.poll();
		else
			throw new NoMoreBonusException("KING REWARD");
	}
	
	public String toString() {
		String string="KING REWARD DECK\n";
		for(Tile tile : deck) {
			string+=tile.toString()+"\n";
		}
		return string;
	}

}