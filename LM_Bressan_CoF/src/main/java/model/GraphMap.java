package model;

import java.util.ArrayList;
import java.util.HashMap;

import controller.Player;

/**
 * 
 */
public class GraphMap {
	/**
	 * 
	 */
	private ArrayList<City> map;

	/**
	 * Default constructor
	 */
	public GraphMap() {
	}

	/**
	 * @param connections
	 * @return
	 */
	public void connectCities(HashMap<City, City> connections) {
		// TODO implement here

	}

	/**
	 * @param cityFrom
	 * @param cityTo
	 * @return
	 */
	public int countDistance(City cityFrom, City cityTo) {
		// TODO implement here
		return 0;
	}

	/**
	 * @return
	 */
	public City findKingCity() {
		// TODO implement here
		return null;
	}

	/**
	 * @param cityDestination
	 * @return
	 */
	public void moveKing(City cityDestination) {
		// TODO implement here

	}

	/**
	 * @param city
	 * @return
	 */
	public ArrayList<City> getCitiesConnectedTo(City city) {
		// TODO implement here
		return null;
	}

	/**
	 * @return
	 */
	public void setAllNotVisited() {
		// TODO implement here

	}

	/**
	 * @param owner
	 * @return
	 */
	public boolean isEligibleForColorBonus(Player owner) {
		// TODO implement here
		return false;
	}

}