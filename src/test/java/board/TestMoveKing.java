package board;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import model.City;
import model.Board;

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
		Board map = new Board(4, 2, 2, 2, 2);
		List<City> cities = map.getMap();
		for (City city : cities) {
			map.moveKing(city);
			assertEquals(map.findKingCity(), city);
		}
	}

}
