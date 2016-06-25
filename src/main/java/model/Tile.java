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
	
	public Tile(ArrayList<String> bonus) {
		this.bonus=bonus;
	}
	
	/**
	 * Implicit constructor needed for subclasses
	 */
	public Tile() {}

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
}
