package controller;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import exceptions.CardNotFoundException;
import model.PoliticCard;

public class TestPlayerMethods {

	@Test
	public void test() {
		Player player = new Player(0);
		assertEquals(3,player.getNumberOfAssistants());
		assertEquals(10,player.getCoins());
		assertEquals(0,player.getPositionInNobilityTrack());
		assertEquals(0,player.getVictoryPoints());
		player.setPlayerNickName("example");
		assertEquals("example",player.getNickName());
		assertEquals(0,player.getNumberOfPermitTile());
		assertEquals(6,player.getPoliticCards().size());
		PoliticCard card = new PoliticCard();
		player.addCardOnHand(card);
		ArrayList<PoliticCard> cards = player.getPoliticCards();
		assertTrue(cards.contains(card));
		assertEquals(7,player.getPoliticCards().size());
		String s="";
		try {
		player.checkIfYouOwnThisCard(card.getColorCard(), cards);
		} catch(CardNotFoundException e) {
			s+=e.showError();
		}
		assertEquals("",s);
		ArrayList<String> cardsToRemove=new ArrayList<>();
		for(int i=0;i<3;i++)
			cardsToRemove.add(cards.get(i).getColorCard());
		player.removeCardsFromHand(cardsToRemove);
		assertEquals(4,player.getPoliticCards().size());
		card = player.getPoliticCards().get(0);
		player.sellPoliticCard(card.getColorCard());
		assertEquals(3,player.getPoliticCards().size());
		player.changePositionInNobilityTrack(5);
		assertEquals(5,player.getPositionInNobilityTrack());
		assertEquals(0,player.getNumberOfEmporium());
		assertEquals(0,player.getNumberOfControlledCities());
		player.mainActionDone(true);
		assertTrue(player.hasPerformedMainAction());
		player.quickActionDone();
		assertTrue(player.hasPerformedQuickAction());
		player.addVictoryPoints(10);
		assertEquals(10,player.getVictoryPoints());
	}

}
