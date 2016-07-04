package board;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import controller.Player;
import exceptions.NoMoreBonusException;
import model.Board;
import model.City;
import model.CityColors;
import model.Tile;

/**
 * This test verifies if a player is eligible for color bonus after he has built
 * an emporium in all the cities of a specified color
 * 
 * @author Riccardo
 *
 */
public class TestIsEligibleForColorBonus {

	@Test
	public void test() {
		Board board = new Board(4, 3, 3, 3, 3);
		Player player = new Player(1);
		String randomCityColor = CityColors.random();
		String string = "";
		List<City> map = board.getMap();
		assertEquals(false, board.isEligibleForColorBonus(player, randomCityColor));
		List<Tile> rewardTokens = new ArrayList<>();
		for (City city : map) {
			if (city.getColor().equals(randomCityColor)) {
				city.buildEmporium(player);
				rewardTokens.add(city.winBonus());
			}
		}
		for(Tile rewardToken : rewardTokens) {
			assertNotEquals(null,rewardToken);
		}
		boolean notAlreadyBuilt=true;
		for (City city : map) {
			if (city.getColor().equals(randomCityColor)) {
				notAlreadyBuilt=city.buildEmporium(player);
			}
		}
		assertEquals(false,notAlreadyBuilt);
		assertEquals(true, board.isEligibleForColorBonus(player, randomCityColor));
		Tile colorBonus = null;
		Tile kingReward = null;
		try {
			colorBonus = board.winColorBonus(randomCityColor);
			kingReward = board.winKingReward();
		} catch (NoMoreBonusException e) {
			string = e.showError();
		}
		assertEquals("", string);
		assertNotEquals(null, colorBonus);
		assertNotEquals(null, kingReward);
	}

}
