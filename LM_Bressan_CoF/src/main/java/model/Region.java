package model;

import java.util.*;

import controller.Player;

/**
 * 
 */
public class Region {
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
	private RegionBonusTile regionBonus;

	/**
	 * Instantiates a City with its main attributes. By default, King is not
	 * meant to be in this city.
	 * 
	 * @param name
	 *            The region name
	 * @param council
	 *            The region council
	 * @param cities
	 *            The cities located in this region
	 * @param permitTileDeck
	 *            The permit tile deck of this region
	 * @param regionBonus
	 *            The bonus assigned to this region
	 */
	public Region(String name, Council council, ArrayList cities, PermitTileDeck deck,
			RegionBonusTile regionBonusTile) {
		this.name = name;
		this.council = council;
		this.cities = cities;
		this.deck = deck;
		this.regionBonus = regionBonusTile;
	}

	/**
	 * @param councillor
	 * @return void
	 */
	public void electCouncillor(Councillor councillor) {
		this.council.removeCoucillor();
		this.council.addCouncillor();
	}

	/**
	 * @param politicCards
	 * @return true if council is satisfied or false if the council is not
	 *         satisfied
	 */
	public boolean checkCouncilSatisfaction(ArrayList<PoliticCard> politicCards) {
		
		return false;
	}

	/**
	 * @param owner
	 * @return boolean value that says if one player owns all cities in one region
	 */
	public boolean isEligibleForRegionBonus(Player owner) {
		int i;
		City tempCity;
		for(i=0;i<cities.size();i++){
			tempCity=cities.get(i);
			if(tempCity.checkPresenceOfEmporium(owner)==false)
				return false;
			}
		return true;
	}

	/**
	 * @param owner
	 * @return region bonus if player is eligible for region bonus, else return null
	 */
	public RegionBonusTile winRegionBonus(Player owner) {
		if(isEligibleForRegionBonus(owner))
			return this.regionBonus;
		return null;
	}

}