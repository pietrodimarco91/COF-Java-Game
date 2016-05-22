package controller;

import model.Map;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Gabriele on 22/05/16. This class represents the thread always
 * running while a match is on-going. It stores the core of the game engine.
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
	private Map map;

	/**
	 * An ArrayList of player in this MatchHandler.
	 */
	private ArrayList<Player> player;

	/**
	 * Default constructor
	 */
	public MatchHandler(int id, Date date, Connector connector) {
		this.id = id;
		this.date = date;
	}

	/**
	 * Default constructor
	 */
	public void run() {

	}

	/**
	 * This method is invoked to setup the map before a match starts. The
	 * parameters are set by the first player that joins the match.
	 * 
	 * @param numberOfPlayers
	 *            the number of players of the match
	 * @param linksBetweenCities
	 *            the number of MAXIMUM connections between the cities; in
	 *            detail, this number represents the maximum number of streets
	 *            that come out from each city (vertex)
	 */
	public void mapSetup(int numberOfPlayers, int linksBetweenCities, int bonusNumber) {
		map = new Map(numberOfPlayers, bonusNumber,linksBetweenCities);
	}

	public String toString() {
		String string = "";
		string += "Match numero " + this.id + "\nLanciato in data: ";
		DateFormat dateFormat = new SimpleDateFormat();
		string += dateFormat.format(date) + "\n";

		// Needs more implementation: the current status of the match should be
		// displayed
		return string;
	}

	public boolean isPending() {
		return false;
	}

	public void addPlayer(Connector userId) {
	}

	public boolean isNotFull() {
		return false;
	}
}