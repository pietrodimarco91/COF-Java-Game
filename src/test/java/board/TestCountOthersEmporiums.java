package board;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import controller.Player;
import model.Board;
import model.City;

public class TestCountOthersEmporiums {

	@Test
	public void test() {
		Board board = new Board(4,3,3,3,3);
		Player player1 = new Player(1);
		Player player2 = new Player(2);
		Player player3 = new Player(3);
		Player player4 = new Player(4);
		List<City> map = board.getMap();
		City city = map.get(0);
		city.buildEmporium(player1);
		city.buildEmporium(player2);
		city.buildEmporium(player3);
		assertEquals(3,city.countOthersEmporiums(player4));
	}

}
