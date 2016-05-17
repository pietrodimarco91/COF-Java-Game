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
		ArrayList<String> bonus = new ArrayList<String>();

		for (int i = 0; i < track.length; i++) {
			if (random.nextFloat() > 0.7) { // each Cell has 30% of chance to
											// contain a bonus
				bonus=NobilityTrackBonusType.random(bonusNumber);
				track[i] = new NobilityCell(bonus);
				bonus.clear();
			} else
				track[i] = new NobilityCell(bonus);
		}
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