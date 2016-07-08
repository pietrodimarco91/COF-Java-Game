package client.view.gui.configurator;

import javafx.scene.shape.Line;
import model.City;

/**
 * This class is used to match a link between to cities (Line) to the source and
 * destination cities in the GUI Map
 * 
 * @author Riccardo
 *
 */
public class SingleLink {
	private Line line;

	private City city1;

	private City city2;

	public SingleLink(City city1, City city2, Line line) {
		this.line = line;
		this.city1 = city1;
		this.city2 = city2;
	}

	public City getCity1() {
		return this.city1;
	}

	public City getCity2() {
		return this.city2;
	}

	public Line getLine() {
		return this.line;
	}
}
