package model;

import java.util.*;

import controller.Player;

/**
 * 
 */
public class Emporium {

	/**
	 * The owner of the emporium
	 */
	private Player owner;

	/**
	 * The city where the emporium has been built.
	 */
	private String cityName;

	/**
	 * Instantiates an Emporium with its owner and the city where it is built.
	 * Thus, an emporium is instantiated straight before it is built.
	 */
	public Emporium(Player owner, String cityName) {
		this.owner = owner;
		this.cityName=cityName;
	}

	public Player getOwner() {
		return owner;
	}

	public String getCityName() {
		return cityName;
	}

}