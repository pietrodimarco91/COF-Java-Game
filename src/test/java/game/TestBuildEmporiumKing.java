package game;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;

import org.junit.Test;

import client.actions.KingBuildEmporiumAction;
import controller.MatchActionsHandler;
import controller.MatchHandler;
import controller.Player;
import model.Board;
import model.City;
import model.Council;
import model.Councillor;
import model.PoliticCard;

public class TestBuildEmporiumKing {

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

		// Now checking the 'buildEmporiumWithKingsHelp' action
		List<City> cities = board.getMap();
		player.addCoins(10);
		City kingCity = null;
		for (City tempCity : cities) {
			if (tempCity.getKingIsHere())
				kingCity = tempCity;
		}
		assertNotEquals(null, kingCity);
		ArrayList<String> councilColors = new ArrayList<String>();
		Council kingCouncil = board.getKingCouncil();
		Queue<Councillor> councillors = kingCouncil.getCouncillors();
		councilColors.clear();
		councillors = kingCouncil.getCouncillors();
		for (Councillor councillor : councillors) {
			councilColors.add(councillor.getColor());
		}
		player.getPoliticCards().clear();
		for (String color : councilColors) {
			player.addCardOnHand(new PoliticCard(color));
		}
		actionsHandler
				.buildEmporiumWithKingsHelp(new KingBuildEmporiumAction("main", kingCity.getName(), councilColors), 0);
		assertEquals(9, player.getNumberOfEmporium());
	}

}
