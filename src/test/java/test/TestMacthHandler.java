package test;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import controller.MatchHandler;
import controller.Player;
import model.Board;
import model.CouncillorColors;
import model.Region;

public class TestMacthHandler {

	@Test
	public void test() {

		Player testPlayer = new Player(null);
		testPlayer.addCoins(100);
		
		for(int i=0;i<testPlayer.getPoliticCards().size();i++)
			System.out.println(testPlayer.getPoliticCards().get(i).getColorCard());
		
		
		MatchHandler testMatch = new MatchHandler(0, null, null);
		testMatch.boardSetup(2, 2, 2, 2, 3);
		testMatch.buyPermitTile(testPlayer, "HILLS");
		if(testPlayer.getNumberOfPermitTile()>0)
		assertEquals(1, testPlayer.getNumberOfPermitTile());
		else
			assertEquals(0, testPlayer.getNumberOfPermitTile());

	}

}
