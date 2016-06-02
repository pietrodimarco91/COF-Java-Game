package model;


import java.util.ArrayList;

/**
 * Created by Gabriele Bressan on 13/05/16.
 */
public abstract class PoliticCardDeck {

	private static final int FIRST_HAND_POLITIC_CARDS=6;
	/**
	 * @return PoliticCard 
	 * returns the new card generated
	 */
	public static PoliticCard generateRandomPoliticCard() {
		return new PoliticCard();
	}

	/**
	 * @return an ArrayList of randomized politic card
	 */
	public static ArrayList<PoliticCard> distributePoliticCards() {
		ArrayList<PoliticCard> playerCards = new ArrayList<>();

		for (int i = 0; i < FIRST_HAND_POLITIC_CARDS; i++) {
			playerCards.add(generateRandomPoliticCard());
		}
		return playerCards;
	}

}