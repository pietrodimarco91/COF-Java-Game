package map;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import model.City;
import model.Map;

/**
 * Tests if the king is correctly moved from the source city to the destination
 * city
 * 
 * @author Riccardo
 *
 */
public class TestMoveKing {

	@Test
	public void test() {
		Map map = new Map(4, 2, 2, 2, 2);
		ArrayList<City> cities = map.getMap();
		for (City city : cities) {
			map.moveKing(city);
			assertEquals(map.findKingCity(), city);
		}
	}

}
