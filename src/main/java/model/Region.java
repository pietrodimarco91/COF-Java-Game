package model;

import java.util.*;

import controller.Player;
import exceptions.CouncillorNotFoundException;

/**
 * Created by Gabriele Bressan on 13/05/16.
 */
public class Region{
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

	/**
	 * Instantiates a Region with its main attributes.
	 * 
	 * @param name the region name
	 *            
	 * @param council the region council
	 *            
	 * @param permitTileDeck the permit tile deck of this region
	 *            
	 */
	public Region(String name, Council council, PermitTileDeck deck) {
		this.name = name;
		this.council = council;
		this.deck = deck;
		TileFactory tileFactory = new ConcreteTileFactory();
		this.regionBonus = tileFactory.createRegionBonusTile(5 + new Random().nextInt(5));

	}

	/**
	 * This method allows to perform the Main Move "Elect a councillor"
	 * 
	 * @param color the color of the councillor to elect
	 *           
	 */
	public void electCouncillor(String color) throws CouncillorNotFoundException {
		if (CouncillorsPool.checkPresenceOfCouncillor(color)) {
			this.council.removeCouncillor();
			this.council.addCouncillor(color);
		} else
			throw new CouncillorNotFoundException();
	}

	public PermitTileDeck getDeck() {
		return this.deck;
	}

	/**
	 * @param politicCards of the player
	 * @return number of councillors satisfied
	 */
	public int checkCouncilSatisfaction(ArrayList<PoliticCard> politicCards) {
		Iterator<Councillor> iterationCouncillors = this.council.getCouncillors().iterator();
		Councillor councillor;
		PoliticCard tempPoliticCard;
		int numberOfCouncillorsSatisfied = 0;
		ArrayList<PoliticCard> tempArrayList = new ArrayList<PoliticCard>(politicCards);
		while (iterationCouncillors.hasNext()) {
			boolean councillorsSatisfied = false;
			councillor = iterationCouncillors.next();
			
			for (int i = 0; i < tempArrayList.size() && councillorsSatisfied == false; i++) {
				tempPoliticCard = tempArrayList.get(i);
				if (councillor.getColor() == tempPoliticCard.getColorCard()) {
					councillorsSatisfied = true;
					tempArrayList.remove(i);
					numberOfCouncillorsSatisfied++;
				}

			}

		}
		
		if(numberOfCouncillorsSatisfied<4){
			int numberOfMulticolorCard=0;
			for (int i = 0; i < tempArrayList.size(); i++){
				tempPoliticCard = tempArrayList.get(i);
				if (tempPoliticCard.getColorCard()== "MULTICOLOR")
					numberOfMulticolorCard++;
			}
			if((numberOfCouncillorsSatisfied+numberOfMulticolorCard)<4)
				return numberOfCouncillorsSatisfied+numberOfMulticolorCard;
			else
				return 4;
		}
		
		else
		return numberOfCouncillorsSatisfied;
	}


	/**
	 * @param owner
	 * @return region bonus tile if player is eligible for region bonus, else return null
	 *        
	 */
	public Tile winRegionBonus(Player owner) {
		if (isEligibleForRegionBonus(owner))
			return this.regionBonus;
		return null;
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
	 * This method adds the specified cities to this region
	 * 
	 * @param cities the cities to add to this region
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

	/**
	 * ToString method
	 * 
	 * @return String
	 */
	public String toString() {
		String regionInformation;
		regionInformation = ("This in the region called: " + this.name + "\n");
		regionInformation += ("This region is composed by these councillors: \n");
		regionInformation += this.council.toString();
		regionInformation += ("In this region there are these cities: \n");

		for (int i = 0; i < this.cities.size(); i++) {
			regionInformation += (cities.get(i).getName() + "\n");
		}

		return regionInformation;
	}
}