package controller;

import model.Map;

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
	private ArrayList<String> players; // To add UML scheme

	/**
	 * Number of player in this Match
	 */
	private int playersNumber; // To add UML scheme

	/**
	 * An ArrayList of player connector.
	 */
	private ArrayList<Connector> connectors; // To add UML scheme

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
		String reciveFromClient;
		int numberOfPlayers;
		connectors.get(1).writeToClient(
				"Inserisci il numero di giocatori massimo per questa partita.\n Puoi inserire un valore massimo di 8 giocatori.");
		numberOfPlayers = connectors.get(1).reciveIntFromClient();
		// Player has to add a correct number between 2 and 8
		while (numberOfPlayers < 2 || numberOfPlayers > 8) {
			connectors.get(1).writeToClient("ATTENZIONE!\n Devi inserire un numero compreso tra 2 e 8.");
			numberOfPlayers = connectors.get(1).reciveIntFromClient();
		}

		int linksBetweenCities;
		connectors.get(1).writeToClient("Inserisci il numero massimo di collegamenti tra le citta");
		linksBetweenCities = connectors.get(1).reciveIntFromClient();
		// Player has to add a correct number between x and y
		while (linksBetweenCities < X || linksBetweenCities > Y) {
			connectors.get(1).writeToClient("ATTENZIONE!\n Devi inserire un numero compreso tra X e Y.");
			linksBetweenCities = connectors.get(1).reciveIntFromClient();
		}

		int bonusNumber;
		connectors.get(1).writeToClient("Inserisci il numero di bonus.");
		bonusNumber = connectors.get(1).reciveIntFromClient();
		while (bonusNumber < X || bonusNumber > Y) {
			connectors.get(1).writeToClient("ATTENZIONE!\n Devi inserire un numero compreso tra X e Y.");
			bonusNumber = connectors.get(1).reciveIntFromClient();
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
		this.connectors = new ArrayList<Connector>();
		this.id = id;
		this.date = date;
		this.connectors.add(connector);

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
		map = new Map(numberOfPlayers, bonusNumber);
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

	public void addPlayer(String userId) {
		this.players.add(UserId);		
	}

	public boolean isNotFull() {
		return false;
	}
	
	
}