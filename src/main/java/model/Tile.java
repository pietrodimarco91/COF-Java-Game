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
	public ArrayList<String> bonus;

	public String toString() {
		String string = "Bonuses: ";
		Iterator<String> iterator = bonus.iterator();
		while (iterator.hasNext()) {
			string += iterator.next() + "-";
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
