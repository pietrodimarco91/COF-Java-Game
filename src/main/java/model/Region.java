package model;

import java.io.Serializable;
import java.util.*;

import controller.Player;
import exceptions.CouncillorNotFoundException;
import exceptions.NoMoreBonusException;

/**
 * Created by Gabriele Bressan on 13/05/16.
 * 
 * Region class used on the board class
 */
public class Region implements Serializable {
	/**
	 * Name of the region
	 */
	private String name;

	/**
	 * Council of the region
	 */
	private Council council;

	/**
	 * ArrayList of cities that are in region
	 */
	private ArrayList<City> cities;

	/**
	 * Permit Tile Deck of the region
	 */
	private PermitTileDeck deck;

	/**
	 * 
	 */
	private Tile regionBonus;

	private CouncillorsPool pool;

	/**
	 * Instantiates a Region with its main attributes.
	 * 
	 * @param name
	 *            the region name
	 * 
	 * @param council
	 *            the region council
	 * 
	 * @param permitTileDeck
	 *            the permit tile deck of this region
	 * 
	 */
	public Region(String name, Council council, PermitTileDeck deck, CouncillorsPool pool) {
		this.name = name;
		this.pool=pool;
		this.council = council;
		this.deck = deck;
		TileFactory tileFactory = new ConcreteTileFactory();
		this.regionBonus = tileFactory.createRegionBonusTile(5 + new Random().nextInt(5));

	}

	/**
	 * This method allows to perform the Main Move "Elect a councillor"
	 * 
	 * @param color
	 *            the color of the councillor to elect
	 * 
	 */
	public void electCouncillor(String color) throws CouncillorNotFoundException {
		if (pool.checkPresenceOfCouncillor(color)) {
			this.council.removeCouncillor();
			this.council.addCouncillor(color);
		} else
			throw new CouncillorNotFoundException();
	}

	public PermitTileDeck getDeck() {
		return this.deck;
	}

	/**
	 * @param owner
	 * @return region bonus tile if player is eligible for region bonus
	 * 
	 */
	public Tile winRegionBonus(Player owner) throws NoMoreBonusException {
		if (this.regionBonus == null)
			throw new NoMoreBonusException("REGION BONUS");
		Tile wonTile = regionBonus;
		regionBonus = null;
		return wonTile;
	}

	/**
	 * @param owner
	 * @return boolean value that says if one player owns all cities in one
	 *         region
	 */
	public boolean isEligibleForRegionBonus(Player owner) {
		int i;
		City tempCity;
		for (i = 0; i < cities.size(); i++) {
			tempCity = cities.get(i);
			if (!tempCity.checkPresenceOfEmporium(owner))
				return false;
		}
		return true;
	}

	/**
	 * This method adds the specified cities to this region
	 * 
	 * @param cities
	 *            the cities to add to this region
	 * 
	 */
	public void addCities(ArrayList<City> cities) {
		this.cities = cities;
	}

	/**
	 * 
	 * @return ArrayList of cities in region
	 */
	public ArrayList<City> getCities() {
		return cities;
	}

	/**
	 * @return the name of the region
	 */
	public String getName() {
		return this.name;
	}

	public Council getCouncil() {
		return this.council;
	}

	/**
	 * ToString method
	 * 
	 * @return String
	 */
	public String toString() {
		String regionInformation;
		regionInformation = ("This is the region called: " + this.name + "\n");
		regionInformation += ("This region is composed by these councillors: ");
		regionInformation += this.council.toString();
		regionInformation += ("\nIn this region there are these cities: ");

		for (int i = 0; i < this.cities.size(); i++) {
			regionInformation += (cities.get(i).getName() + " ");
		}

		return regionInformation;
	}

}