package model;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Pietro Di Marco on 14/05/16. This class is used to create a permit
 * Tile.
 */
public class PermitTile extends Tile {

	/**
	 * Cities in which the tile allows the construction of emporiums.
	 */
	private ArrayList<City> cities;

	/**
	 * Constructor method that is used in the same time to fill the PermitTiles
	 * with bonuses.
	 */
	public PermitTile(ArrayList<City> cities, int bonusNumber) {
		this.cities = new ArrayList<City>();
		for (int i = new Random().nextInt(3); i > 0; i--) {
			this.cities.add(cities.get(new Random().nextInt(cities.size())));
		}
		bonus = PermitTileBonusType.random(bonusNumber);
	}

}