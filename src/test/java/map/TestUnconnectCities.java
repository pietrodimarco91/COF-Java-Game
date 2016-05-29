package map;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import model.City;
import model.Board;

/**
 * Tests the unconnectCities method of Map
 * 
 * @author Riccardo
 *
 */
public class TestUnconnectCities {
	Board map = new Board(4, 2, 2, 2, 3);

	@Test
	public void test() {
		int initialLength1, initialLength2;
		List<City> cities = map.getMap();
		City city = cities.get(0);
		City connectedCity = city.getConnectedCities().get(0);
		initialLength1 = city.getConnectedCities().size();
		initialLength2 = connectedCity.getConnectedCities().size();
		map.unconnectCities(city, connectedCity);
		assertEquals(initialLength1 - 1, city.getConnectedCities().size());
		assertEquals(initialLength2 - 1, connectedCity.getConnectedCities().size());
	}

}
