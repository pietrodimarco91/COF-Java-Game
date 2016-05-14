package model;

import java.util.*;

/**
 * This class represents the NobilityTrack as a fixed-length array of Nobility
 * Cells.
 */
public class NobilityTrack {
	private static final int LENGTH = 20;
	/**
	 * The track is an array of NobilityCell.
	 */
	private NobilityCell[] track;

	/**
	 * Default constructor: this will generate a random track, with bonus in
	 * random positions
	 */
	public NobilityTrack(int bonusNumber) {
		track = new NobilityCell[LENGTH];
		Random random = new Random();
		ArrayList<String> typesOfBonus = bonusToArrayOfString();
		int low = 0, high = typesOfBonus.size() - 1;
		ArrayList<String> bonus = new ArrayList<String>();
		String valueToInsert;

		for (int i = 0; i < track.length; i++) {
			if (random.nextFloat() > 0.7) { // each Cell has 30% of chance to
											// contain a bonus
				for (int j = 0; j < bonusNumber; j++) {
					valueToInsert = typesOfBonus.get(random.nextInt(high - low) + low);
					while (j != 0 && bonusAlreadyExisting(bonus, valueToInsert)) {
						valueToInsert = typesOfBonus.get(random.nextInt(high - low) + low);
					}
					bonus.add(valueToInsert);
				}
				track[i] = new NobilityCell(bonus);
				bonus.clear();
			} else
				track[i] = new NobilityCell(bonus);
		}
	}

	/*
	 * This method automatically generates an ArrayList<String> that contains
	 * all the values of the enumeration.
	 */
	public ArrayList<String> bonusToArrayOfString() {
		ArrayList<String> stringValues = new ArrayList<String>();
		for (int i = 0; i < NobilityTrackBonusType.values().length; i++) {
			stringValues.add(NobilityTrackBonusType.values()[i].toString());
		}
		return stringValues;
	}

	/**
	 * This method is useful to check whether a specified bonus is already
	 * inside this NobilityCell or not, in order to avoid duplicates.
	 * 
	 * @param value
	 *            The string to search for inside the bonus array
	 * @return true if the value string already exists, false otherwise.
	 */
	public boolean bonusAlreadyExisting(ArrayList<String> array, String value) {
		Iterator<String> iterator = array.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().equals(value))
				return true;
		}
		return false;
	}

	public String toString() {
		String string = "";
		int shift;
		for (int i = 0; i < LENGTH; i++) {
			shift = i + 1;
			string += "Cell number " + shift + ":\n";
			string += track[i].toString();
			string += "\n";
		}
		return string;
	}
}