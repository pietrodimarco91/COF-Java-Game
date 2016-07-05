package game;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.Test;

import controller.MatchHandler;
import controller.Player;
import model.Board;

public class TestMatchWinner {
	

	@Test
	public void test() {
		ArrayList<Player> players=new ArrayList<Player>();
		Board board = new Board(4,2,2,2,2);
		MatchHandler matchHandler=new MatchHandler(0, new Date());
		players=matchHandler.getPlayers();
		
		for(int i=1;i<4;i++){
			players.add(new Player(i));
		}
		
		for(Player player:players){
			player.setPlayerOffline();
		}
		
		for(Player player:players){
			System.out.println(player);
		}
		
		
		//I establish that the winner player has got id 0
		Player player;
		player=players.get(0);
		player.addVictoryPoints(100);
		
		for(Player tempPlayer:players){
			System.out.println(tempPlayer);
		}
		
		matchHandler.notifyMatchWinner();
		assertEquals(true,player.getPlayerWon());
	}
	

}
