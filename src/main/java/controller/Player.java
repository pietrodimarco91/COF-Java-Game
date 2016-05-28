package controller;

import java.util.*;

import model.City;
import model.CouncillorColors;
import model.PermitTile;
import model.PermitTileDeck;
import model.PoliticCard;
import model.PoliticCardDeck;
import model.Tile;

/**
 * 
 */
public class Player {

	/**
	 * 
	 */
	private String userName;

	/**
	 * 
	 */
	private String password;

	/**
	 * 
	 */
	private int matchesWon;

	/**
	 * 
	 */
	private int rageQuits;

	/**
	*
	*/
	private String ipAddress;

	/**
	 *
	 */
	private int port;

	/**
	 *
	 */
	private int coins;

	/**
	 *
	 */
	private int assistants;

	/**
	 *
	 */
	private int turnNumber;

	/**
	 *
	 */
	private ArrayList<PoliticCard> politicCards;

	/**
	 *
	 */
	private int victoryPoints;
	/**
	 *
	 */
	private ArrayList<Tile> usedPermitTiles;
	/**
	 *
	 */
	private ArrayList<Tile> unusedPermitTiles;
	/**
	 *
	 */
	private int emporiums;

	/**
	 *
	 */
	private ArrayList<City> controlledCities;

	/**
	 * 
	 */
	private Connector playerConnector;// To add UML scheme

	/**
	 * Default constructor
	 */
	public Player(Connector playerConnector) {
		this.usedPermitTiles = new ArrayList<Tile>();
		this.unusedPermitTiles = new ArrayList<Tile>();
		this.controlledCities = new ArrayList<City>();
		this.playerConnector = playerConnector;
		initializeFirstHand();// Distributes the first hand of politic cards
	}

	/**
	 * @return
	 */
	public String getUserName() {
		// TODO implement here
		return "";
	}

	/**
	 * @return
	 */
	public String getPassword() {
		// TODO implement here
		return "";
	}

	/**
	 * @return
	 */
	public int getMatchesWon() {
		// TODO implement here
		return 0;
	}

	/**
	 * @return
	 */
	public int getRageQuits() {
		// TODO implement here
		return 0;
	}

	/**
	 * @return
	 */
	public Connector getConnector() { // Da aggiungere UML
		return this.playerConnector;
	}

	/**
	 * 
	 */
	public void initializeFirstHand() {
		this.politicCards = PoliticCardDeck.distributePoliticCards();
	}

	/**
	 * @return
	 */
	public ArrayList<PoliticCard> cardsToCouncilSatisfaction() {
		int numberOfCardsUsed = 0;
		String colorCard;
		boolean flagStopChoose = false;
		ArrayList<PoliticCard> cardsChose = new ArrayList<PoliticCard>();
		ArrayList<PoliticCard> tempHandCards = new ArrayList<PoliticCard>(this.politicCards);
		Scanner input = new Scanner(System.in);
		while (numberOfCardsUsed < 4 && flagStopChoose == false) {
			System.out.println("Write the color card that you would to use:");
			colorCard = input.nextLine();
			System.out.println(colorCard);
			if (!colorCard.equals("stop")) {
				while (!checkExistingColor(colorCard)) {
					System.out.println("You have entered an incorret color Card!");
					System.out.println("Write the color card that yoy would to use:");
					colorCard = input.nextLine();
				}
				while (!checkIfYouHaveThisCard(colorCard, tempHandCards)) {
					System.out.println("You don't have this card!");
					System.out.println("Rewrite the color card that you would to use:");
					colorCard = input.nextLine();
				}
				PoliticCard politicCard = new PoliticCard(colorCard);
				cardsChose.add(politicCard);
				numberOfCardsUsed++;
				System.out.println("Perfect!");
			} else if (colorCard.equals("stop") && cardsChose.size() == 0)
				System.out.println("ERROR: You have to enter at least one card!!");
			else
				flagStopChoose = true;
		}
		return cardsChose;

	}

	/**
	 * @return
	 */
	public boolean checkExistingColor(String colorCard) {
		ArrayList<String> allColorsCards;
		allColorsCards = CouncillorColors.getPoliticCardsColors();
		for (int i = 0; i < allColorsCards.size(); i++) {
			if (allColorsCards.get(i).equals(colorCard))
				return true;
		}
		return false;
	}

	/**
	 * @return
	 */
	public boolean checkIfYouHaveThisCard(String colorCard, ArrayList<PoliticCard> tempHandCards) {
		for (int i = 0; i < tempHandCards.size(); i++) {
			if (tempHandCards.get(i).getColorCard().equals(colorCard)) {
				tempHandCards.remove(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * @return
	 */
	public void removeCardsFromHand(ArrayList<PoliticCard> cardsChose) {
		for (int i = 0; i < cardsChose.size(); i++)
			if (this.politicCards.contains(cardsChose.get(i)))
				this.politicCards.remove(i);
	}

	/**
	 * @return
	 */
	public void addCardOnHand(PoliticCard card) {
		this.politicCards.add(card);
	}

	/**
	 * @return
	 */
	public boolean performPayment(int payment) {
		if ((this.coins - payment) >= 0) {
			this.coins -= payment;
			return true;
		} else
			return false;
	}

	/**
	 * @return
	 */
	public void setUnusedPermitTiles(Tile permitTile) {
		this.unusedPermitTiles.add((PermitTile) permitTile);
	}

	/**
	 * @return
	 */
	public int getNumberOfPermitTile() {
		return this.unusedPermitTiles.size();
	}

	/**
	 * @return
	 */
	public void addCoins(int coins) {
		this.coins = coins;
	}

	/**
	 * @return
	 */
	public void removeCoins(int coins) {
		this.coins -= coins;
	}

	/**
	 * @return
	 */
	public int getCoins() {
		return this.coins;
	}

	/**
	 * @return
	 */
	public ArrayList<PoliticCard> getPoliticCards() {
		return this.politicCards;
	}

	/**
	 * @return
	 */
	public void addAssistant() {
		this.assistants++;
	}

	/**
	 * @return
	 */
	public void removeAssistant() {
		this.assistants--;
	}

	/**
	 * @return
	 */
	public int getNumberAssistants() {
		return this.assistants;
	}

}