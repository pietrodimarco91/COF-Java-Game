package model;

import java.util.*;

/**
 * This class represents a single slot of the Nobility Track. 
 */
public class NobilityCell {

	/**
	* This attribute represents the bonus stored in this Cell.
	*/
	private ArrayList<String> bonus;

	/**
	 * Default constructor
	 */
	public NobilityCell(ArrayList<String> values) {
		bonus=new ArrayList<String>();
		Iterator<String> iterator = values.iterator();
		while (iterator.hasNext()) {
			bonus.add(iterator.next());
		}
	}
	
	/**
	 * This method is invoked when a player is located on this cell: he automatically wins the bonus
	 */
	public ArrayList<String> winBonus() {
		return bonus;
	}

}