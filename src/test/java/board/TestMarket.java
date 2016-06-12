package board;

import controller.Player;
import exceptions.UnsufficientCoinsException;
import model.*;
import server.view.cli.ServerOutputPrinter;

import org.junit.Test;

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

		Player player1 = new Player(1); //player1 has 11 coins
		
		Player player2 = new Player(2); //player2 has 12 coins
	
		Player player3 = new Player(3); //player3 has 13 coins
		
		ItemFactory itemFactory = new ConcreteItemFactory();
		ItemOnSale item1 = itemFactory.createAssistantOnSale(player1, 6); //player1 has 6 coins
		PoliticCard pc = new PoliticCard();
		ItemOnSale item2 = itemFactory.createPoliticCardOnSale(pc, player2, 7); //player2 has 5 coins
		Tile pt = new PermitTile(1,board.getMap(),2);
		ItemOnSale item3 = itemFactory.createPermitTileOnSale(pt, player3, 10); //player3 has 3 coins
		Market.putItemOnSale(item1);
		Market.putItemOnSale(item2);
		Market.putItemOnSale(item3);
		assertEquals(3,Market.getItemsOnSale().size());
		
		Player player4 = new Player(4); //player4 has 14 coins
		player4.removeCoins(10);; //player4 has 4 coins
		try {
			Market.buyItemOnSale(player4, item1); 
		} catch (UnsufficientCoinsException e) {
			ServerOutputPrinter.printLine(e.showError());
			string=e.showError();
		}
		assertEquals("Unsifficient Coins! The specified coins aren't enough to perform this action",string);
		player4.addCoins(3); //player4 has 7 coins
		try {
			Market.buyItemOnSale(player4, item1);
		} catch (UnsufficientCoinsException e) {
			ServerOutputPrinter.printLine(e.showError());
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
		assertEquals(23,player4.getCoins());
	}

}
