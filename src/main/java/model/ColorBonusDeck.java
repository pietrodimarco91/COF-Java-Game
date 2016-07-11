package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import exceptions.NoMoreBonusException;

/**
 * This class represents the deck of the Color Bonus Tiles.
 * 
 * @author Riccardo
 *
 */
public class ColorBonusDeck implements Serializable {

	/**
	 * variable used for serialization 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * List of tiles 
	 */
	private List<Tile> tiles;

	public ColorBonusDeck() {
		tiles = new ArrayList<>();
		TileFactory factory = new ConcreteTileFactory();
		tiles.add(factory.createColorBonusTile(30, String.valueOf(CityColors.GOLD)));
		tiles.add(factory.createColorBonusTile(25, String.valueOf(CityColors.SILVER)));
		tiles.add(factory.createColorBonusTile(20, String.valueOf(CityColors.BRONZE)));
		tiles.add(factory.createColorBonusTile(15, String.valueOf(CityColors.STEEL)));
	}

	/**
	 * @return the color bonus tile of the specified color
	 * @throws NoMoreBonusException
	 *             if the bonus of the specified color has already been won.
	 */
	public Tile getColorBonus(String color) throws NoMoreBonusException {
		if (!tiles.isEmpty()) {
			for (Tile tile : tiles) {
				if (((ColorBonusTile) tile).getColor().equals(color)) {
					tiles.remove(tile);
					return tile;
				}
			}
			throw new NoMoreBonusException("COLOR BONUS");
		} else
			throw new NoMoreBonusException("COLOR BONUS");
	}
	
	@Override
	public String toString() {
		String string="COLOR BONUS DECK\n";
		for(Tile tile : tiles) {
			string+=tile.toString()+"\n";
		}
		return string;
	}
}
