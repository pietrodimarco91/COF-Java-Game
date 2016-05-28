package model;


import java.util.ArrayList;

/**
 * Created by Gabriele Bressan on 13/05/16.
 */
public abstract class PoliticCardDeck {

	/**
	 * @return PoliticCard 
	 * returns the new card generated
	 */
	public static PoliticCard generateRandomPoliticCard() {
		PoliticCard politicCardDraw = new PoliticCard();
		return politicCardDraw;
	}

	/**
	 * @return an ArrayList of randomized politic card
	 */
	public static ArrayList<PoliticCard> distributePoliticCards() {
		ArrayList<PoliticCard> playerCards = new ArrayList<PoliticCard>();
		PoliticCard singleCard;

		for (int i = 0; i < 6; i++) {
			singleCard = new PoliticCard();
			playerCards.add(i, singleCard);
		}
		return playerCards;
	}

}