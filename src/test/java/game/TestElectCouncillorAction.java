package game;

import static org.junit.Assert.*;
import java.util.Date;
import org.junit.Test;
import client.actions.ElectCouncillorAction;
import controller.MatchActionsHandler;
import controller.MatchHandler;
import controller.Player;
import exceptions.UnsufficientCoinsException;
import model.Board;
import model.Councillor;
import model.CouncillorsPool;

public class TestElectCouncillorAction {

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

		// Now checking the 'electCouncillor' action
		try {
			player.performPayment(player.getCoins());
		} catch (UnsufficientCoinsException e) {
			e.printStackTrace();
		}
		assertEquals(0, player.getCoins());
		String regionName = board.getRegions()[0].getName();
		Councillor councillor = board.getPool().getCouncillor();
		String councillorColor = councillor.getColor();
		board.getPool().addCouncillor(councillor);
		int previousCoins = player.getCoins();
		actionsHandler.electCouncillor(new ElectCouncillorAction("main", regionName, councillorColor), 0);
		int postCoins = player.getCoins();
		assertEquals(previousCoins + 4, postCoins);
		assertTrue(player.hasPerformedMainAction());
	}

}