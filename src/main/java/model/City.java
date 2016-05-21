package model;

import java.util.*;

import controller.Player;

/**
 * This class represents a City in the map. It is also stored as a vertex inside
 * the ArrayList of vertexes of the GraphMap.
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
	 * This attribute is used during the BFS visit of the map: it states whether
	 * a city is not discovered yet (WHITE), it has just been discovered (GRAY)
	 * or it has been visited (BLACK).
	 */
	private String BFScolor;

	/**
	 * This attribute is used during the BFS visit: it counts the distance from
	 * the source city.
	 */
	private int BFSdistance;

	/**
	 * Since a City is a vertex of the GraphMap, each City stores its connected
	 * cities inside an ArrayList of Cities.
	 */
	private ArrayList<City> connectedCities;

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
	public City(String name, String color, Region region, RewardToken rewardToken) {
		this.cityName = name;
		this.color = color;
		this.region = region;
		this.rewardToken = rewardToken;
		emporiums = new ArrayList<Emporium>();
		kingIsHere = false;
	}

	/**
	 * Returns the name of the city.
	 * 
	 * @return The name of the city
	 */
	public String getName() {
		return cityName;
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
	 * @return The list of cities connected to this city
	 */
	public ArrayList<City> getConnectedCities() {
		return connectedCities;
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
		int counter = 0;
		while (iterator.hasNext()) {
			emporium = iterator.next();
			if (emporium.getOwner() != owner)
				counter++;
		}
		return counter;
	}

	/**
	 * This method returns the ArrayList of the emporiums that are built in this
	 * city
	 * 
	 * @return The list of emporiums already built in this city
	 */
	public ArrayList<Emporium> getEmporiums() {
		return emporiums;
	}

	/**
	 * This method returns the coordinates (x,y) of the city
	 * 
	 * @return the coordinates (x,y) of the city represented by a Point class
	 */
	public Point getCoordinates() {
		return coordinates;
	}

	/**
	 * Sets a connection in the GraphMap between this city and the specified
	 * city, by adding it to the list of connected cities.
	 * 
	 * @param city
	 *            the city to be connected to this city
	 */
	public void addConnectedCity(City city) {
		connectedCities.add(city);
	}

	/**
	 * Sets the coordinates of the city to the specified point2D
	 * 
	 * @param point
	 *            the point2D corresponding to the coordinates of the city.
	 */
	public void setCoordinates(Point point) {
		this.coordinates = point;
	}

	/**
	 * THIS METHOD MUST BE COMPLETED! Some parts lack.
	 */
	public String toString() {
		String string = "";
		string += "City Name: " + this.cityName + "\n";
		string += "City Color: " + this.color + "\n";
		Iterator<Emporium> emporiumIterator = emporiums.iterator();
		while (emporiumIterator.hasNext())
			string += emporiumIterator.next().toString() + "\n";
		// string += "City Region: " + this.region.toString() + "\n";
		// string += "Coordinates: " + this.coordinates.toString() + "\n";
		string += "RewardToken: " + this.rewardToken.toString() + "\n";
		string += "King is here? " + String.valueOf(kingIsHere) + "\n";
		/*
		 * Iterator<City> cityIterator = connectedCities.iterator(); string +=
		 * "Connected cities:\n"; while (cityIterator.hasNext()) {
		 * if(!(cityIterator.next().equals(connectedCities.get(connectedCities.
		 * size()-1)))) string += cityIterator.next().getName() + " -> "; else
		 * string += cityIterator.next().getName(); }
		 */
		return string;
	}

	/**
	 * This method is invoked when a BFS (Breadth First Search) visit starts; it
	 * initializes the necessary attributes to their default values;
	 */
	public void BFSinitialization() {
		this.BFScolor = "WHITE";
		this.BFSdistance = -1;
	}

	/**
	 * This method is invoked when the source of the BFS visit must be visited.
	 */
	public void BFSsourceVisit() {
		this.BFScolor = "GRAY";
		this.BFSdistance = 0;
	}

	/**
	 * @return The BFS Color of this city, during a BFS visit.
	 */
	public String getBFScolor() {
		return BFScolor;
	}

	/**
	 * @return During a BFS visit, returns the distance from this city to the
	 *         source city.
	 */
	public int getBFSdistance() {
		return this.BFSdistance;
	}

	/**
	 * During a BFS visit, sets the BFS color of the city to the specified
	 * value.
	 * 
	 * @param color
	 *            the specified BFS color value
	 */
	public void setBFScolor(String color) {
		this.BFScolor = color;
	}

	/**
	 * During a BFS visit, this method increments the distance from the source
	 * city.
	 * 
	 * @param val
	 *            the distance from the source city
	 */
	public void setBFSdistance(int val) {
		this.BFSdistance = val;
	}
}