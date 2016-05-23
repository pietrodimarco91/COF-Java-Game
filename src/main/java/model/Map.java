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
	private ArrayList<City> cities;

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
	 * This attribute is used only to graphically represent the map with the
	 * regions and the cities distributed in them; the method
	 * generateDefaultConnections() will generate automatically the default
	 * connections between the cities.
	 */
	String[][] matrix;

	/**
	 * This attribute represents the matrix rows.
	 */
	private int MATRIX_ROWS;

	/**
	 * This attribute represents the matrix columns. 10 cells for each region +
	 * 2 columns of '*' to delimit the regions
	 */
	private final int MATRIX_COLUMNS = 32;

	/**
	 * The Map constructor builds the map with the specified parameters, setting
	 * by default (4 players or less) the number of cities at 15 and the number
	 * of permit tiles at 45. For each new player, the number of cities
	 * increases of 3 and the number of permit tiles increases of the number of
	 * the cities. Each region is instantiated with its council and its permit
	 * tile deck. Each city is instantiated with its name, its color, its region
	 * and a random RewardToken. Random connections between cities in the same
	 * region are generated too, and connections between different regions must
	 * be set by the first player. This constructor initializes the Market too.
	 */
	public Map(int numberOfPlayers, int bonusNumber, int linksBetweenCities) {
		cities = new ArrayList<City>();
		CouncillorsPool councillorsPool = new CouncillorsPool();
		constantsInitialization(numberOfPlayers);
		regionsInitialization(numberOfPermitTiles);
		kingCouncil = new KingCouncil();
		citiesInitialization(bonusNumber);
		MATRIX_ROWS = this.numberOfCities / 3;
		generateDefaultConnections(linksBetweenCities);
		nobilityTrackSetup(bonusNumber);
		Market market = new Market();
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
	 * This method removes the connection between the two specified cities
	 * 
	 * @param city1
	 *            the first city
	 * @param city2
	 *            the second city
	 */
	public void unconnectCities(City city1, City city2) {
		city1.removeConnectedCity(city2);
		city2.removeConnectedCity(city1);
	}

	/**
	 * This method checks if the specified cities can be connected, according to
	 * the specified parameter of maximum number of links between the cities.
	 * 
	 * @param city1
	 *            the first city
	 * @param city2
	 *            the second city
	 * @param linksBetweenCities
	 *            the maximum number of links between the cities.
	 * @return true if the new connection is possible, false otherwise.
	 */
	public boolean checkPossibilityOfNewConnection(City city1, City city2, int linksBetweenCities) {
		if(city1.getConnectedCities().contains(city2))
			return false;
		return city1.getConnectedCities().size() < linksBetweenCities
				&& city2.getConnectedCities().size() < linksBetweenCities;
	}

	/**
	 * This method removes randomly some connections between the cities in order
	 * to respect the maximum number of links between the cities.
	 * 
	 * @param linksBetweenCities
	 *            the parameter to respect
	 */
	public void removeRandomConnections(int linksBetweenCities) {
		Random random = new Random();
		for (City city : this.cities) {
			while (city.getConnectedCities().size() > linksBetweenCities) {
				City cityToRemove;
				do {
					cityToRemove = city.getConnectedCities().get(random.nextInt(city.getConnectedCities().size() - 1));
				} while (cityToRemove.getConnectedCities().size() <= 2);
				city.removeConnectedCity(cityToRemove);
				cityToRemove.removeConnectedCity(city);
			}
		}
	}

	/**
	 * This methods checks whether the graph map is fully connected or not by
	 * launching a BFS visit
	 * 
	 * @return true if the graph is connected, false otherwise.
	 */
	public boolean graphIsConnected() {
		Queue<City> grayNodesQueue = new LinkedList<City>();
		City cityToExpand, connectedCity;
		Iterator<City> mapIterator = cities.iterator();
		while (mapIterator.hasNext()) { // all cities initialized for BFS visit
			mapIterator.next().BFSinitialization();
		}
		City cityFrom = cities.get(0);
		cityFrom.BFSsourceVisit();
		grayNodesQueue.add(cityFrom);
		while (!(grayNodesQueue.isEmpty())) {
			cityToExpand = grayNodesQueue.remove();
			Iterator<City> connectedCitiesIterator = cityToExpand.getConnectedCities().iterator();
			while (connectedCitiesIterator.hasNext()) {
				connectedCity = connectedCitiesIterator.next();
				if (connectedCity.getBFScolor().equals("WHITE")) {
					connectedCity.setBFScolor("GRAY");
					connectedCity.setBFSdistance(cityToExpand.getBFSdistance() + 1);
					grayNodesQueue.add(connectedCity);
				}
			}
			cityToExpand.setBFScolor("BLACK");
		}
		for (City city : cities) {
			if (!(city.getBFScolor().equals("BLACK"))) {
				for (City tempCity : cities) {
					tempCity.BFSinitialization();
				}
				return false;
			}
		}
		for (City tempCity : cities) {
			tempCity.BFSinitialization();
		}
		return true;
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
		if (cityFrom == cityTo)
			return 0;
		Queue<City> grayNodesQueue = new LinkedList<City>();
		City cityToExpand;
		int distance = -1;

		for (City city : cities) { // all cities initialized for BFS visit
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
						distance = connectedCity.getBFSdistance();
						for (City city : cities) { // all cities are set back to
													// their default values
							city.BFSinitialization();
						}
						return distance;
					}
					grayNodesQueue.add(connectedCity);
				}
			}
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
		Iterator<City> iterator = cities.iterator();
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
		Iterator<City> iterator = cities.iterator();
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
		Iterator<City> iterator = cities.iterator();
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
		Iterator<City> mapIterator = cities.iterator();
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
		City city = cities.get(random.nextInt(cities.size()));
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
		Iterator<City> iterator = cities.iterator();
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
			numberOfPermitTiles += 12 * (numberOfPlayers - 4);
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
				cities.add(city);
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

	/**
	 * This method creates the default connections between the cities at the
	 * beginning of the match. It uses the matrix to graphically represent the
	 * cities in the map.
	 */
	public void generateDefaultConnections(int linksBetweenCities) {
		initializeMatrix();
		int column;
		int low = 0;
		String direction;

		for (int k = 0; k < regions.length; k++) {
			HashMap<String, City> hashMap = new HashMap<String, City>();
			ArrayList<String> cityNames = new ArrayList<String>();
			hashMap = mapCitiesWithLetters(cityNames, hashMap, k);
			direction = "right";
			City currentCity, fatherCity = null, grandFatherCity = null;
			for (int i = 0; i < MATRIX_ROWS; i++) {
				if (direction.equals("right"))
					column = low + 7;
				else
					column = low + 3;
				String tempCityName = cityNames.remove(0);
				String initialLetter = String.valueOf(tempCityName.charAt(0));
				currentCity = hashMap.get(String.valueOf(tempCityName.charAt(0)));
				if (fatherCity != null) {
					connectCities(currentCity, fatherCity);
					if (grandFatherCity != null) {
						connectCities(currentCity, grandFatherCity);
					}
				}
				matrix[i][column] = initialLetter;
				if (direction.equals("right")) {
					direction = "left";
				} else {
					direction = "right";
				}
				grandFatherCity = fatherCity;
				fatherCity = currentCity;
			}
			low += 11;
		}
		removeRandomConnections(linksBetweenCities);
	}

	/**
	 * This method initializes the matrix for the graphical visualization of the
	 * map and the cities.
	 */
	public void initializeMatrix() {
		matrix = new String[MATRIX_ROWS][MATRIX_COLUMNS];
		for (int i = 0; i < MATRIX_ROWS; i++) {
			for (int j = 0; j < MATRIX_COLUMNS; j++) {
				if (j == 11 || j == 22) {
					matrix[i][j] = "*";
				} else
					matrix[i][j] = " ";
			}
		}
	}

	/**
	 * This method is invoked to create an association between a city and its
	 * initial letter. It is used when the default connections between the
	 * cities are being generated.
	 * 
	 * @param cityNames
	 *            a list with all the names of the cities of the current region
	 *            k in the array of regions[k]
	 * @param hashMap
	 *            the hash map to fill with the keys "initial letters" and the
	 *            values "city"
	 * @param k
	 *            the index of the current region
	 */
	public HashMap<String, City> mapCitiesWithLetters(ArrayList<String> cityNames, HashMap<String, City> hashMap,
			int k) {
		ArrayList<City> cities = regions[k].getCities();
		Iterator<City> iterator = cities.iterator();
		String name;
		while (iterator.hasNext()) { // association of the initial letter
										// with the corresponding city
			City city = iterator.next();
			if (city.getRegion() == this.regions[k]) {
				name = city.getName();
				String initialLetter = String.valueOf(name.charAt(0));
				cityNames.add(initialLetter); // the
												// initial
												// letter of
												// the name
				hashMap.put(initialLetter, city);
			}
		}
		return hashMap;
	}

	/**
	 * This method prints the matrix representing the cities in the map.
	 */
	public void printMatrix() {
		String print = "";
		for (int i = 0; i < MATRIX_ROWS; i++) {
			for (int j = 0; j < MATRIX_COLUMNS; j++) {
				print += matrix[i][j];
			}
			print += "\n";
		}
		System.out.println(print);
	}

	/**
	 * This method is invoked during the print of the matrix, to visualize which
	 * cities each city is connected to.
	 */
	public void printConnections() {
		for (City city : cities) {
			System.out.println("Cities connected to " + city.getName() + ":");
			for (City cityConnected : city.getConnectedCities()) {
				System.out.print(cityConnected.getName() + " ");
			}
			System.out.println("\n");
		}
	}

	/**
	 * This methods prints the distances between all the cities of the map.
	 */
	public void printDistances() {
		for (City city1 : cities) {
			for (City city2 : cities) {
				int distance = countDistance(city1, city2);
				if (distance != -1) {
					System.out.println(
							"Distance between " + city1.getName() + " and " + city2.getName() + " is: " + distance);
				} else
					System.out.println(city1.getName() + " and " + city2.getName() + " are not connected.");
			}
		}
	}

	public String toString() {
		String string = "";
		string += "Map status:\n";
		string += "Cities:\n";
		Iterator<City> iterator = cities.iterator();
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

	public int getNumberOfCities() {
		return this.numberOfCities;
	}

	public int getNumberOfPermitTiles() {
		return this.numberOfPermitTiles;
	}

	public ArrayList<City> getMap() {
		return this.cities;
	}

	public Region[] getRegions() {
		return this.regions;
	}

	public Council getKingCouncil() {
		return this.kingCouncil;
	}

	public NobilityTrack getNobilityTrack() {
		return this.nobilityTrack;
	}
}