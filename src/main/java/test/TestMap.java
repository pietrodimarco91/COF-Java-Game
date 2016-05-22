package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import model.City;
import model.Map;

public class TestMap {

	@Test
	public void test() {
		int numberOfPlayers = 8, bonusNumber = 3, linksBetweenCities = 2;
		Map map = new Map(numberOfPlayers, bonusNumber, linksBetweenCities);
		// System.out.println(map.toString());
		assertEquals(27, map.getNumberOfCities());
		assertEquals(93, map.getNumberOfPermitTiles());
		assertEquals(3, map.getRegions().length);

		numberOfPlayers = 4;
		map = new Map(numberOfPlayers, bonusNumber, linksBetweenCities);
		// System.out.println(map.toString());
		assertEquals(15, map.getNumberOfCities());
		assertEquals(45, map.getNumberOfPermitTiles());

		boolean result = map.graphIsConnected();
		assertEquals(false, result);

		System.out.println("UNCONNECTED GRAPH:");
		ArrayList<City> cities = map.getMap();
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
		//System.out.println(map.toString());
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
