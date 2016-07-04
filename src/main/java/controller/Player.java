package controller;

import model.*;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import exceptions.CardNotFoundException;
import exceptions.TileNotFoundException;
import exceptions.UnsufficientCoinsException;

/**
 * 
 */
public class Player implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	private int mainActionsLeft;

	/**
	 * 
	 */
	private boolean hasPerformedQuickAction;

	/**
	 *
	 */
	private boolean disconnected;

	/**
	 * Default constructor
	 */
	public Player(ClientSideConnectorInt connector, int id, String nickName) {
		mainActionsLeft = 1;
		hasPerformedQuickAction = false;
		Random random = new Random();
		this.id = id;
		this.nickName = nickName;
		this.connector = connector;
		this.coins = INITIAL_COINS + id;
		this.assistants = INITIAL_ASSISTANT;
		this.usedPermitTiles = new ArrayList<Tile>();
		this.positionInNobilityTrack = INITIAL_POSITION;
		this.unusedPermitTiles = new ArrayList<Tile>();
		this.controlledCities = new ArrayList<City>();
		this.emporiums=10;
		this.connector = connector;
		initializeFirstHand();// Distributes the first hand of politic cards
		this.victoryPoints = INITIAL_POSITION;
		this.color = String.valueOf(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
		this.disconnected = false;
	}

	/**
	 * 	IMPORTANT! This constructor is realized only for test purposes.
	 */
	public Player(int id) {
		mainActionsLeft = 1;
		hasPerformedQuickAction = false;
		Random random = new Random();
		this.id = id;
		this.coins = INITIAL_COINS + id;
		this.assistants = INITIAL_ASSISTANT;
		this.usedPermitTiles = new ArrayList<Tile>();
		this.unusedPermitTiles = new ArrayList<Tile>();
		this.controlledCities = new ArrayList<City>();
		this.emporiums=10;
		initializeFirstHand();// Distributes the first hand of politic cards
		this.victoryPoints = 0;
		this.color = String.valueOf(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
		this.disconnected=false;
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
	
	public Tile getRandomUsedPermitTile() throws TileNotFoundException {
		if(usedPermitTiles.size()==0)
			throw new TileNotFoundException();
		Random random = new Random();
		return usedPermitTiles.get(random.nextInt(usedPermitTiles.size()));
	}
	
	public Tile getRandomUnusedPermitTile() throws TileNotFoundException {
		if(unusedPermitTiles.size()==0)
			throw new TileNotFoundException();
		Random random = new Random();
		return unusedPermitTiles.get(random.nextInt(unusedPermitTiles.size()));
	}

	/**
	 * @return
	 */
	public Tile getUnusedPermitTileFromId(int permitTileId) throws TileNotFoundException {
		PermitTile tempTile;
		for (int i = 0; i < this.unusedPermitTiles.size(); i++) {
			tempTile = (PermitTile) this.unusedPermitTiles.get(i);
			if (tempTile.getId() == permitTileId) {
				return this.unusedPermitTiles.get(i);
			}
		}
		throw new TileNotFoundException();
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

	public int getPositionInNobilityTrack() {
		return this.positionInNobilityTrack;
	}

	/**
	 * NEEDS REVISION: must implement exceptions and correct communication with
	 * the client.
	 * 
	 * @return
	 */
	/*
	 * public ArrayList<PoliticCard> cardsToCouncilSatisfaction() { int
	 * numberOfCardsUsed = 0; String colorCard = ""; boolean flagStopChoose =
	 * false; ArrayList<PoliticCard> cardsChose = new ArrayList<PoliticCard>();
	 * ArrayList<PoliticCard> tempHandCards = new
	 * ArrayList<PoliticCard>(this.politicCards); while (numberOfCardsUsed < 4
	 * && !flagStopChoose) { try { connector.writeToClient(
	 * "Write the color card that you would to use one by one and write 'stop' when you finished:"
	 * ); } catch (RemoteException e) { logger.log(Level.FINEST,
	 * "Error: couldn't write to client\n", e); } try { colorCard =
	 * connector.receiveStringFromClient(); } catch (RemoteException e) {
	 * logger.log(Level.FINEST, "Error: couldn't receive from client\n", e); }
	 * colorCard = colorCard.trim(); colorCard = colorCard.toUpperCase(); if
	 * (!colorCard.equals("STOP")) { while (!checkExistingColor(colorCard)) {
	 * try { connector.writeToClient(
	 * "You have entered an incorret color Card!1\nWrite the color card that yoy would to use:"
	 * ); } catch (RemoteException e) { logger.log(Level.FINEST,
	 * "Error: couldn't write to client\n", e); } try { colorCard =
	 * connector.receiveStringFromClient(); } catch (RemoteException e) {
	 * logger.log(Level.FINEST, "Error: couldn't receive from client\n", e); }
	 * colorCard = colorCard.trim(); colorCard = colorCard.toUpperCase(); }
	 * while (!checkIfYouOwnThisCard(colorCard, tempHandCards)) { try {
	 * connector.writeToClient(
	 * "You have entered an incorret color Card!1\nWrite the color card that yoy would to use:"
	 * ); } catch (RemoteException e) { logger.log(Level.FINEST,
	 * "Error: couldn't write to client\n", e); } try { colorCard =
	 * connector.receiveStringFromClient(); } catch (RemoteException e) {
	 * logger.log(Level.FINEST, "Error: couldn't receive from client\n", e); }
	 * colorCard = colorCard.trim(); colorCard = colorCard.toUpperCase(); }
	 * PoliticCard politicCard = new PoliticCard(colorCard);
	 * cardsChose.add(politicCard); numberOfCardsUsed++; try {
	 * connector.writeToClient("PERFECT!");
	 * 
	 * } catch (RemoteException e) { logger.log(Level.FINEST,
	 * "Error: couldn't write to client\n", e); } } else if
	 * (colorCard.equals("STOP") && cardsChose.size() == 0) try {
	 * connector.writeToClient("ERROR: You have to enter at least one card!!");
	 * 
	 * } catch (RemoteException e) { logger.log(Level.FINEST,
	 * "Error: couldn't write to client\n", e); } else flagStopChoose = true; }
	 * return cardsChose;
	 * 
	 * }
	 * 
	 * /** This method checks whether the specified color is a valid Politic
	 * Card color or not.
	 * 
	 * @return true if the color is correct, false otherwise
	 */

	/**
	 * Checks whether the player owns a PoliticCard of the specified color or
	 * not.
	 */
	public void checkIfYouOwnThisCard(String colorCard, ArrayList<PoliticCard> tempHandCards) throws CardNotFoundException {
		for (int i = 0; i < tempHandCards.size(); i++) {
			if (tempHandCards.get(i).getColorCard().equals(colorCard)) {
				return;
			}
		}
		throw new CardNotFoundException();
	}

	/**
	 * This method removes the specified PoliticCards from the hand of the
	 * player.
	 */
	public void removeCardsFromHand(ArrayList<String> cardsChose) {
		boolean cardFound;
		for (int i = 0; i < cardsChose.size(); i++) {
			cardFound = false;
			for (int j = 0; j < this.politicCards.size() && !cardFound; j++) {
				if (this.politicCards.get(j).getColorCard().equals(cardsChose.get(i))) {
					this.politicCards.remove(j);
					cardFound = true;
				}
			}
		}
	}

	/**
	 * This method is invoked when a player decides to sell a Politic Card in
	 * the Market. It removes the chosen card from the arraylist of owned
	 * politic cards.
	 * 
	 * @param color
	 *            the color of the chosen Politic Card to sell
	 * @return The politic card to sell
	 */
	public PoliticCard sellPoliticCard(String color) {
		for (int i = 0; i < this.politicCards.size(); i++) {
			if (politicCards.get(i).getColorCard().equals(color)) {
				return politicCards.remove(i);
			}
		}
		return null;
	}

	/**
	 * This method is invoked when a player decides to sell a Permit Tile in the
	 * Market. It removes the chosen tile from the arraylist of unused permit
	 * tiles.
	 * 
	 * @param id
	 *            the id of the permit tile to sell
	 * @return The permit tile to sell
	 */
	public Tile sellPermitTile(int id) throws TileNotFoundException {
		PermitTile tile;
		for (int i = 0; i < unusedPermitTiles.size(); i++) {
			tile = (PermitTile) unusedPermitTiles.get(i);
			if (tile.getId() == id) {
				return unusedPermitTiles.remove(i);
			}
		}
		throw new TileNotFoundException();
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
	public void performPayment(int payment) throws UnsufficientCoinsException {
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
	public int getNumberOfUsedPermitTile() {
		return this.usedPermitTiles.size();
	}

	/**
	 * This method adds the specified coins to the coins owned by the player
	 */
	public void addCoins(int coins) {
		if (this.coins + coins > MAXIMUN_COINS)
			this.coins = MAXIMUN_COINS;
		else
			this.coins += coins;
	}

	/**
	 * 
	 */
	public void changePositionInNobilityTrack(int position) {
		if (this.positionInNobilityTrack + position > MAXIMUN_POSITION)
			this.positionInNobilityTrack = MAXIMUN_POSITION;
		else
			this.positionInNobilityTrack += position;
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
	public int getNumberOfControlledCities() {
		return this.controlledCities.size();
	}

	/**
	 * 
	 */
	public City getSingleControlledCity(int choice) {
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
		this.assistants += number;
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
	public void fromUnusedToUsedPermitTile(PermitTile permitTile) {
		this.unusedPermitTiles.remove(permitTile);
		this.usedPermitTiles.add(permitTile);
	}
	
	public void hasBuiltAnEmporium() {
		this.emporiums--;
	}

	/**
	 * @return
	 */
	public boolean hasBuiltLastEmporium() {
		return this.emporiums == 0;
	}

	/**
	 * to string method
	 * 
	 * @return String of player attriute
	 */
	public String toString() {
		String allPlayerInformation = "";
		allPlayerInformation += "Your ID: " + id + "\nYour color: " + color + "\nYour Coins: " + coins
				+ "\nYour Assistance: " + assistants + "\nYour position in Victory Track: " + victoryPoints
				+ "\nYour position in Nobility Track:   " + positionInNobilityTrack + "\n";
		allPlayerInformation += "\nYour cities:\n";
		for (City tempCity : controlledCities)
			allPlayerInformation += tempCity.getName() + " ";
		allPlayerInformation += "\nYour POLITIC cards:\n";
		for (PoliticCard tempPoliticCard : politicCards)
			allPlayerInformation += tempPoliticCard.getColorCard() + " ";
		allPlayerInformation += "\nYour UNUSED Permit Tiles:\n";
		for (Tile tempPermitTile : unusedPermitTiles)
			allPlayerInformation += tempPermitTile.toString() + "\n";
		allPlayerInformation += "\nYour USED Permit Tiles:\n";
		for (Tile tempPermitTile : usedPermitTiles)
			allPlayerInformation += tempPermitTile.toString() + "\n";
		return allPlayerInformation;

	}

	public void setPlayerNickName(String nickName) {
		this.nickName = nickName;
	}

	public void mainActionDone(boolean value) {
		if (value)
			mainActionsLeft--;
		else 
			mainActionsLeft++;
	}

	public boolean hasPerformedMainAction() {
		return mainActionsLeft==0;
	}

	public boolean hasPerformedQuickAction() {
		return this.hasPerformedQuickAction;
	}

	public void quickActionDone() {
		hasPerformedQuickAction = true;
	}

	public void resetTurn() {
		mainActionsLeft = 1;
		hasPerformedQuickAction = false;
	}

	public void setPlayerOffline() {
		this.disconnected = true;
	}

	public boolean playerIsOffline() {
		return this.disconnected;
	}

	public void addVictoryPoints(int points) {
		this.victoryPoints += points;
	}
}