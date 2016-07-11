package model;

/**
 * Created by Pietro Di Marco on 13/05/16.
 * This class is used to assign one color to the tile
 */
public class ColorBonusTile extends Tile {
	/**
	 * Color string of bonus tile
	 */
	private String color;
	
	public ColorBonusTile(int points, String color) {
		super(points);
		this.color = color;
	}

	public String getColor() {
		return this.color;
	}

	@Override
	public String toString() {
		String string = "ColorBonusTile:\n";
		string += "Color: " + color + "\n";
		string += "Points: " + super.getPoints() + "\n";
		return string;
	}
}