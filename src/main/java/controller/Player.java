package controller;

import model.*;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 */
public class Player {
	/**
	 * 
	 */
	private static final Logger logger = Logger.getLogger(Player.class.getName());

	/**
	 *
	 */
	private final static int INITIAL_COINS = 10;
	/**
	 *
	 */
	private final static int INITIAL_ASSISTANT = 3;
	/**
	 *
	 */
	private final static int MAXIMUN_POSITION = 20;
	/**
	 *
	 */
	private final static int MAXIMUN_COINS = 20;
	/**
	 *
	 */
	private final static int INITIAL_POSITION = 0;
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
	private int positionInNobilityTrack;
	
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
	private ConnectorInt connector;// To add UML scheme

	/**
	 * Default constructor
	 */
	public Player(ConnectorInt connector, int id) {
		Random random = new Random();
		this.turnNumber = id;
		this.coins = INITIAL_COINS + id;
		this.assistants = INITIAL_ASSISTANT;
		this.usedPermitTiles = new ArrayList<Tile>();
		this.positionInNobilityTrack=INITIAL_POSITION;
		this.unusedPermitTiles = new ArrayList<Tile>();
		this.controlledCities = new ArrayList<City>();
		this.connector = connector;
		initializeFirstHand();// Distributes the first hand of politic cards
		this.victoryPoints = INITIAL_POSITION;
		this.color = String.valueOf(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
	}

	/**
	 * This constructor is realized only for test purposes.
	 */
	public Player(int id) {
		Random random = new Random();
		this.turnNumber = id;
		this.coins = INITIAL_COINS + id;
		this.assistants = INITIAL_ASSISTANT;
		this.usedPermitTiles = new ArrayList<Tile>();
		this.unusedPermitTiles = new ArrayList<Tile>();
		this.controlledCities = new ArrayList<City>();
		initializeFirstHand();// Distributes the first hand of politic cards
		this.victoryPoints = 0;
		this.color = String.valueOf(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
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
	public Tile getUnusedPermitTile(int choice) {// metodo da risistemare e da
													// aggiungere UML
		return this.unusedPermitTiles.get(choice);
	}
	/**
	 * 
	 */
	public Tile getUsedPermitTile(int choiche){
		return this.getUsedPermitTile(choiche);
	}

	/**
	 * @return
	 */
	public ConnectorInt getConnector() { // Da aggiungere UML
		return this.connector;
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
		String colorCard = "";
		boolean flagStopChoose = false;
		ArrayList<PoliticCard> cardsChose = new ArrayList<PoliticCard>();
		ArrayList<PoliticCard> tempHandCards = new ArrayList<PoliticCard>(this.politicCards);
		while (numberOfCardsUsed < 4 && !flagStopChoose) {
			try {
				connector.writeToClient(
						"Write the color card that you would to use one by one and write 'stop' when you finished:");
			} catch (RemoteException e) {
				logger.log(Level.FINEST, "Error: couldn't write to client\n", e);
			}
			try {
				colorCard = connector.receiveStringFromClient();
			} catch (RemoteException e) {
				logger.log(Level.FINEST, "Error: couldn't receive from client\n", e);
			}
			colorCard = colorCard.trim();
			colorCard = colorCard.toUpperCase();
			if (!colorCard.equals("STOP")) {
				while (!checkExistingColor(colorCard)) {
					try {
						connector.writeToClient(
								"You have entered an incorret color Card!1\nWrite the color card that yoy would to use:");
					} catch (RemoteException e) {
						logger.log(Level.FINEST, "Error: couldn't write to client\n", e);
					}
					try {
						colorCard = connector.receiveStringFromClient();
					} catch (RemoteException e) {
						logger.log(Level.FINEST, "Error: couldn't receive from client\n", e);
					}
					colorCard = colorCard.trim();
					colorCard = colorCard.toUpperCase();
				}
				while (!checkIfYouOwnThisCard(colorCard, tempHandCards)) {
					try {
						connector.writeToClient(
								"You have entered an incorret color Card!1\nWrite the color card that yoy would to use:");
					} catch (RemoteException e) {
						logger.log(Level.FINEST, "Error: couldn't write to client\n", e);
					}
					try {
						colorCard = connector.receiveStringFromClient();
					} catch (RemoteException e) {
						logger.log(Level.FINEST, "Error: couldn't receive from client\n", e);
					}
					colorCard = colorCard.trim();
					colorCard = colorCard.toUpperCase();
				}
				PoliticCard politicCard = new PoliticCard(colorCard);
				cardsChose.add(politicCard);
				numberOfCardsUsed++;
				try {
					connector.writeToClient("PERFECT!");

				} catch (RemoteException e) {
					logger.log(Level.FINEST, "Error: couldn't write to client\n", e);
				}
			} else if (colorCard.equals("STOP") && cardsChose.size() == 0)
				try {
					connector.writeToClient("ERROR: You have to enter at least one card!!");

				} catch (RemoteException e) {
					logger.log(Level.FINEST, "Error: couldn't write to client\n", e);
				}
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
	 * This method removes the specified PoliticCards from the hand of the
	 * player.
	 */
	public void removeCardsFromHand(ArrayList<PoliticCard> cardsChose) {
		boolean cardFound;
		int j;
		for (int i = 0; i < cardsChose.size(); i++) {
			cardFound = false;
			for (j = 0; j < this.politicCards.size() && !cardFound; j++) {
				if (this.politicCards.get(j).getColorCard().equals(cardsChose.get(i).getColorCard()))
					this.politicCards.remove(j);
				cardFound = true;
			}
		}
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
	 * 
	 */
	public int getNumberOfUsedPermitTile(){
		return this.usedPermitTiles.size();
	}
	
	
	/**
	 * This method adds the specified coins to the coins owned by the player
	 */
	public void addCoins(int coins) {
		if(this.coins+coins>MAXIMUN_COINS)
			this.coins=MAXIMUN_COINS;
		else
			this.coins+=coins;
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
	 * 
	 */
	public void changePositionInNobilityTrack(int position){
		if(this.positionInNobilityTrack+position>MAXIMUN_POSITION)
		this.positionInNobilityTrack=MAXIMUN_POSITION;
		else
			this.positionInNobilityTrack+=position;
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
	public int getNumberOfEmporium() {
		return this.emporiums;
	}
	
	/**
	 * 
	 */
	public int getNumberoOfControlledCities(){
		return this.controlledCities.size();
	}
	
	/**
	 * 
	 */
	public City getSingleControlledCity(int choice){
		return this.controlledCities.get(choice);
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
	 * 
	 */
	public void addMoreAssistant(int number) {
		this.assistants+=number;
	}

	/**
	 * This method removes an assistant from the owned assistants of the player
	 */
	public void removeAssistant() {
		this.assistants--;
	}

	/**
	 * This method removes an assistant from the owned assistants of the player
	 */
	public void removeMoreAssistants(int numberOfAssistants) {
		this.assistants -= numberOfAssistants;
	}
	/**
	 * 
	 */
	public void setVoctoryPoints(int points){
		this.victoryPoints+=points;
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
	public int getVictoryPoints() {
		return this.victoryPoints;
	}

	/**
	 * @return
	 */
	public String showPermitTileCards() {
		String permitTile = "";
		if (this.unusedPermitTiles.size() == 0)
			permitTile += "You don't have got any Permit Tile unused";
		else {
			int i = 0;
			for (Tile tile : this.unusedPermitTiles) {
				permitTile += i + ")" + " " + tile.toString() + "\n";
				i++;
			}
		}
		return permitTile;
	}

	/**
	 * @return
	 */
	public String showUsedPermitTileCards() {
		String permitTile = "";
		if (this.usedPermitTiles.size() == 0)
			permitTile += "You don't have got any Permit Tile used";
		else {
			int i = 0;
			for (Tile tile : this.usedPermitTiles) {
				permitTile += i + ")" + " " + tile.toString() + "\n";
				i++;
			}
		}
		return permitTile;
	}

	/**
	 * @return
	 */
	public String showPoliticCards() {
		String politicCards = "";
		if (this.politicCards.size() == 0)
			politicCards += "You don't have got any Politic Card";
		else {
			for (PoliticCard tempPoliticCard : this.politicCards)
				politicCards += tempPoliticCard.getColorCard() + " ";
		}
		return politicCards;
	}

	/**
	 * @return
	 */
	public void fromUnusedToUsedPermitTile(Player player, PermitTile permitTile) {
		this.unusedPermitTiles.remove(permitTile);
		this.usedPermitTiles.add(permitTile);
	}

	/**
	 * @return
	 */
	public boolean hasBuiltLastEmporium() {
		return this.emporiums==0;
	}

}