package model;

import java.util.*;

import controller.Player;

/**
 * This class represents an Emporium that is built inside a City by a specified
 * Player.
 */
public class Emporium {
	
	/**
	 * This color represents the color of the Player that owns this emporium
	 */
	private String color;

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
		this.cityName = cityName;
		this.color=owner.getColor();
	}

	public Player getOwner() {
		return owner;
	}

	public String getCityName() {
		return cityName;
	}

}