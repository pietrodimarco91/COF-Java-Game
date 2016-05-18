package controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import model.GraphMap;
import model.NobilityTrack;

/**
 * This class represents the thread always running while a match is on-going. It
 * stores the core of the game engine.
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
	 * This attribute represents a reference to the NobilityTrack for this
	 * match.
	 */
	private NobilityTrack nobilityTrack;

	/**
	 * Default constructor
	 */
	public MatchHandler(int id, Date date) {
		this.id = id;
		this.date = date;
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
	public void graphMapSetup(int numberOfPlayers, int linksBetweenCities,int bonusNumber) {
		graphMap = new GraphMap(numberOfPlayers, linksBetweenCities,bonusNumber);
	}

	public void nobilityTrackSetup(int bonusNumber) {
		nobilityTrack = new NobilityTrack(bonusNumber);
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

}