package game;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import controller.MatchHandler;
import controller.Player;
import model.Board;
import model.City;
import model.Region;

/**
 * This tests check that a specified player wins the important bonuses of the
 * match when he is eligible to win them, such as color bonuses, region bonuses
 * and king reward tiles. Also, we check that he wins all the reward tokens in
 * the owned cities connected to the city where he builds an emporium
 * 
 * @author Riccardo
 *
 */
public class TestWinBuildingBonuses {

	@Test
	public void test() {
		Board board = new Board(4, 2, 2, 2, 2);
		MatchHandler match = new MatchHandler(0, new Date());
		match.setBoard(board);
		match.initializeMatchComponents();
		match.getPlayers().get(0).setPlayerOffline();
		for (int i = 1; i < 4; i++) {
			match.addPlayer(i);
			match.getPlayers().get(i).setPlayerOffline();
			assertEquals(6, match.getPlayers().get(i).getPoliticCards().size());
		}
		Player player = match.getPlayers().get(0);
		assertEquals(0, player.getNumberOfRewardTokensWon());
		Region region = board.getRegions()[0];

		ArrayList<String> cityColors = new ArrayList<String>();
		// Building in all the cities of a region
		boolean colorFound = false;
		int differentColors = 0;
		for (City city : region.getCities()) {
			city.buildEmporium(player);
			for (String color : cityColors) {
				if (color.equals(city.getColor()))
					colorFound = true;
			}
			if (!colorFound) {
				differentColors++;
				cityColors.add(city.getColor());
			}
			colorFound = false;
			match.winBuildingBonuses(city, player);
		}
		assertEquals(1 + 2 + 3 + 4 + 5, player.getNumberOfRewardTokensWon());
		assertEquals(1, player.getNumberOfRegionBonusWon());

		Region region2 = board.getRegions()[1];
		Region region3 = board.getRegions()[2];

		// Building in all the cities of the second region
		for (City city : region2.getCities()) {
			city.buildEmporium(player);
			for (String color : cityColors) {
				if (color.equals(city.getColor()))
					colorFound = true;
			}
			if (!colorFound) {
				differentColors++;
				cityColors.add(city.getColor());
			}
			colorFound = false;
			match.winBuildingBonuses(city, player);
		}

		// Building in all the cities of the third region
		for (City city : region3.getCities()) {
			city.buildEmporium(player);
			for (String color : cityColors) {
				if (color.equals(city.getColor()))
					colorFound = true;
			}
			if (!colorFound) {
				differentColors++;
				cityColors.add(city.getColor());
			}
			colorFound = false;
			match.winBuildingBonuses(city, player);
		}
		assertTrue(differentColors <= 4);

		// I built in all the cities of the map
		assertTrue(player.getNumberOfEmporium() < 0);
		assertEquals(3, player.getNumberOfRegionBonusWon());
		if (differentColors == 4)
			assertEquals(4, player.getNumberOfColorBonusWon());
		else if (differentColors == 3)
			assertEquals(3, player.getNumberOfColorBonusWon());
		else if (differentColors == 2)
			assertEquals(2, player.getNumberOfColorBonusWon());
		else if (differentColors == 1)
			assertEquals(1, player.getNumberOfColorBonusWon());
		assertEquals(5, player.getNumberOfKingRewardBonusWon());
	}

}
