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
		Board board = new Board(4,3,3,3,3);
		List<City> map = board.getMap();
		Region region = board.getRegions()[0];
		Player player1=new Player(1);
		City city1= region.getCities().get(0);
		City city2=region.getCities().get(1);
		City city3=region.getCities().get(2);
		city1.buildEmporium(player1);
		city2.buildEmporium(player1);
		city3.buildEmporium(player1);
		List<City> ownedCities = board.getNearbyOwnedCities(player1,city1);
		assertEquals(3,ownedCities.size());
		for(City city:map) {
			if(board.countDistance(city, city1)==-1&&board.countDistance(city, city2)==-1&&board.countDistance(city, city3)==-1) {
				city.buildEmporium(player1);
			}
		}
		ownedCities = board.getNearbyOwnedCities(player1,city1);
		assertEquals(3,ownedCities.size());
	}

}
