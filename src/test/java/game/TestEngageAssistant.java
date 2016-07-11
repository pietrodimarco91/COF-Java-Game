package game;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Test;

import client.actions.EngageAssistantAction;
import controller.MatchActionsHandler;
import controller.MatchHandler;
import controller.Player;
import model.Board;

public class TestEngageAssistant {

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
		MatchActionsHandler actionsHandler = new MatchActionsHandler(match, board, match.getPlayers());

		// Now checking the 'engageAssistant' action
		player.resetTurn();
		player.addCoins(10);
		int numberOfAssistants = player.getNumberOfAssistants();
		int prevCoins = player.getCoins();
		actionsHandler.engageAssistant(new EngageAssistantAction("quick"), 0);
		int postCoins = player.getCoins();
		assertEquals(numberOfAssistants + 1, player.getNumberOfAssistants());
		assertEquals(postCoins, prevCoins - 3);
		assertTrue(player.hasPerformedQuickAction());

	}

}
