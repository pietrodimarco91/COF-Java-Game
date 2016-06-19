package controller;

import model.*;

import java.awt.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.UnsufficientCoinsException;

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
	private String nickName;

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
	private int coins;
	/**
	 *
	 */
	private int assistants;
	/**
	 *
	 */
	private int id;
	
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
	private ClientSideConnectorInt connector;// To add UML scheme

	/**
	 *
	 */
	private boolean hasPerformedMainAction,hasPerformedQuickAction;

	/**
	 * Default constructor
	 */
	public Player(ClientSideConnectorInt connector, int id,String nickName) {
		hasPerformedMainAction=false;
		hasPerformedQuickAction=false;
		Random random = new Random();
		this.id = id;
		this.nickName=nickName;
		this.connector=connector;
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
		this.id = id;
		this.coins = INITIAL_COINS + id;
		this.assistants = INITIAL_ASSISTANT;
		this.usedPermitTiles = new ArrayList<Tile>();
		this.unusedPermitTiles = new ArrayList<Tile>();
		this.controlledCities = new ArrayList<City>();
		initializeFirstHand();// Distributes the first hand of politic cards
		this.victoryPoints = 0;
		this.color = String.valueOf(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
	}
	
	public String getNickName() {
		return this.nickName;
	}
	
	public int getId() {
		return this.id;
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
	public PermitTile getUnusedPermitTileFromId(int permitTileId) {
		boolean find=false;
		PermitTile tempTile=null;
		for(int i=0;i<this.unusedPermitTiles.size() && !find;i++){
			tempTile=(PermitTile)this.unusedPermitTiles.get(i);
			if(tempTile.getId()==permitTileId)
				find=true;
	}
			return tempTile;
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
	public ClientSideConnectorInt getConnector() { // Da aggiungere UML
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
	/*public ArrayList<PoliticCard> cardsToCouncilSatisfaction() {
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
	public void removeCardsFromHand(ArrayList<String> cardsChose) {
		boolean cardFound;
		int j;
		for (int i = 0; i < cardsChose.size(); i++) {
			cardFound = false;
			for (j = 0; j < this.politicCards.size() && !cardFound; j++) {
				if (this.politicCards.get(j).getColorCard().equals(cardsChose.get(i))){
					this.politicCards.remove(j);
				    cardFound = true;
				}
			}
		}
	}
	
	/**
	 * This method is invoked when a player decides to sell a Politic Card in the Market.
	 * It removes the chosen card from the arraylist of owned politic cards.
	 * @param color the color of the chosen Politic Card to sell
	 * @return The politic card to sell
	 */
	public PoliticCard sellPoliticCard(String color) {
		for(int i=0;i<this.politicCards.size();i++) {
			if(politicCards.get(i).getColorCard().equals(color)) {
				return politicCards.remove(i);
			}
		}
		return null;
	}
	
	/**
	 * This method is invoked when a player decides to sell a Permit Tile in the Market.
	 * It removes the chosen tile from the arraylist of unused permit tiles.
	 * @param id the id of the permit tile to sell
	 * @return The permit tile to sell
	 */
	public Tile sellPermitTile(int id) {
		PermitTile tile;
		for(int i=0;i<unusedPermitTiles.size();i++) {
			tile=(PermitTile)unusedPermitTiles.get(i);
			if(tile.getId()==id) {
				return unusedPermitTiles.remove(i);
			}
		}
		return null;
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
	public void performPayment(int payment) throws UnsufficientCoinsException{
		if ((this.coins - payment) >= 0) {
			this.coins -= payment;
		} else
			throw new UnsufficientCoinsException();
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
	
	/**
	 * to string method
	 * @return String of player attriute
	 */
	public String toString(){
		String allPlayerInformation="";
		allPlayerInformation+="Your ID: "+id+" Your color: "+color+" Your Coins: "+coins+"\n Your Assistance: "+assistants+" Your position in Victory Points: "+victoryPoints+
				"Your position in Nobility Track:   "+positionInNobilityTrack+" Your position in Victory Points: "+victoryPoints;
		allPlayerInformation+=" Your cities: ";
		for(City tempCity: controlledCities)
			allPlayerInformation+=tempCity.getName()+" ";
			allPlayerInformation+="\n Your POLITIC card: ";
			for(PoliticCard tempPoliticCard: politicCards)
				allPlayerInformation+=tempPoliticCard.getColorCard()+" ";
			allPlayerInformation+="\n Your UNUSED permit tile: ";
			for(Tile tempPermitTile: unusedPermitTiles)
				allPlayerInformation+=tempPermitTile.toString()+" ";
			allPlayerInformation+="\n Your USED permit tile: ";
			for(Tile tempPermitTile: usedPermitTiles)
				allPlayerInformation+=tempPermitTile.toString()+" ";
			return allPlayerInformation;
		
				
	}

	public void setPlayerNickName(String nickName) {
		this.nickName=nickName;
	}

	public void mainActionDone(){
		hasPerformedMainAction=true;
	}
	public void quickActionDone(){
		hasPerformedQuickAction=true;
	}

	public void resetTurn(){
		hasPerformedMainAction=false;
		hasPerformedQuickAction=false;
	}
	
}