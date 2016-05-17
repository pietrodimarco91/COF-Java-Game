package controller;

import java.util.*;

import model.GraphMap;

/**
 * 
 */
public class MatchHandler extends Thread {

	/**
	 * The ID of the match: IDs are assigned in a crescent way, starting from 0.
	 */
	private int id;

	/**
	 * The date of the launch of the match
	 */
	private Date date;
	
	/**
	 * A reference to the local GraphMap for this match.
	 */
	private GraphMap graphMap;
	/**
	 * Default constructor
	 */
	public MatchHandler(int id,Date date) {
		this.id=id;
		this.date=date;		
	}

	/**
	 * This method is invoked to setup the map before a match starts.
	 * @param numberOfPlayers the number of players of the match
	 * @return
	 */
	public void graphMapSetup(int numberOfPlayers) {

		graphMap = new GraphMap(numberOfPlayers);
	}

}