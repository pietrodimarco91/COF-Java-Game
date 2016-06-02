package board;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import model.CouncillorColors;

public class TestPoliticCardsColors {

	@Test
	public void test() {
		ArrayList<String> colors = CouncillorColors.getPoliticCardsColors();
		assertEquals(CouncillorColors.values().length,colors.size());
	}

}
