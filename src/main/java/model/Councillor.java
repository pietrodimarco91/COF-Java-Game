package model;

/**
 * Created by Gabriele Bressan on 13/05/16.
 */
public class Councillor {
	/**
	 * Colour of the Councillor
	 */
	private String color;

	/**
	 * Default constructor which choose a random color to set a councillor
	 */
	public Councillor() {
		do {
			this.color = CouncillorColors.getRandomColor();
		} while (this.color == "MULTICOLOR");// Check to don't draw a MULTICOLOR
												// Councillor
	}

	/**
	 * @return String 
	 * Return a color of councillor
	 */
	
	public String getColor() {

		return this.color;
	}
}