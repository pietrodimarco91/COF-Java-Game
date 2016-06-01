package controller;

import model.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * 
 */
public class Player {

	/**
	 *
	 */
	private final static int INITIAL_COINS = 10;
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
	 * The color of the player in the current match
	 */
	private String color;
	/**
	 *
	 */
	private ClientSideRMIInt playerClientSideRMIInt;// To add UML scheme

	/**
	 * Default constructor
	 */
	public Player(ClientSideRMIInt playerClientSideRMIInt, int id) {
		Random random = new Random();
		this.turnNumber = id;
		this.usedPermitTiles = new ArrayList<Tile>();
		this.unusedPermitTiles = new ArrayList<Tile>();
		this.controlledCities = new ArrayList<City>();
		this.playerClientSideRMIInt = playerClientSideRMIInt;
		initializeFirstHand();// Distributes the first hand of politic cards
		this.victoryPoints = 0;
		this.color=String.valueOf(new Color(random.nextFloat(),random.nextFloat(),random.nextFloat()));
	}
	
	/**
	 * 
	 * @return
	 */
	public String getColor() {
		return this.color;
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

		return 0;
	}

	/**
	 * @return
	 */
	public Tile getUnusedPermitTile(int choice) {// metodo da risistemare e da aggiungere UML
		return this.unusedPermitTiles.get(choice);												
	}

	/**
	 * @return
	 */
	public ClientSideRMIInt getConnector() { // Da aggiungere UML
		return this.playerClientSideRMIInt;
	}

	/**
	 * 
	 */
	public void initializeFirstHand() {
		this.politicCards = PoliticCardDeck.distributePoliticCards();
	}

	/**
	 * NEEDS REVISION: must implement exceptions and correct communication with
	 * the client.
	 * 
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
				while (!checkIfYouOwnThisCard(colorCard, tempHandCards)) {
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
	 * This method checks whether the specified color is a valid Politic Card
	 * color or not.
	 * 
	 * @return true if the color is correct, false otherwise
	 */
	public boolean checkExistingColor(String colorCard) {
		colorCard = colorCard.toUpperCase();
		colorCard = colorCard.trim();
		ArrayList<String> allColorsCards;
		allColorsCards = CouncillorColors.getPoliticCardsColors();
		for (String color : allColorsCards) {
			if (color.equals(colorCard))
				return true;
		}
		return false;
	}

	/**
	 * Checks whether the player owns a PoliticCard of the specified color or
	 * not.
	 * 
	 * @return true if the player owns it, false otherwise
	 */
	public boolean checkIfYouOwnThisCard(String colorCard, ArrayList<PoliticCard> tempHandCards) {
		for (int i = 0; i < tempHandCards.size(); i++) {
			if (tempHandCards.get(i).getColorCard().equals(colorCard)) {
				tempHandCards.remove(i);
				return true;
			}
		}
		return false;
	}

	/**
	 * This method removed the specified PoliticCard from the hand of the
	 * player.
	 */
	public void removeCardsFromHand(ArrayList<PoliticCard> cardsChose) {
		for (int i = 0; i < cardsChose.size(); i++)
			if (this.politicCards.contains(cardsChose.get(i)))
				this.politicCards.remove(i);
	}

	/**
	 * This method adds the specified PoliticCard to the hand of the player
	 */
	public void addCardOnHand(PoliticCard card) {
		this.politicCards.add(card);
	}

	/**
	 * NEEDS REVISION! Must implement exception handling instead of boolean
	 * return.
	 * 
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
	 * This method adds the specified PermitTile to the list of the unused
	 * Permit Tiles of the player.
	 */
	public void addUnusedPermitTiles(Tile permitTile) {
		this.unusedPermitTiles.add(permitTile);
	}

	/**
	 * @return the number of the UNUSED permit tiles of the player
	 */
	public int getNumberOfPermitTile() {
		return this.unusedPermitTiles.size();
	}

	/**
	 * This method adds the specified coins to the coins owned by the player
	 */
	public void addCoins(int coins) {
		this.coins += coins;
	}

	/**
	 * This method removes the specified quantity of coins from the owned coins
	 * of the player
	 */
	public void removeCoins(int coins) {
		this.coins -= coins;
	}

	/**
	 * @return set the initial coins of one player
	 */
	public int setInitialCoins(int turnNumber) {
		return INITIAL_COINS + turnNumber;
	}

	/**
	 * @return the coins of the player
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
	 * This method adds an assistant to the owned assistants of the player
	 */
	public void addAssistant() {
		this.assistants++;
	}

	/**
	 * This method removes an assistant from the owned assistants of the player
	 */
	public void removeAssistant() {
		this.assistants--;
	}

	/**
	 * @return
	 */
	public int getNumberOfAssistants() {
		return this.assistants;
	}
	
	/**
	 * @return
	 */
	public String showPermitTileCards() {
		String permitTile="";
		int i=0;
		for(Tile tile:this.unusedPermitTiles){
			permitTile+=i+")"+" "+tile.toString()+"\n";
			i++;
		}
		return permitTile;
	}


}