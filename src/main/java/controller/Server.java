package controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class initializes the game engine and the connection among the Clients.
 */
public class Server {

	/**
	 * The IP Address of the Server, that means the address that all Clients
	 * must know to connect to the game.
	 */
	private static final String ip="localhost";

	/**
	 * The port of the specified IP Address (connection parameter).
	 */
	private static final int port=80;
	
	/**
	 * This attribute is the counter for the Match IDs, starting from 0 and incrementing for each new match.
	 */
	private int matchCounter;

	/**
	 * THIS ATTRIBUTE NEEDS REVISION!
	 */
	private ArrayList<Player> registeredUsers;
	
	/**
	 * The Server stores an array of currently on-going matches through their MatchHandlers
	 */
	private ArrayList<MatchHandler> matches;

	/**
	 * Default constructor
	 */
	public Server() {
		this.matchCounter=0;
	}

	/**
	 * NEEDS REVISION! This method initializes a new match by making its MatchHandler run.
	 */
	public void launchNewMatch() {
		Date date = new Date();
		MatchHandler matchHandler=new MatchHandler(matchCounter, date);
		matches.add(matchHandler);
		matchHandler.run();
		this.matchCounter++;
	}
	
	/**
	 * NEEDS IMPLEMENTATION! This method allows a Client to join a currently on-going match, if this is not full. 
	 */
	public void joinMatch() {
		
	}
	
	/**
	 * NEEDS IMPLEMENTATION! This method allows to check whether a match is currently full or not
	 * @return True if the specified match is full, false otherwise.
	 */
	public boolean checkIfMatchIsFull(MatchHandler match) {
		return true; //just for example 
	}
	

}