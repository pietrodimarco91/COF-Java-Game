package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import controller.Player;

/**
 * The constructor of the Map initializes the map with the specified parameters.
 * It also allows to make the connections between the cities.
 */
public class Map {
	/**
	 * This attribute stores all the cities (vertex) of the map.
	 */
	private ArrayList<City> map;

	/**
	 * The number of cities is an important parameter, depending on the number
	 * of players.
	 */
	private int numberOfCities;

	/**
	 * This number represents the total number of the permit tiles
	 */
	private int numberOfPermitTiles;

	/**
	 * The regions are stored in this array.
	 */
	private Region[] regions;

	/**
	 * Since the King's Council doesn't belong to any Region, the GraphMap saves
	 * its reference.
	 */
	private Council kingCouncil;

	/**
	 * This attribute represents a reference to the NobilityTrack for the
	 * current match.
	 */
	private NobilityTrack nobilityTrack;

	/**
	 * The Map constructor builds the map with the specified parameters, setting
	 * by default (4 players or less) the number of cities at 15 and the number
	 * of permit tiles at 45. For each new player, the number of cities
	 * increases of 3 and the number of permit tiles increases of the number of
	 * the cities. Each region is instantiated with its council and its permit
	 * tile deck. Each city is instantiated with its name, its color, its region
	 * and a random RewardToken.
	 */
	public Map(int numberOfPlayers, int bonusNumber) {
		map = new ArrayList<City>();
		CouncillorsPool councillorsPool = new CouncillorsPool();
		constantsInitialization(numberOfPlayers);
		regionsInitialization(numberOfPermitTiles);
		kingCouncil = new KingCouncil();
		citiesInitialization(bonusNumber);
		nobilityTrackSetup(bonusNumber);
	}

	/**
	 * This methods sets a connection between the specified cities
	 * 
	 * @param city1
	 *            the first city
	 * @param city2
	 *            the second city
	 */
	public void connectCities(City city1, City city2) {
		city1.addConnectedCity(city2);
		city2.addConnectedCity(city1);
	}

	/**
	 * This method counts the shortest path from the specified "cityFrom" to the
	 * "cityTo" with a BFS visit of the map.
	 * 
	 * @param cityFrom
	 *            the starting point (City) of the shortest path calculation
	 * @param cityTo
	 *            the end point (City) of the shortest path calculation
	 * @return the number of connections that separate the two cities
	 */
	public int countDistance(City cityFrom, City cityTo) {
		Queue<City> grayNodesQueue = new LinkedList<City>();
		City cityToExpand;

		for (City city : map) { // all cities initialized for BFS visit
			city.BFSinitialization();
		}
		cityFrom.BFSsourceVisit();
		grayNodesQueue.add(cityFrom);
		while (!(grayNodesQueue.isEmpty())) {
			cityToExpand = grayNodesQueue.remove();
			for (City connectedCity : cityToExpand.getConnectedCities()) {
				if (connectedCity.getBFScolor().equals("WHITE")) {
					connectedCity.setBFScolor("GRAY");
					connectedCity.setBFSdistance(cityToExpand.getBFSdistance() + 1);
					if (connectedCity == cityTo) { // checks whether the city
													// that has just been
													// discovered is the one I'm
													// looking for
						for (City city : map) { // all cities are set back to
												// their default values
							city.BFSinitialization();
						}
						return connectedCity.getBFSdistance();
					}
					grayNodesQueue.add(connectedCity);
				}
			}
			grayNodesQueue.remove();
			cityToExpand.setBFScolor("BLACK");
		}
		return -1;
	}

	/**
	 * This method returns the city where the king is located
	 * 
	 * @return the city where the king is located
	 */
	public City findKingCity() {
		Iterator<City> iterator = map.iterator();
		City city;
		while (iterator.hasNext()) {
			city = iterator.next();
			if (city.getKingIsHere())
				return city;
		}
		return null;
	}

	/**
	 * This method is invoked when a player performs the Quick Move
	 * "Build emporium with king's help" and decides to move the king to another
	 * city.
	 * 
	 * @param cityDestination
	 *            The city where the king must me moved to
	 */
	public void moveKing(City cityDestination) {
		Iterator<City> iterator = map.iterator();
		boolean stop = false;
		City cityTo;
		City cityFrom = findKingCity();
		cityFrom.setKingIsHere(false);
		while (iterator.hasNext() && !stop) {
			cityTo = iterator.next();
			if (cityTo == cityDestination) {
				stop = true;
				cityTo.setKingIsHere(true);
			}
		}
	}

	/**
	 * This method returns the list of cities connected to the specified city
	 * 
	 * @param city
	 *            The specified city
	 * @return the arraylist of cities connected to the specified city. Returns
	 *         null if the specified city doesn't exist.
	 */
	public ArrayList<City> getCitiesConnectedTo(City city) {
		City cityToSearch;
		Iterator<City> iterator = map.iterator();
		while (iterator.hasNext()) {
			cityToSearch = iterator.next();
			if (cityToSearch == city) {
				return cityToSearch.getConnectedCities();
			}
		}
		return null;
	}

	/**
	 * This method states whether the specified player is eligible for the Color
	 * Bonus or not.
	 * 
	 * @param owner
	 *            The specified player
	 * @return true if the player is eligible, false otherwise
	 */
	public boolean isEligibleForColorBonus(Player owner, String color) {
		Iterator<City> mapIterator = map.iterator();
		City tempCity;

		while (mapIterator.hasNext()) {
			tempCity = mapIterator.next();
			if (tempCity.getColor().equals(color)) {
				if (!(tempCity.checkPresenceOfEmporium(owner)))
					return false;
			}
		}
		return true;
	}

	/**
	 * This method sets the king randomly in a City.
	 */
	public void setKingRandomly() {
		Random random = new Random();
		City city = map.get(random.nextInt(map.size()));
		city.setKingIsHere(true);
	}

	/**
	 * Checks whether a City with the specified name already exists or not
	 * 
	 * @param name
	 *            The city name to look for
	 * @return True if there exists already a city with the specified name,
	 *         false otherwise
	 */
	public boolean cityNameAlreadyExisting(String name) {
		Iterator<City> iterator = map.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().getName().equals(name))
				return true;
		}
		return false;
	}

	/**
	 * This method initializes the number of cities and the number of permit
	 * tiles depending on the number of players.
	 * 
	 * @param numberOfPlayers
	 *            the parameter to set the constants.
	 */
	public void constantsInitialization(int numberOfPlayers) {
		numberOfCities = 15;
		numberOfPermitTiles = 45;
		if (numberOfPlayers > 4) {
			numberOfCities += 3 * (numberOfPlayers - 4);
			numberOfPermitTiles += numberOfCities * (numberOfPlayers - 4);
		}
	}

	/**
	 * This method initializes the regions and their permit tiles deck
	 * 
	 * @param numberOfPermitTiles
	 *            the number of permit tiles of each permit tile deck
	 */
	public void regionsInitialization(int numberOfPermitTiles) {
		regions = new Region[RegionName.values().length];
		PermitTileDeck permitTileDeck;
		Council regionCouncil;
		ArrayList<String> regionNames = RegionName.getRegionNames();
		Iterator<String> nameIterator = regionNames.iterator();

		for (int i = 0; i < RegionName.values().length; i++) {
			regionCouncil = new RegionCouncil();
			permitTileDeck = new PermitTileDeck(numberOfPermitTiles);
			regions[i] = new Region(nameIterator.next(), regionCouncil, permitTileDeck);
			regions[i].getDeck().setRegion(regions[i]);
		}
	}

	/**
	 * This method initializes the cities of the map. Notice that the cities
	 * represent the vertexes of the map, and as the constructor of the Map
	 * ends, there won't be any connection between the cities. These should be
	 * set up later. This method also adds the cities to each region equally and
	 * generates the permit tiles of the corresponding permit tile deck.
	 * 
	 * @param bonusNumber
	 *            the number of the bonus that each reward token and permit tile
	 *            should have.
	 */
	public void citiesInitialization(int bonusNumber) {
		City city;
		String name;
		RewardToken rewardToken;
		ArrayList<City> citiesInRegion;
		for (int j = 0; j < RegionName.values().length; j++) {
			citiesInRegion = new ArrayList<City>();
			for (int i = 0; i < numberOfCities / 3; i++) {
				do {
					name = CityNames.random();
				} while (cityNameAlreadyExisting(name));
				rewardToken = new RewardToken(bonusNumber);
				city = new City(name, CityColors.random(), regions[j], rewardToken);
				map.add(city);
				citiesInRegion.add(city);
			}
			regions[j].addCities(citiesInRegion);
			regions[j].getDeck().generatePermitTiles(bonusNumber);
		}
		setKingRandomly();
	}

	/**
	 * This method initializes the NobilityTrack for the current match and makes
	 * it randomly
	 * 
	 * @param bonusNumber
	 *            the number of bonuses inside the cells containing bonuses.
	 */
	public void nobilityTrackSetup(int bonusNumber) {
		nobilityTrack = new NobilityTrack(bonusNumber);
	}

	public String toString() {
		String string = "";
		string += "Map status:\n";
		string += "Cities:\n";
		Iterator<City> iterator = map.iterator();
		while (iterator.hasNext()) {
			string += iterator.next().toString() + "\n";
		}
		string += "Number of Permit Tiles: " + numberOfPermitTiles + "\n";
		string += "Number of Cities: " + numberOfCities + "\n";
		string += "Regions:\n";
		for (int i = 0; i < regions.length; i++) {
			string += regions[i].toString() + "\n";
		}
		string += "King's Council:\n";
		string += kingCouncil.toString() + "\n";
		string += "Councillors Pool: current content of the pool is:\n";
		string += CouncillorsPool.poolStatus() + "\n";
		string += "Nobility Track:\n";
		string += nobilityTrack.toString() + "\n";
		return string;
	}
}