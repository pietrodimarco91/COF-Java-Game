package controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestRandomPlayerIterator {

	@Test
	public void test() {
		List<Player> players = new ArrayList<>();
		for(int i=0;i<10;i++) {
			players.add(new Player(i));
		}
		Player player;
		int count=0;
		RandomPlayerIterator iterator = new RandomPlayerIterator(players);
		while(iterator.hasNext()) {
			player = iterator.next();
			System.out.println(player.getId());
			count++;
			assertTrue(players.contains(player));
		}
		assertEquals(10,count);
		
		PlayerTurnIterator turnIterator = new PlayerTurnIterator(players);
		System.out.println("\n\n");
		for(int i=0;i<50;i++) {
			player = turnIterator.next();
			assertTrue(players.contains(player));
			System.out.println(player.getId());
		}
	}

}
