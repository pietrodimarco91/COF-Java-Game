package model;

import java.util.*;

/**
 * This class represents the deck of the Business Permit Tiles of a single region.
 */
public class PermitTileDeck {
	/**
	 * The deck is represented by a Queue.
	 */
	private Queue<Tile> deck;

	/*
	 * The number of tiles in the deck. This is parametric, according to the
	 * number of cities.
	 */
	private int numberOfTiles;

	/**
	 * This attribute represents one of the uncovered Permit Tiles that a player
	 * can choose from when he purchases a Permit Tile.
	 */
	private Tile uncoveredPermitTile1;

	/**
	 * This attribute represents one of the uncovered Permit Tiles that a player
	 * can choose from when he purchases a Permit Tile.
	 */
	private Tile uncoveredPermitTile2;

	/**
	 * Each Region has its own PermitTileDeck
	 */
	private Region region;
	/**
	 * Default constructor
	 */
	public PermitTileDeck(Region region,int numberOfTiles,int bonusNumber) {
		this.region=region;
		this.numberOfTiles = numberOfTiles;
		deck=new LinkedList<Tile>();
		ConcreteTileFactory tileFactory = new ConcreteTileFactory();
		for(int i=0;i<numberOfTiles;i++) {
			deck.add(tileFactory.createPermitTile(region.getCities(),bonusNumber));
		}
		uncoveredPermitTile1=deck.remove();
		uncoveredPermitTile2=deck.remove();
	}

	/**
	 * This method allows a player to perform the correspondent Quick Move.
	 */
	public void switchPermitTiles() {
		deck.add(uncoveredPermitTile1);
		deck.add(uncoveredPermitTile2);
		uncoveredPermitTile1=deck.remove();
		uncoveredPermitTile2=deck.remove();
	}

	/**
	 * This method allows a player to pick up one of the uncovered Permit Tiles.
	 * @param slot The number of the slot where the uncovered tiles are placed. This number should be 1 or 2.
	 * @return The permit tile associated to the chosen slot
	 * @throws InvalidSlotException if the specified slot is different from 1 or 2.
	 */
	public Tile drawPermitTile(int slot) throws InvalidSlotException {
		switch (slot) {
		case 1: {
			Tile tempPermitTile = uncoveredPermitTile1;
			uncoveredPermitTile1=deck.remove();
			return tempPermitTile;
		}
			
		case 2: {
			Tile tempPermitTile = uncoveredPermitTile1;
			uncoveredPermitTile2=deck.remove();
			return tempPermitTile;
		}
		default:
			throw new InvalidSlotException();
		}
	}

}