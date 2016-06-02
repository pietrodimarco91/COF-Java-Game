package board;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Random;

import org.junit.Test;

import model.Board;
import model.City;

/**
 * This tet verifies the method getCitiesConnectedTo(City city), as it should
 * return the list of cities directly connected with the specified city
 * 
 * @author Riccardo
 *
 */
public class TestGetCitiesConnectedTo {

	@Test
	public void test() {
		Random random = new Random();
		Board board = new Board(4,3,3,3,3);
		List<City> map = board.getMap();
		City randomCity = map.get(random.nextInt(map.size()));
		List<City> connectedCities = randomCity.getConnectedCities();
		assertEquals(connectedCities,board.getCitiesConnectedTo(randomCity));
	}

}
