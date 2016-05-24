package map;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import model.City;
import model.Map;

/**
 * Tests the unconnectCities method of Map
 * 
 * @author Riccardo
 *
 */
public class TestUnconnectCities {
	Map map = new Map(4, 2, 2, 2, 3);

	@Test
	public void test() {
		int initialLength1, initialLength2, finalLength;
		ArrayList<City> cities = map.getMap();
		City city = cities.get(0);
		City connectedCity = city.getConnectedCities().get(0);
		initialLength1 = city.getConnectedCities().size();
		initialLength2 = connectedCity.getConnectedCities().size();
		map.unconnectCities(city, connectedCity);
		assertEquals(initialLength1 - 1, city.getConnectedCities().size());
		assertEquals(initialLength2 - 1, connectedCity.getConnectedCities().size());
	}

}
