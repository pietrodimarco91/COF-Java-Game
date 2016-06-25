package model;

import java.util.*;

/**
 * This class represents a single slot of the Nobility Track.
 */
public class NobilityCell {

	/**
	 * This attribute represents the bonus stored in this Cell (Cells without
	 * bonus have an empty ArrayList)
	 */
	private ArrayList<String> bonus;

	/**
	 * Default constructor
	 */
	public NobilityCell(ArrayList<String> values) {
		bonus = new ArrayList<String>();
		Iterator<String> iterator = values.iterator();
		while (iterator.hasNext()) {
			bonus.add(iterator.next());
		}
	}

	/**
	 * This method is invoked when a player is located on this cell: he
	 * automatically wins the bonus
	 */
	public ArrayList<String> winBonus() {
		return bonus;
	}

	public String toString() {
		Iterator<String> iterator = bonus.iterator();
		String string = "";
		if (bonus.size() == 0)
			return "This cell has no bonus\n";
		int i = 0;
		while (iterator.hasNext()) {
			string += "Bonus number " + i + ": ";
			string += iterator.next();
			string += "\n";
			i++;
		}
		return string;
	}
}