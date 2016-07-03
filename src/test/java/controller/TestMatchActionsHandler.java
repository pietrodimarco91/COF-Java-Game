package controller;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;

import org.junit.Test;

import client.actions.AdditionalMainAction;
import client.actions.BuyPermitTileAction;
import client.actions.EngageAssistantAction;
import client.actions.SimpleBuildEmporiumAction;
import exceptions.TileNotFoundException;
import model.Board;
import model.City;
import model.Council;
import model.Councillor;
import model.PermitTile;
import model.PoliticCard;
import model.Tile;

public class TestMatchActionsHandler {

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
		Council council = board.getRegions()[0].getCountil();
		ArrayList<String> councilColors = new ArrayList<String>();
		Queue<Councillor> councillors = council.getCouncillors();
		for (Councillor councillor : councillors) {
			councilColors.add(councillor.getColor());
		}
		player.getPoliticCards().clear();
		for (String color : councilColors) {
			player.addCardOnHand(new PoliticCard(color));
		}
		assertEquals(4, player.getPoliticCards().size());
		actionsHandler.buyPermitTile(new BuyPermitTileAction("main", "COAST", councilColors, 1), 0);
		assertEquals(1, player.getNumberOfPermitTile());
		String s = "";
		Tile tile = null;
		try {
			tile = (PermitTile) player.getRandomUnusedPermitTile();
		} catch (TileNotFoundException e) {
			s += "error";
		}
		assertEquals("", s);
		int tileId = ((PermitTile) tile).getId();
		
		player.resetTurn();
		List<City> cities = ((PermitTile) tile).getCities();
		String city = cities.get(0).getName();
		actionsHandler.buildEmporiumWithPermitTile(new SimpleBuildEmporiumAction("main", tileId, city), 0);
		assertEquals(9, player.getNumberOfEmporium());
		
		actionsHandler.performAdditionalMainAction(new AdditionalMainAction("quick"), 0);
		assertFalse(player.hasPerformedMainAction());
		player.addCoins(10);
		int numberOfAssistants = player.getNumberOfAssistants();
		actionsHandler.engageAssistant(new EngageAssistantAction("quick"),0);
		assertEquals(numberOfAssistants+1,player.getNumberOfAssistants());
	}

}
