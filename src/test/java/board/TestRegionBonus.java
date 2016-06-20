package board;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import controller.Player;
import model.Board;
import model.City;
import model.NoMoreBonusException;
import model.Region;
import model.Tile;

/**
 * This test verifies if a player satisfies the condition to win the region
 * bonus after he built an emporium in all the cities of a specified region
 * 
 * @author Riccardo
 *
 */
public class TestRegionBonus {

	@Test
	public void test() {
		Board board = new Board(4,3,3,3,3);
		Region region = board.getRegions()[0];
		List<City> regionCities = region.getCities();
		Player player = new Player(1);
		assertEquals(false,region.isEligibleForRegionBonus(player));
		for(City city : regionCities) {
			city.buildEmporium(player);
		}
		assertEquals(true,region.isEligibleForRegionBonus(player));
		Tile regionBonus = null;
		String string = null;
		try {
			regionBonus = region.winRegionBonus(player);
		} catch (NoMoreBonusException e) {
			string=e.showError();
		}
		assertNotEquals("There isn't any REGION BONUS available for this match!",string);
		assertNotEquals(null,regionBonus);
		Region region1=board.getRegions()[1];
		List<City> region1Cities = region1.getCities();
		City city1 = region1Cities.get(0);
		City city2 = region1Cities.get(1);
		city1.buildEmporium(player);
		city2.buildEmporium(player);
		assertEquals(false,region1.isEligibleForRegionBonus(player));
		try {
			regionBonus=region1.winRegionBonus(player);
		} catch (NoMoreBonusException e) {
			string=e.showError();
		}
		assertNotEquals("There isn't any REGION BONUS available for this match!",string);
	}

}
