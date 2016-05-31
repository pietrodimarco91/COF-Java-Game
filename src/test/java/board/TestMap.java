package board;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import model.City;
import model.Board;

public class TestMap {

	@Test
	public void test() {
		int numberOfPlayers = 8, rewardTokenBonusNumber = 3, permitTileBonusNumber = 3, nobilityTrackBonusNumber = 3,
				linksBetweenCities = 3;
		Board map = new Board(numberOfPlayers, rewardTokenBonusNumber, permitTileBonusNumber, nobilityTrackBonusNumber,
				linksBetweenCities);
		System.out.println(map.toString());

		assertEquals(27, map.getNumberOfCities());
		assertEquals(93, map.getNumberOfPermitTiles());
		assertEquals(3, map.getRegions().length);

		numberOfPlayers = 4;
		map = new Board(numberOfPlayers, rewardTokenBonusNumber, permitTileBonusNumber, nobilityTrackBonusNumber,
				linksBetweenCities); // System.out.println(map.toString());

		assertEquals(15, map.getNumberOfCities());
		assertEquals(45, map.getNumberOfPermitTiles());

		boolean result = map.graphIsConnected();
		assertEquals(false, result);

		System.out.println("UNCONNECTED GRAPH:");
		List<City> cities = map.getMap();
		for (City city4 : cities) {
			for (City city5 : cities) {
				int distance = map.countDistance(city4, city5);
				System.out.println(
						"Distance between " + city4.getName() + " and " + city5.getName() + " is: " + distance);
			}
		}

		City city1 = map.getRegions()[0].getCities().get(0);
		City city2 = map.getRegions()[1].getCities().get(0);
		City city3 = map.getRegions()[2].getCities().get(0);
		map.connectCities(city1, city2);
		map.connectCities(city2, city3);
		System.out.println(map);
		map.printMatrix();
		result = map.graphIsConnected();
		assertEquals(true, result);

		System.out.println("CONNECTED GRAPH:");
		cities = map.getMap();
		for (City city4 : cities) {
			for (City city5 : cities) {
				int distance = map.countDistance(city4, city5);
				System.out.println(
						"Distance between " + city4.getName() + " and " + city5.getName() + " is: " + distance);
			}
		}
	}
}
