package demo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import model.City;
import model.Board;

public class MapDemo {

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		int numberOfPlayers, permitTileBonusNumber, rewardTokenBonusNumber, nobilityTrackBonusNumber,
				linksBetweenCities, choice;
		boolean stop = false;
		while (!stop) {
			do {
				System.out.println("Maximum number of players (between 2 and 8)");
				numberOfPlayers = input.nextInt();
			} while (numberOfPlayers > 8 || numberOfPlayers < 2);
			do {
				System.out.println("Number of bonuses per Permit Tile (between 1 and 3)");
				permitTileBonusNumber = input.nextInt();
			} while (permitTileBonusNumber > 3 || permitTileBonusNumber < 1);
			do {
				System.out.println("Number of bonuses per Reward Token (City bonus) (between 1 and 3)");
				rewardTokenBonusNumber = input.nextInt();
			} while (rewardTokenBonusNumber > 3 || rewardTokenBonusNumber < 1);
			do {
				System.out.println("Number of bonuses per Nobility Track Cell (between 1 and 3)");
				nobilityTrackBonusNumber = input.nextInt();
			} while (nobilityTrackBonusNumber > 3 || nobilityTrackBonusNumber < 1);
			do {
				System.out.println("Maximum number of connections between the cities (between 2 and 4)");
				linksBetweenCities = input.nextInt();
			} while (linksBetweenCities > 4 || linksBetweenCities < 2);
			Board map = new Board(numberOfPlayers, rewardTokenBonusNumber, permitTileBonusNumber,
					nobilityTrackBonusNumber, linksBetweenCities);
			while (!stop) {
				System.out.println("Next choice?");
				System.out.println(
						"1) New connection\n2)Remove connection\n3) Go on\n4) View graphic map\n5) View links\n6) View map status\n7) Count distance\n8) Show all distances");
				choice = input.nextInt();
				switch (choice) {
				case 1:
					generateConnection(map, linksBetweenCities);
					break;
				case 2:
					removeConnection(map);
					break;
				case 3:
					if (map.graphIsConnected()) {
						stop = true;
						break;
					} else
						System.out.println("Error: map is not connected. Add the necessary connections.");
					break;
				case 4:
					map.printMatrix();
					break;
				case 5:
					map.printConnections();
					break;
				case 6:
					System.out.println(map);
					break;
				case 7:
					countDistance(map);
					break;
				case 8:
					map.printDistances();
					break;
				default:
					System.out.println("Error: invalid number");
				}
			}
		}
	}

	public static void generateConnection(Board map, int linksBetweenCities) {
		String first, second;
		City city1 = null, city2 = null, tempCity;
		List<City> cities = map.getMap();
		Iterator<City> cityIterator = cities.iterator();
		Scanner input = new Scanner(System.in);
		System.out.println("NEW CONNECTION");

		do {
			System.out.println("Insert the FIRST letter of the first city:");
			first = input.nextLine();
			first = first.toUpperCase();
		} while (first.length() > 1);
		do {
			System.out.println("Insert the FIRST letter of the second city:");
			second = input.nextLine();
			second = second.toUpperCase();
		} while (second.length() > 1 || second.equals(first));

		while (cityIterator.hasNext()) {
			tempCity = cityIterator.next();
			if (tempCity.getName().charAt(0) == first.charAt(0)) {
				city1 = tempCity;
			} else if (tempCity.getName().charAt(0) == second.charAt(0)) {
				city2 = tempCity;
			}
		}
		if (map.checkPossibilityOfNewConnection(city1, city2, linksBetweenCities))
			map.connectCities(city1, city2);
		else {
			System.out.println("Error: cities cannot be connected");
		}
	}

	public static void removeConnection(Board map) {
		String first, second;
		City city1 = null, city2 = null, tempCity;
		List<City> cities = map.getMap();
		Iterator<City> cityIterator = cities.iterator();
		Scanner input = new Scanner(System.in);
		System.out.println("REMOVE CONNECTION");

		do {
			System.out.println("Insert the FIRST letter of the first city:");
			first = input.nextLine();
			first = first.toUpperCase();
		} while (first.length() > 1);
		do {
			System.out.println("Insert the FIRST letter of the second city:");
			second = input.nextLine();
			second = second.toUpperCase();
		} while (second.length() > 1 || second.equals(first));

		while (cityIterator.hasNext()) {
			tempCity = cityIterator.next();
			if (tempCity.getName().charAt(0) == first.charAt(0)) {
				city1 = tempCity;
			} else if (tempCity.getName().charAt(0) == second.charAt(0)) {
				city2 = tempCity;
			}
		}
		map.unconnectCities(city1, city2);
	}

	public static void countDistance(Board map) {
		String first, second;
		City city1 = null, city2 = null, tempCity;
		List<City> cities = map.getMap();
		Iterator<City> cityIterator = cities.iterator();
		Scanner input = new Scanner(System.in);
		System.out.println("COUNT DISTANCE:");

		do {
			System.out.println("Insert the FIRST letter of the first city:");
			first = input.nextLine();
			first = input.nextLine();
		} while (first.length() > 1);
		do {
			System.out.println("Insert the FIRST letter of the second city:");
			second = input.nextLine();
			second = second.toUpperCase();
		} while (second.length() > 1 || second.equals(first));

		while (cityIterator.hasNext()) {
			tempCity = cityIterator.next();
			if (tempCity.getName().charAt(0) == first.charAt(0)) {
				city1 = tempCity;
			} else if (tempCity.getName().charAt(0) == second.charAt(0)) {
				city2 = tempCity;
			}
		}
		if (city1 != null && city2 != null)
			System.out.println("Distance between " + city1.getName() + " and " + city2.getName() + " is: "
					+ map.countDistance(city1, city2));
	}

}
