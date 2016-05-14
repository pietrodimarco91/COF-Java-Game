package model;

/**
 * 
 */
public class Councillor {
	/**
	 * Colour of the Councillor
	 */
	private String color;

	/**
	 * Default constructor
	 */
	public Councillor() {
		do {
			this.color = CouncillorColors.getRandomColor();
		} while (this.color == "MULTICOLOR");// Check to don't draw a MULTICOLOR Councillor
	}
	
	public String getColor(){

		return this.color;
	}
 }