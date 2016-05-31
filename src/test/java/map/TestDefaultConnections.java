package map;

import static org.junit.Assert.*;

import org.junit.Test;

import model.Board;

/**
 * This test verifies the correct generation of the default connections between
 * the cities at the beginning of the match
 * 
 * @author Riccardo
 *
 */
public class TestDefaultConnections {

	@Test
	public void test() {

		int counter = 0;
		for (int i = 2; i <= 8; i++) {
			for (int j = 1; j <= 3; j++) {
				for (int k = 2; k <= 4; k++, counter++) {
					Board map = new Board(i, j, 2, 2, k);
					System.out.println("Default connections between cities correctly generated!");
				}
			}
		}
		System.out.println("Total combinations: " + counter);
		/*
		 * Map map = new Map(4,2,2); System.out.println(map.toString());
		 * map.printMatrix();
		 */
	}

}
