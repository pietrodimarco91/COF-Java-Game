package model;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by pietro on 12/05/16.
 */
public abstract class Tile {
	/**
	 * 
	 */
	private ArrayList<String> bonus;
	
	private int points;
	
	/**
	 * This constructor is used by those tiles that contain a list of bonuses
	 * @param bonus the list of bonuses of the tile
	 */
	public Tile(ArrayList<String> bonus) {
		this.bonus=bonus;
		this.points=0; //not effectively needed but initialized anyway
	}
	
	/**
	 * This constructor is used by those tiles that only contain points for the victory track
	 * @param points the number of points to win when a player wins the tile
	 */
	public Tile(int points) {
		this.points=points;
		this.bonus=new ArrayList<String>(); //not effectively needed but initialized anyway
	}

	public String toString() {
		String string = "Bonuses: ";
		Iterator<String> iterator = bonus.iterator();
		while (iterator.hasNext()) {
			string += iterator.next() + " ";
		}
		return string;
	}

	/**
	 * 
	 * @return
	 */
	public ArrayList<String> getBonus() {
		return this.bonus;
	}
	
	public int getPoints()  {
		return this.points;
	}
}
