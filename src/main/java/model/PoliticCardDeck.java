package model;


import java.util.ArrayList;

/**
 * Created by Gabriele Bressan on 13/05/16.
 */
public class PoliticCardDeck {

	/**
	 * Default constructor
	 */
	public PoliticCardDeck() {
	}

	/**
	 * @return PoliticCard 
	 * return the new card generated
	 */
	public PoliticCard generateRandomPoliticCard() {
		PoliticCard politicCardDraw = new PoliticCard();
		return politicCardDraw;
	}

	/**
	 * @return an ArrayList of randomized politic card
	 */
	public ArrayList<PoliticCard> distributePoliticCards() {
		ArrayList<PoliticCard> playerCards = new ArrayList<PoliticCard>();
		int i;
		PoliticCard singleCard;

		for (i = 0; i < 6; i++) {
			singleCard = new PoliticCard();
			playerCards.add(i, singleCard);
		}
		return playerCards;
	}

}