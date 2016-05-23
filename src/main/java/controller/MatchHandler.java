package controller;

import model.Map;

import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


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
	private Map map;

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
	 * Default constructor
	 */

	public void run() {
		String receiveFromClient;
		int numberOfPlayers;
		Connector playerOneConnector=this.players.get(0).getConnector(); //PlayerOne is the creator of this match and for this reason i use him Connector
		playerOneConnector.writeToClient(
				"Inserisci il numero di giocatori massimo per questa partita.\n Puoi inserire un valore massimo di 8 giocatori.");
		numberOfPlayers = playerOneConnector.receiveIntFromClient();
		// Player has to add a correct number between 2 and 8
		while (numberOfPlayers < 2 || numberOfPlayers > 8) {
			playerOneConnector.writeToClient("ATTENZIONE!\n Devi inserire un numero compreso tra 2 e 8.");
			numberOfPlayers = playerOneConnector.receiveIntFromClient();
		}

		int linksBetweenCities;
		playerOneConnector.writeToClient("Inserisci il numero massimo di collegamenti tra le citta");
		linksBetweenCities = playerOneConnector.receiveIntFromClient();
		// Player has to add a correct number between x and y
		while (linksBetweenCities < 2 || linksBetweenCities > 4) {
			playerOneConnector.writeToClient("ATTENZIONE!\n Devi inserire un numero compreso tra X e Y.");
			linksBetweenCities = playerOneConnector.receiveIntFromClient();
		}

		int bonusNumber;
		playerOneConnector.writeToClient("Inserisci il numero di bonus.");
		bonusNumber = playerOneConnector.receiveIntFromClient();
		while (bonusNumber < 1 || bonusNumber > 3) {
			playerOneConnector.writeToClient("ATTENZIONE!\n Devi inserire un numero compreso tra X e Y.");
			bonusNumber = playerOneConnector.receiveIntFromClient();
		}
		
		pending=true; //Player has finished to set the match

		mapSetup(numberOfPlayers, linksBetweenCities, bonusNumber);
		
		//Aggiungi controllo per verificare se ArrayList è pieno di giocatori 
		
		//Start the match
	}
		
	/**
	 * Default constructor
	 */

	public MatchHandler(int id, Date date, Connector connector) {
		this.players=new ArrayList<Player>();
		Player player= new Player(connector);
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
	public void mapSetup(int numberOfPlayers, int linksBetweenCities, int bonusNumber) {
		map = new Map(numberOfPlayers, bonusNumber,linksBetweenCities);
	}

	public void startGame() {//To add UML scheme
		Player player;
		for(int i=0;i<this.players.size();i++){
			
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

	public Connector getPlayerConnector(int numPlayer) {//To add UML scheme
		Player player=players.get(numPlayer);
		return player.getConnector();
	}
	
	public void addPlayer(Connector connector) {//To add UML scheme
		Player player= new Player(connector);
		this.players.add(player);
		if(isFull())
			this.startGame();
	}


	public boolean isFull() {
		if(this.players.size()<this.playersNumber)
		return false;
		else
		return true;
	}
	
}