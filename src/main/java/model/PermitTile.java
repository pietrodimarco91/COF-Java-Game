package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Pietro Di Marco on 14/05/16. This class is used to create a permit
 * Tile.
 */
public class PermitTile extends Tile {

	/**
	 * Cities in which the tile allows the construction of emporiums.
	 */
	private List<City> cities;

	/**
	 * This is a unique identifier for each permit tile
	 */
	private int id;

	/**
	 * Constructor method that is used in the same time to fill the PermitTiles
	 * with bonuses.
	 */
	public PermitTile(int id, List<City> cities, int bonusNumber) {
		super(PermitTileBonusType.random(bonusNumber));
		this.id = id;
		this.cities = new ArrayList<>();
		generateCities(cities);
	}

	/**
	 * This method is used to randomly generate the cities of this permit tile
	 * 
	 * @param cities
	 *            the cities of the region
	 */
	private void generateCities(List<City> cities) {
		int infLimit = 1;
		int supLimit = 3;
		int numberOfCities = new Random().nextInt(supLimit) + infLimit;
		for (int i = 0; i < numberOfCities; i++) {
			City cityToCheck = cities.get(new Random().nextInt(cities.size()));
			while (cityAlreadyExisting(cityToCheck))
				cityToCheck = cities.get(new Random().nextInt(cities.size()));
			this.cities.add(cityToCheck);
		}
	}

	/**
	 * This method is used to check whether the specified city is already inside
	 * the list of cities of this permit tile or not.
	 * 
	 * @param cityToCheck
	 *            the city to check
	 * @return true if it is already existing, false otherwise
	 */
	private boolean cityAlreadyExisting(City cityToCheck) {
		for (City city : cities)
			if (city.getName().equals(cityToCheck.getName()))
				return true;
		return false;
	}

	public List<City> getCities() {
		return this.cities;
	}

	public int getId() {
		return this.id;
	}

	@Override
	public String toString() {
		String string = "";
		string += "Permit Tile ID: " + this.id + "\n";
		string += super.toString() + "\n";
		string += "Cities: ";
		for (City city : cities)
			string += city.getName() + " ";
		return string;
	}
}