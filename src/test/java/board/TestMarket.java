package board;

import static org.junit.Assert.*;

import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Test;

import controller.ConnectorInt;
import controller.Player;
import controller.SocketConnector;
import exceptions.UnsufficientCoinsException;
import model.Board;
import model.ConcreteItemFactory;
import model.ItemFactory;
import model.ItemOnSale;
import model.Market;
import model.PermitTile;
import model.PoliticCard;
import model.Tile;

/**
 * This test verifies that the Market works correctly
 * @author Riccardo
 *
 */
public class TestMarket {

	private static final Logger logger= Logger.getLogger( TestMarket.class.getName() );
	@Test
	public void test() {
		Board board = new Board(4,2,2,2,2);
		Market market = new Market();
		ArrayList<ItemOnSale> items=new ArrayList<>();
		String string="";
		assertEquals(items.getClass(),Market.getItemsOnSale().getClass());
		
		ConnectorInt connectorInt1 = new SocketConnector(new Socket());
		Player player1 = new Player(connectorInt1);
		ConnectorInt connectorInt2 = new SocketConnector(new Socket());
		Player player2 = new Player(connectorInt2);
		ConnectorInt connectorInt3 = new SocketConnector(new Socket());
		Player player3 = new Player(connectorInt3);
		
		ItemFactory itemFactory = new ConcreteItemFactory();
		ItemOnSale item1 = itemFactory.createAssistantOnSale(player1, 5);
		PoliticCard pc = new PoliticCard();
		ItemOnSale item2 = itemFactory.createPoliticCardOnSale(pc, player2, 10);
		Tile pt = new PermitTile(board.getMap(),2);
		ItemOnSale item3 = itemFactory.createPermitTileOnSale(pt, player3, 15);
		Market.putItemOnSale(item1);
		Market.putItemOnSale(item2);
		Market.putItemOnSale(item3);
		assertEquals(3,Market.getItemsOnSale().size());
		
		ConnectorInt connectorInt4 = new SocketConnector(new Socket());
		Player player4 = new Player(connectorInt1);
		player4.addCoins(3);
		try {
			Market.buyItemOnSale(player4, item1);
		} catch (UnsufficientCoinsException e) {
			logger.log(Level.SEVERE, e.showError(), e);
			string=e.showError();
		}
		assertEquals("Unsifficient Coins! The specified coins aren't enough to perform this action",string);
		player4.addCoins(3);
		try {
			Market.buyItemOnSale(player4, item1);
		} catch (UnsufficientCoinsException e) {
			logger.log(Level.SEVERE, e.showError(), e);
		}
		assertEquals(2,Market.getItemsOnSale().size());
		assertEquals(1,player4.getCoins());
		
		player4.addCoins(39); //now player 4 has 40 coins
		try {
			Market.buyItemOnSale(player4, item2);
			Market.buyItemOnSale(player4, item3);
		} catch (UnsufficientCoinsException e) {
			logger.log(Level.SEVERE, e.showError(), e);
		}
		assertEquals(0,Market.getItemsOnSale().size());
		assertEquals(15,player4.getCoins());
	}

}
