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
	 * Default constructor: this will generate a random track, with bonus with
	 * random positions
	 */
	public NobilityTrack(int bonusNumber) {
		track = new NobilityCell[LENGTH];
		Random random = new Random();
		ArrayList<String> typesOfBonus = bonusToArrayOfString();
		int low = 0, high = typesOfBonus.size() - 1;
		ArrayList<String> bonus = new ArrayList<String>();

		for (int i=0;i<track.length;i++) {
			if (random.nextFloat() > 0.5) {
				for (int j = 0; j < bonusNumber; j++) {
					bonus.add(typesOfBonus.get(random.nextInt(high - low) + low));
				}
				track[i] = new NobilityCell(bonus);
				bonus.clear();
			}
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

}