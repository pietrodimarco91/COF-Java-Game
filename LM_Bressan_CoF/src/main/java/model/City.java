package model;

import java.util.*;

import controller.Player;

/**
 * This class represents a City in the map. It is also stored as a vertex inside the ArrayList of vertexes of the GraphMap. 
 */
public class City {

	/**
	 * Name of the city
	 * 
	 */
	private String cityName;

	/**
	 * Color of the city
	 * 
	 */
	private String color;

	/**
	 * Each city stores a list of Emporiums that have been built in it.
	 * Initially, this list is empty.
	 * 
	 */
	private ArrayList<Emporium> emporiums;

	/**
	 * Region where the city is located (hills, mountains or coast).
	 * 
	 */
	private Region region;

	/**
	 * Each city has a precise coordinate location represented by Point class,
	 * with (x,y) coordinates.
	 * 
	 */
	private Point coordinates;

	/**
	 * The RewardToken bonus is the bonus assigned to each city at the beginning
	 * of the match.
	 * 
	 */
	private RewardToken rewardToken;

	/**
	 * It states whether the king is located in this city or not.
	 * 
	 */
	private boolean kingIsHere;

	/**
	 * It states whether this city has been visited during a visit of the map or
	 * not.
	 * 
	 */
	private boolean visited;

	/**
	 * Instantiates a City with its main attributes. By default, King is not
	 * meant to be in this city.
	 * 
	 * @param name
	 *            The city name
	 * @param color
	 *            The city color
	 * @param region
	 *            The region in which the city is located
	 * @param coordinates
	 *            The precise (x,y) coordinates of the city, represented by a
	 *            Point
	 * @param rewardToken
	 *            The bonus assigned to this city
	 */
	public City(String name, String color, Region region, Point coordinates, RewardToken rewardToken) {
		this.cityName=name;
		this.color=color;
		this.region=region;
		this.coordinates = coordinates;
		this.rewardToken=rewardToken;
		emporiums = new ArrayList<Emporium>();
		kingIsHere = false;
		setVisited();
	}

	/**
	 * This method allows to state whether the king is located in this city or
	 * not.
	 * 
	 * @return The boolean value of kingIsHere
	 */
	public boolean getKingIsHere() {
		return kingIsHere;
	}

	/**
	 * Returns the region of the city.
	 * 
	 * @return The Region of the city
	 */
	public Region getRegion() {
		return region;
	}

	/**
	 * Returns the color of the city.
	 * 
	 * @return The Color of the city
	 */
	public String getColor() {
		return color;
	}

	/**
	 * Allows to set the king in this city
	 * 
	 * @param value
	 *            kingIsHere will be set at the specified value (true if the
	 *            king is here, false otherwise)
	 */
	public void setKingIsHere(boolean value) {
		kingIsHere = value;
	}

	/**
	 * Builds an emporium in this city, which is owned by the specified Player
	 * parameter.
	 * 
	 * @param owner
	 *            The player who owns the emporium that must be built
	 * @return False if the player already owns an emporium in this city (it is
	 *         forbidden to own more than one emporium in the same city), True
	 *         otherwise and the emporium is correctly built.
	 */
	public boolean buildEmporium(Player owner) {
		if (checkPresenceOfEmporium(owner)) {
			return false;
		} else {
			Emporium emporium = new Emporium(owner, cityName);
			emporiums.add(emporium);
			return true;
		}
	}

	/**
	 * Checks if the specified player already owns an emporium in this city.
	 * 
	 * @param owner
	 *            The owner of the emporiums tha
	 * @return True if the player already has an emporium in this city, false
	 *         otherwise.
	 */
	public boolean checkPresenceOfEmporium(Player owner) {
		boolean stop = false;
		Emporium emporium;
		Iterator<Emporium> iterator = emporiums.iterator();
		while (iterator.hasNext() && !stop) {
			emporium = iterator.next();
			if (emporium.getOwner() == owner)
				stop = true;
		}
		return stop;
	}

	/**
	 * This method is invoked when a player builds an emporium in this city. If
	 * buildEmporium returns true, then the player is allowed to win the
	 * RewardToken of the city.
	 * 
	 * @return The RewardToken assigned to this city
	 */
	public RewardToken winBonus() {
		return rewardToken;
	}

	/**
	 * This method is invoked before a player builds an emporium in this city.
	 * He has to check how many emporiums are already built, as this number
	 * represents how many assistants he has to pay to build his emporium.
	 * 
	 * @param owner
	 *            The owner of the emporium that should be built.
	 * @return The number of the emporiums owned by the other players.
	 */
	public int countOthersEmporiums(Player owner) {
		Iterator<Emporium> iterator = emporiums.iterator();
		Emporium emporium;
		int counter=0;
		while (iterator.hasNext()) {
			emporium = iterator.next();
			if(emporium.getOwner()!=owner)
				counter++;
		}
		return counter;
	}


	/**
	 * This method is invoked during a visit of the map, in order to keep track of cities already visited it sets true to the "visited" attribute when a city gets visited.
	 * @return
	 */
	public void setVisited() {
		visited=false;
	}

	/**
	 * Returns the name of the city.
	 * @return The name of the city
	 */
	public String getName() {
		return cityName;
	}

/**
 * This method returns the ArrayList of the emporiums that are built in this city
 * @return The list of emporiums already built in this city
 */
	public ArrayList<Emporium> getEmporiums() {
		return emporiums;
	}

/**
 * This method returns the coordinates (x,y) of the city
 * @return the coordinates (x,y) of the city represented by a Point class
 */
	public Point getCoordinates() {
		return coordinates;
	}


	public boolean hasBeenVisited() {
		return visited;
	}

}