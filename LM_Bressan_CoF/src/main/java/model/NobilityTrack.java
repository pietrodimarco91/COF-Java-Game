package model;

import java.util.*;

/**
 * This class represents the NobilityTrack as a fixed-length array of Nobility
 * Cells.
 */
public class NobilityTrack {
	private static final int LENGTH=20;
	/**
	 * The track is an array of NobilityCell.
	 */
	private NobilityCell[] track;

	/**
	 * Default constructor
	 */
	public NobilityTrack() {
		track = new NobilityCell[LENGTH];
		ArrayList<String> values = new ArrayList<String>();
		for(NobilityCell nobilityCell:track) {
			/*Waiting for factory method to be implemented 
			nobilityCell = new NobilityCell(); */	
		}

	}

	/**
	 * 
	 */

}