package model;

import java.util.*;

/**
 * This class represents the deck of the Business Permit Tiles.
 */
public class PermitTileDeck {
	/**
	 * The deck is represented by a Queue.
	 */
	private Queue<PermitTile> deck;

	/*
	 * The number of tiles in the deck. This is parametric, according to the
	 * number of cities.
	 */
	private int numberOfTiles;

	/**
	 * This attribute represents one of the uncovered Permit Tiles that a player
	 * can choose from when he purchases a Permit Tile.
	 */
	private PermitTile uncoveredPermitTile1;

	/**
	 * This attribute represents one of the uncovered Permit Tiles that a player
	 * can choose from when he purchases a Permit Tile.
	 */
	private PermitTile uncoveredPermitTile2;

	/**
	 * Default constructor
	 */
	public PermitTileDeck(int numberOfTiles) {
		this.numberOfTiles = numberOfTiles;
		uncoveredPermitTile1=new PermitTile();
		uncoveredPermitTile2=new PermitTile();
		/*
		 * We need to instantiate the deck
		 */

	}

	/**
	 * This method allows a player to perform the correspondent Quick Move.
	 */
	public void switchPermitTiles() {
		uncoveredPermitTile1=deck.remove();
		uncoveredPermitTile2=deck.remove();
		deck.add(uncoveredPermitTile1);
		deck.add(uncoveredPermitTile2);
	}

	/**
	 * This method allows a player to pick up one of the uncovered Permit Tiles.
	 * @param slot The number of the slot where the uncovered tiles are placed. This number should be 1 or 2.
	 * @return The permit tile associated to the chosen slot
	 */
	public PermitTile drawPermitTile(int slot) throws InvalidSlotException {
		switch (slot) {
		case 1: {
			PermitTile tempPermitTile = uncoveredPermitTile1;
			uncoveredPermitTile1=deck.remove();
			return tempPermitTile;
		}
			
		case 2: {
			PermitTile tempPermitTile = uncoveredPermitTile1;
			uncoveredPermitTile2=deck.remove();
			return tempPermitTile;
		}
		default:
			throw new InvalidSlotException();
		}
	}

}