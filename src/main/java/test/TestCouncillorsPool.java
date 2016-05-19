package test;
import static org.junit.Assert.*;

import org.junit.Test;

import model.CouncillorColors;
import model.CouncillorsPool;

public class TestCouncillorsPool {

	@Test
	public void test() {
		int councillorsPerColor = 24 / (CouncillorColors.values().length - 1);
		assertEquals(4,councillorsPerColor);
	}

}
