package controller;

import model.Board;
import model.PermitTile;
import model.PermitTileDeck;
import model.PoliticCard;
import model.Region;

import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import exceptions.InvalidSlotException;

/**
 * Created by Gabriele on 22/05/16. This class represents the thread always
 * running while a match is on-going. It stores the core of the game engine.
 */
public class MatchHandler extends Thread {

	/**
	 * The ID of the match: IDs are assigned in a crescent way, starting from 0.
	 */
	private int id;

	/**
	 * The date of the launch of the match
	 */
	private Date date;

	/**
	 * A reference to the local GraphMap for this match.
	 */
	private Board board;

	/**
	 * An ArrayList of player in this MatchHandler.
	 */
	private ArrayList<Player> players; // To add UML scheme

	/**
	 * Number of player in this Match
	 */
	private int playersNumber; // To add UML scheme

	/**
	 * An boolean value used to know if the first player has decided the total
	 * number of player. It's true when he has finished to set the number else
	 * it's false.
	 */
	private boolean pending = false; // To add UML scheme

	/**
	 * MUST BE ADAPTED TO THE LAST VERSION OF MAP CONSTRUCTOR
	 */

	public void run() {

		pending = true; // Player has finished to set the match

		/*
		 * NEEDS REVISION: MUST INSERT THE NEW ATTRIBUTES: SEE MAP CONSTRUCTOR!
		 */

		// Aggiungi controllo per verificare se ArrayList è pieno di giocatori

		// Start the match
	}

	/**
	 * Default constructor
	 */

	public MatchHandler(int id, Date date, Connector connector) {
		this.players = new ArrayList<Player>();
		Player player = new Player(connector);
		this.players.add(player);
		this.id = id;
		this.date = date;
	}

	/**
	 * This method is invoked to setup the map before a match starts. The
	 * parameters are set by the first player that joins the match.
	 * 
	 * @param numberOfPlayers
	 *            the number of players of the match
	 * @param linksBetweenCities
	 *            the number of MAXIMUM connections between the cities; in
	 *            detail, this number represents the maximum number of streets
	 *            that come out from each city (vertex)
	 */
	public void boardSetup(int numberOfPlayers, int rewardTokenBonusNumber, int permitTileBonusNumber,
			int nobilityTrackBonusNumber, int linksBetweenCities) {
		board = new Board(numberOfPlayers, rewardTokenBonusNumber, permitTileBonusNumber, nobilityTrackBonusNumber,
				linksBetweenCities);
	}

	public void startGame() {// To add UML scheme
		Player player;
		for (int i = 0; i < this.players.size(); i++) {

		}

	}

	public String toString() {
		String string = "";
		string += "Match numero " + this.id + "\nLanciato in data: ";
		DateFormat dateFormat = new SimpleDateFormat();
		string += dateFormat.format(date) + "\n";

		// Needs more implementation: the current status of the match should be
		// displayed
		return string;
	}

	public boolean isPending() {
		return this.pending;
	}

	public void buyPermitTile(Player player, String regionName) {
		Region[] regions = this.board.getRegions();
		boolean flag = false;
		int playerPayment;
		int numberOfCouncillorSatisfied;
		PermitTileDeck regionDeck;
		int i;
		for (i = 0; i < 3 && flag == false; i++) {
			if (regions[i].getName() == regionName)
				flag = true;
		}
		ArrayList<PoliticCard> cardsChoseForCouncilSatisfaction = player.cardsToCouncilSatisfaction();
		numberOfCouncillorSatisfied = regions[i].numberOfCouncilSatisfied(cardsChoseForCouncilSatisfaction);
		CoinsManager coinsManager = new CoinsManager();
		Scanner input = new Scanner(System.in);
		if (numberOfCouncillorSatisfied > 0) {
			System.out.println("Puoi soddisfare il consiglio con " + numberOfCouncillorSatisfied + " carte!");
			playerPayment = coinsManager.paymentForPermitTile(numberOfCouncillorSatisfied);
			player.applyPayment(playerPayment);
			player.removeCardsFromHand(cardsChoseForCouncilSatisfaction);
			regionDeck = regions[i].getDeck();
			System.out.println("Quale slot vuoi scegliere 1 o 2?");
			int Slot = input.nextInt();
			try {
				player.setUnusedPermitTiles(regionDeck.drawPermitTile(Slot));
			} catch (InvalidSlotException e) {

				e.printStackTrace();
			}
			
		}else
		System.out.println("Non ci sono consiglieri con questo colore");
	}

	public Connector getPlayerConnector(int numPlayer) {// To add UML scheme
		Player player = players.get(numPlayer);
		return player.getConnector();
	}

	public void addPlayer(Connector connector) {// To add UML scheme
		Player player = new Player(connector);
		this.players.add(player);
		if (isFull())
			this.startGame();
	}

	public boolean isFull() {
		if (this.players.size() < this.playersNumber)
			return false;
		else
			return true;
	}

}