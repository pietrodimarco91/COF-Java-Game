package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import controller.Player;

/**
 * The constructor of the GraphMap initializes the map with the specified
 * parameters. It also allows to make the connections between the cities.
 */
public class GraphMap {
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
	 * The GraphMap constructor builds the map with the specified parameters,
	 * setting by default (4 players or less) the number of cities at 15 and the
	 * number of permit tiles at 45. For each new player, the number of cities
	 * increases of 3 and the number of permit tiles increases of the number of
	 * the cities. Each region is instantiated with its council and its permit
	 * tile deck. Each city is instantiated with its name, its color, its region
	 * and a random RewardToken.
	 */
	public GraphMap(int numberOfPlayers, int linksBetweenCities, int bonusNumber) {
		numberOfCities = 15;
		numberOfPermitTiles = 45;
		if (numberOfPlayers > 4) {
			numberOfCities += 3 * (numberOfPlayers - 4);
			numberOfPermitTiles += numberOfCities * (numberOfPlayers - 4);
		}

		regions = new Region[RegionName.values().length];
		Council regionCouncil;
		kingCouncil = new KingCouncil();
		PermitTileDeck permitTileDeck;
		City city;
		ArrayList<String> regionNames = RegionName.getRegionNames();
		Iterator<String> nameIterator = regionNames.iterator();
		String name;
		RewardToken rewardToken;

		for (int i = 0; i < RegionName.values().length; i++) {
			regionCouncil = new RegionCouncil();
			permitTileDeck = new PermitTileDeck(numberOfPermitTiles);
			regions[i] = new Region(nameIterator.next(), regionCouncil, permitTileDeck);
		}

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

}