package model;

import java.util.*;

import controller.Player;

/**
 * 
 */
public class City {

	/**
	 * Name of the city
	 * 
	 */
	private String name;

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
		this.setName(name);
		this.setColor(color);
		this.setRegion(region);
		this.coordinates = coordinates;
		this.setRewardToken(rewardToken);
		emporiums = new ArrayList<Emporium>();
		kingIsHere = false;
		setVisited(false);
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
			Emporium emporium = new Emporium(owner, name);
			emporiums.add(emporium);
			return true;
		}
	}

	/**
	 * Checks if the specified player already owns an emporium in this city.
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
	 * @return
	 */
	public String winBonus() {
		// TODO implement here
		return "";
	}

	/**
	 * @param owner
	 * @return
	 */
	public int countOthersEmporiums(Player owner) {
		// TODO implement here
		return 0;
	}

	/**
	 * @param owner
	 * @return
	 */
	public boolean checkPresenceOfEmporiums(Player owner) {
		// TODO implement here
		return false;
	}

	/**
	 * @return
	 */
	public void setVisited() {
		// TODO implement here

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public ArrayList<Emporium> getEmporiums() {
		return emporiums;
	}

	public void setEmporiums(ArrayList<Emporium> emporiums) {
		this.emporiums = emporiums;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Point getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Point coordinates) {
		this.coordinates = coordinates;
	}

	public RewardToken getRewardToken() {
		return rewardToken;
	}

	public void setRewardToken(RewardToken rewardToken) {
		this.rewardToken = rewardToken;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}

}