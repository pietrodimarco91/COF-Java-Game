package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Pietro Di Marco on 14/05/16.
 * Enumeration of bonus.
 */
public enum PermitTileBonusType {
	ASSISTANT, VICTORYTRACK, POLITIC, NOBILITYTRACK, NEWMAINACTION, COINS;

	/**
	 *This method is used to fill the Tiles with bonuses, in particular this method returns an array from the Enum in a random way.
	 *@param The parameter bonusNumber indicate the size of this array.
	 *@return the arrayList of bonuses (String)
	 */
	public static ArrayList<String> random(int bonusNumber){
		ArrayList<String> bonuses=new ArrayList<String>();
		String valueToInsert;
		for(int i=0;i<bonusNumber;i++){
			valueToInsert=String.valueOf(values()[new Random().nextInt(values().length)]);
			while (i != 0 && bonusAlreadyExisting(bonuses, valueToInsert)) {
				valueToInsert=String.valueOf(values()[new Random().nextInt(values().length)]);
			}
			bonuses.add(valueToInsert);
		}
		return bonuses;
	}
	
	/**
	 * This method is useful to check whether a specified bonus is already
	 * inside an array or not, in order to avoid duplicates.
	 * 
	 * @param value
	 *            The string to search for inside the bonus array
	 * @return true if the value string already exists, false otherwise.
	 */
	public static boolean bonusAlreadyExisting(ArrayList<String> array, String value) {
		Iterator<String> iterator = array.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().equals(value))
				return true;
		}
		return false;
	}
}