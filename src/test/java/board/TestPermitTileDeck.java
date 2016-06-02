package board;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Test;

import exceptions.InvalidSlotException;
import model.Board;
import model.PermitTileDeck;
import model.Region;
import model.Tile;

/**
 * This class tests the methods for the permit tile deck
 * @author Riccardo
 *
 */
public class TestPermitTileDeck {

	@Test
	public void test() {
		Board board = new Board(4,3,3,3,3);
		Region region = board.getRegions()[0];
		PermitTileDeck permitTileDeck = region.getDeck();
		Queue<Tile> deck = new LinkedList<>();
		deck=permitTileDeck.getDeck();
		Tile permitTile1=permitTileDeck.getUnconveredPermitTile1();
		Tile permitTile2=permitTileDeck.getUnconveredPermitTile2();
		permitTileDeck.switchPermitTiles();
		assertEquals(true,deck.contains(permitTile2));
		assertEquals(true,deck.contains(permitTile1));
		assertNotEquals(permitTile1,permitTileDeck.getUnconveredPermitTile1());
		assertNotEquals(permitTile2,permitTileDeck.getUnconveredPermitTile2());
		String string="";
		try {
			permitTile1=permitTileDeck.drawPermitTile(1);
		} catch (InvalidSlotException e) {
			string=e.showError();
		}
		assertEquals("",string);
		try {
			permitTile2=permitTileDeck.drawPermitTile(2);
		} catch (InvalidSlotException e) {
			string=e.showError();
		}
		assertEquals("",string);
		try {
			permitTile1=permitTileDeck.drawPermitTile(0);
		} catch (InvalidSlotException e) {
			string=e.showError();
		}
		assertEquals("Invalid slot! Please choose correct slot!",string);
	}

}
