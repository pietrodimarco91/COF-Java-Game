package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import controller.Player;

/**
 * 
 */
public class GraphMap {
	/**
	 * This attribute stores all the cities (vertex) of the map.
	 */
	private ArrayList<City> map;
	
	/**
	 * The number of cities is an important parameter, depending on the number of players.
	 */
	private int numberOfCities;

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
	 * Default constructor. THIS IS JUST AN EXAMPLE, NEEDS REVISION!
	 */
	public GraphMap(int numberOfPlayers,int linksBetweenCities,int bonusNumber) {
		if(numberOfPlayers<=4) {
			numberOfCities=15;
		}
		else {
			numberOfCities=15+3*numberOfPlayers;
		}
		
		regions=new Region[RegionName.values().length];
		Council regionCouncil;
		kingCouncil = new KingCouncil();
		PermitTileDeck permitTileDeck;
		City city;
		ArrayList<String> regionNames = RegionName.getRegionNames();
		Iterator<String> nameIterator = regionNames.iterator();
		String name;
		RewardToken rewardToken;
		
		for(int i=0;i<RegionName.values().length;i++) {
			regionCouncil = new RegionCouncil();
			permitTileDeck = new PermitTileDeck(45, bonusNumber);
			regions[i]=new Region(nameIterator.next(), regionCouncil, map, permitTileDeck);
		}
		
		int i=0;
		while(i<numberOfCities) {
			for(int j=0;j<RegionName.values().length;j++,i++) {
			do {
				name=CityNames.random();
			} while(cityNameAlreadyExisting(name));
			rewardToken=new RewardToken(bonusNumber);
			city=new City(name, CityColors.random(),regions[j], rewardToken);
			map.add(city);
			}
		}
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