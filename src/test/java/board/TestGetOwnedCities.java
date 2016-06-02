package board;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import controller.Player;
import model.Board;
import model.City;
import model.Region;

public class TestGetOwnedCities {

	@Test
	public void test() {
		Scanner input = new Scanner(System.in);
		Board board = new Board(4,3,3,3,3);
		List<City> map = board.getMap();
		Player player1=new Player(1);
		String cityName="";
		City tempCity=null;
		int counter=0;
		while(!cityName.equals("stop")) {
			System.out.println("Where do you want to build your emporium?");
			System.out.println(board.printMatrix());
			System.out.println(board.printConnections());
			cityName=input.nextLine();
			for(City city : map) {
				if(city.getName().equals(cityName)) {
					city.buildEmporium(player1);
					tempCity=city;
					System.out.println("EMPORIUM BUILT");
					counter++;
				}
			}
		}
		List<City> ownedCities = board.getNearbyOwnedCities(player1,tempCity);
		assertEquals(counter,ownedCities.size());
		
		int k=0;
		for(City city:map) {
			if(board.countDistance(city, tempCity)==-1) {
				city.buildEmporium(player1);
				k++;
				counter++;
			}
		}
		
		ownedCities = board.getNearbyOwnedCities(player1,tempCity);
		assertEquals(counter-k,ownedCities.size());
	}

}
