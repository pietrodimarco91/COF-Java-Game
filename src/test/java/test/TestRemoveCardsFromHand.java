package test;



import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import org.junit.Test;

import controller.Player;

import model.PoliticCard;

public class TestRemoveCardsFromHand {

	@Test
	public void test() {
		ArrayList<String>cardToRemove=new ArrayList<String>();
		ArrayList<String>resultCards=new ArrayList<String>();
		Player player=new Player(0);
		player.getPoliticCards().clear();
		
		player.addCardOnHand(new PoliticCard("PINK"));
		player.addCardOnHand(new PoliticCard("BLUE"));
		player.addCardOnHand(new PoliticCard("BLACK"));
		player.addCardOnHand(new PoliticCard("MULTICOLOR"));
		player.addCardOnHand(new PoliticCard("MULTICOLOR"));
		player.addCardOnHand(new PoliticCard("MULTICOLOR"));
		player.addCardOnHand(new PoliticCard("WHITE"));
		player.addCardOnHand(new PoliticCard("PURPLE"));
		
		resultCards.add("PINK");
		resultCards.add("BLUE");
		resultCards.add("BLACK");
		resultCards.add("MULTICOLOR");
		resultCards.add("PURPLE");
		
		
		cardToRemove.add("MULTICOLOR");
		cardToRemove.add("MULTICOLOR");
		cardToRemove.add("WHITE");
		cardToRemove.add("WHITE");
		
		player.removeCardsFromHand(cardToRemove);
		
		for(int i=0;i<player.getPoliticCards().size();i++)
			assertEquals(player.getPoliticCards().get(i).getColorCard(),resultCards.get(i));	
		
	}

}
