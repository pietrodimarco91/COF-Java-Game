package game;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;

import org.junit.Test;

import client.actions.AdditionalMainAction;
import client.actions.BuyPermitTileAction;
import client.actions.ElectCouncillorAction;
import client.actions.EngageAssistantAction;
import client.actions.KingBuildEmporiumAction;
import client.actions.SimpleBuildEmporiumAction;
import controller.MatchActionsHandler;
import controller.MatchHandler;
import controller.Player;
import exceptions.TileNotFoundException;
import exceptions.UnsufficientCoinsException;
import model.Board;
import model.City;
import model.Council;
import model.Councillor;
import model.CouncillorsPool;
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
		Council council = board.getRegions()[0].getCouncil();
		ArrayList<String> councilColors = new ArrayList<String>();
		Queue<Councillor> councillors = council.getCouncillors();
		for (Councillor councillor : councillors) {
			councilColors.add(councillor.getColor());
		}
		player.getPoliticCards().clear();
		for (String color : councilColors) {
			player.addCardOnHand(new PoliticCard(color));
		}
		
		//Now checking the 'buyPermitTile' action
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
		
		//Now checking the 'buildEmpoiumWithPermitTile'
		player.resetTurn();
		List<City> cities = ((PermitTile) tile).getCities();
		int i=0;
		City city = cities.get(i);
		while(city.getKingIsHere()) {
			i++;
			city = cities.get(i);
		}
		String cityName = cities.get(i).getName();
		
		actionsHandler.buildEmporiumWithPermitTile(new SimpleBuildEmporiumAction("main", tileId, cityName), 0);
		assertEquals(9, player.getNumberOfEmporium());
		
		//Now checking the 'performAdditionalMainAction' action
		actionsHandler.performAdditionalMainAction(new AdditionalMainAction("quick"), 0);
		assertFalse(player.hasPerformedMainAction());
		assertTrue(player.hasPerformedQuickAction());
		
		//Now checking the 'engageAssistant' action
		player.resetTurn();
		player.addCoins(10);
		int numberOfAssistants = player.getNumberOfAssistants();
		actionsHandler.engageAssistant(new EngageAssistantAction("quick"),0);
		assertEquals(numberOfAssistants+1,player.getNumberOfAssistants());
		
		//Now checking the 'buildEmporiumWithKingsHelp' action
		player.resetTurn();
		cities = board.getMap();
		player.addCoins(10);
		City kingCity = null;
		for(City tempCity : cities) {
			if(tempCity.getKingIsHere())
				kingCity=tempCity;
		}
		assertNotEquals(null,kingCity);
		Council kingCouncil = board.getKingCouncil();
		councilColors.clear();
		councillors = kingCouncil.getCouncillors();
		for (Councillor councillor : councillors) {
			councilColors.add(councillor.getColor());
		}
		player.getPoliticCards().clear();
		for (String color : councilColors) {
			player.addCardOnHand(new PoliticCard(color));
		}
		actionsHandler.buildEmporiumWithKingsHelp(new KingBuildEmporiumAction("main", kingCity.getName(), councilColors), 0);
		assertTrue(player.hasPerformedMainAction());
		assertEquals(8,player.getNumberOfEmporium());
		
		//Now checking the 'electCouncillor' action
		player.resetTurn();
		try {
			player.performPayment(player.getCoins());
		} catch (UnsufficientCoinsException e) {
			e.printStackTrace();
		}
		assertEquals(0,player.getCoins());
		String regionName = board.getRegions()[0].getName();
		council = board.getRegions()[0].getCouncil();
		Councillor councillor = CouncillorsPool.getCouncillor();
		String councillorColor = councillor.getColor();
		CouncillorsPool.addCouncillor(councillor);
		int previousCoins = player.getCoins();
		actionsHandler.electCouncillor(new ElectCouncillorAction("main", regionName, councillorColor), 0);
		int postCoins = player.getCoins();
		assertEquals(previousCoins+4,postCoins);
		assertTrue(player.hasPerformedMainAction());
	}

}