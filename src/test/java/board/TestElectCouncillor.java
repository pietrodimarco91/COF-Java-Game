package board;

import static org.junit.Assert.*;

import org.junit.Test;

import exceptions.CouncillorNotFoundException;
import model.Board;
import model.CouncillorColors;
import model.CouncillorsPool;
import model.Region;

public class TestElectCouncillor {

	@Test
	public void test() {
		Board board = new Board(4, 3, 3, 3, 3);
		String color = CouncillorColors.getRandomColor();
		boolean check = CouncillorsPool.checkPresenceOfCouncillor(color);
		Region region = board.getRegions()[0];
		String string = "";
		if (check) {
			try {
				region.electCouncillor(color);
			} catch (CouncillorNotFoundException e) {
				string = e.showError();
			}
			assertEquals("", string);
		} else {
			try {
				region.electCouncillor(color);
			} catch (CouncillorNotFoundException e) {
				string = e.showError();
			}
			assertEquals("The councillor you are looking for isn't available in the Councillors Pool... try with a different color",
					string);
		}
	}

}
