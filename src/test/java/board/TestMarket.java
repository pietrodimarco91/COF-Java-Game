package board;

import controller.ConnectorInt;
import controller.Player;
import controller.ServerSideRMIConnector;
import controller.SocketConnector;
import controller.Client.ClientSideRMIConnector;
import exceptions.UnsufficientCoinsException;
import model.*;
import org.junit.Test;

import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.Assert.assertEquals;

/**
 * This test verifies that the Market works correctly
 * @author Riccardo
 *
 */
public class TestMarket {

	private static final Logger logger= Logger.getLogger( TestMarket.class.getName() );
	@Test
	public void test() throws RemoteException {
		Board board = new Board(4,2,2,2,2);
		Market market = new Market();
		ArrayList<ItemOnSale> items=new ArrayList<>();
		String string="";
		assertEquals(items.getClass(),Market.getItemsOnSale().getClass());

		ServerSideRMIConnector clientSideRMIInt1 = new ServerSideRMIConnector(new ClientSideRMIConnector());
		Player player1 = new Player(clientSideRMIInt1,1);
		ServerSideRMIConnector clientSideRMIInt2 = new ServerSideRMIConnector(new ClientSideRMIConnector());
		Player player2 = new Player(clientSideRMIInt2,2);
		ServerSideRMIConnector clientSideRMIInt3 = new ServerSideRMIConnector(new ClientSideRMIConnector());
		Player player3 = new Player(clientSideRMIInt3,3);
		
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

		ServerSideRMIConnector clientSideRMIInt4 = new ServerSideRMIConnector(new ClientSideRMIConnector());
		Player player4 = new Player(clientSideRMIInt4,4);
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
