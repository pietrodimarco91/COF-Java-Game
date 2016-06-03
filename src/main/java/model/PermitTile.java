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
	public PermitTile(int id,List<City> cities, int bonusNumber) {
		this.id=id;
		this.cities = new ArrayList<>();
		for (int i = new Random().nextInt(3); i > 0; i--) {
			this.cities.add(cities.get(new Random().nextInt(cities.size())));
		}
		bonus = PermitTileBonusType.random(bonusNumber);
	}
	
	public List<City> getCities(){
		return this.cities;
	}
	
	@Override
	public String toString() {
		String string="";
		string+="Permit Tile ID: "+this.id+"\n";
		string+=super.toString();
		return string;
	}
}