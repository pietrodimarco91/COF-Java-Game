package demo;

import java.util.ArrayList;
import java.util.Scanner;

import model.City;
import model.Map;

public class MapDemo {

	public static void main(String[] args) {
		int numberOfPlayers = 4, bonusNumber = 3, linksBetweenCities = 3;
		Map map = new Map(numberOfPlayers, bonusNumber, linksBetweenCities);
		System.out.println(map.toString());
		map.printMatrix();
		
		ArrayList<City> cities = map.getMap();
		for (City city4 : cities) {
			for (City city5 : cities) {
				int distance = map.countDistance(city4, city5);
				System.out.println(
						"Distance between " + city4.getName() + " and " + city5.getName() + " is: " + distance);
			}
		}
	}

}
