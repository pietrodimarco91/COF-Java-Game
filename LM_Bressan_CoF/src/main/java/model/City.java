package model;

import java.util.*;

import controller.Player;

/**
 * 
 */
public class City {

	/**
	 * Default constructor
	 */
	public City() {
	}

	/**
	 * 
	 */
	private String name;

	/**
	 * 
	 */
	private String color;

	/**
	 * 
	 */
	private ArrayList<Emporium> emporiums;

	/**
	 * 
	 */
	private Region region;

	/**
	 * 
	 */
	private Point coordinate;

	/**
	 * 
	 */
	private RewardToken rewardToken;

	/**
	 * 
	 */
	private boolean kingIsHere;

	/**
	 * 
	 */
	private boolean visited;

	/**
	 * @return
	 */
	public boolean getKingIsHere() {
		// TODO implement here
		return false;
	}

	/**
	 * @return
	 */
	public Region getRegion() {
		// TODO implement here
		return null;
	}

	/**
	 * @return
	 */
	public String getColor() {
		// TODO implement here
		return "";
	}

	/**
	 * @param value
	 * @return
	 */
	public void setKingIsHere(boolean value) {
		// TODO implement here
	}

	/**
	 * @param owner
	 * @return
	 */
	public void buildEmporium(Player owner) {
		// TODO implement here

	}

	/**
	 * @param owner
	 * @return
	 */
	public int countEmporiums(Player owner) {
		// TODO implement here
		return 0;
	}

	/**
	 * @return
	 */
	public String winBonus() {
		// TODO implement here
		return "";
	}

	/**
	 * @param owner
	 * @return
	 */
	public int countOthersEmporiums(Player owner) {
		// TODO implement here
		return 0;
	}

	/**
	 * @param owner
	 * @return
	 */
	public boolean checkPresenceOfEmporiums(Player owner) {
		// TODO implement here
		return false;
	}

	/**
	 * @return
	 */
	public void setVisited() {
		// TODO implement here

	}

}