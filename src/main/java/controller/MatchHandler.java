package controller;

import exceptions.ConfigAlreadyExistingException;
import exceptions.CouncillorNotFoundException;
import exceptions.InvalidInputException;
import exceptions.InvalidSlotException;
import model.*;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Gabriele on 22/05/16. This class represents the thread always
 * running while a match is on-going. It stores the core of the game engine.
 */
public class MatchHandler extends Thread {

	private static final Logger logger = Logger.getLogger(MatchHandler.class.getName());
	/**
	 * The ID of the match: IDs are assigned in a crescent way, starting from 0.
	 */
	private int id;

	/**
	 * The date of the launch of the match
	 */
	private Date date;

	/**
	 * A reference to the local Board for this match.
	 */
	private Board board;

	/**
	 * An ArrayList of players in this MatchHandler.
	 */
	private ArrayList<Player> players; // To add UML scheme

	/**
	 * Number of players in this Match
	 */
	private int numberOfPlayers; // To add UML scheme

	/**
	 * A boolean value used to know if the first player has decided the total
	 * number of players. It's true when he has finished to set the number false
	 * otherwise
	 */
	private boolean pending; // To add UML scheme

	private ConfigFileManager configFileManager;

	/**
	 * Default constructor
	 */

	public MatchHandler(int id, Date date, ConnectorInt connectorInt) {
		this.players = new ArrayList<Player>();
		Player player = new Player(connectorInt);
		this.players.add(player);
		this.id = id;
		this.date = date;
		this.configFileManager = new ConfigFileManager();
		this.pending = false;
	}

	/**
	 * MUST BE ADAPTED TO THE LAST VERSION OF MAP CONSTRUCTOR
	 */

	public void run() {
		boardConfiguration(players.get(0));
		pending = true; // Player has finished to set the match

		/*
		 * NEEDS REVISION: MUST INSERT THE NEW ATTRIBUTES: SEE MAP CONSTRUCTOR!
		 */

		// Aggiungi controllo per verificare se ArrayList è pieno di giocatori

		// Start the match
	}

	public void boardConfiguration(Player player) {
		boolean correctAnswer = false;
		int choice = 0;
		ConnectorInt playerConnector = player.getConnector();
		try {
			playerConnector.writeToClient("BOARD CONFIGURATION:\n");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: could't write to client", e);
		}
		while (!correctAnswer) {
			try {
				playerConnector
						.writeToClient("1) Create a new board configuration\n2) Choose an existing configuration\n");
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: could't write to client", e);
			}
			try {
				choice = playerConnector.receiveIntFromClient();
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: could't receive from client", e);
			}
			if (choice != 0) {
				if (choice != 1 && choice != 2) {
					try {
						playerConnector.writeToClient("ERROR: incorrect input. Please retry\n");
					} catch (RemoteException e) {
						logger.log(Level.INFO, "Error: could't write to client", e);
					}
				} else
					correctAnswer = true;
			}
		}

		if (choice == 1) {
			newConfiguration(playerConnector);
		} else {
			if (configFileManager.getConfigurations().size() > 0) {
				try {
					playerConnector.writeToClient("These are the currently existing configurations:\n");
					ArrayList<ConfigObject> configurations = configFileManager.getConfigurations();
					for(ConfigObject configuration: configurations) {
						playerConnector.writeToClient(configuration.toString());
					}
					
					//must add configuration choice from client
					
				} catch (RemoteException e) {
					logger.log(Level.INFO, "Error: could't write to client", e);
				}
			} else {
				try {
					playerConnector
							.writeToClient("There aren't any configurations yet! Please create a new one :-) \n");
					newConfiguration(playerConnector);

				} catch (RemoteException e) {
					logger.log(Level.INFO, "Error: could't write to client", e);
				}
			}
		}
	}

	/**
	 * INCOMPLETE IMPLEMENTATION!
	 * 
	 * @param playerConnector
	 */
	public void newConfiguration(ConnectorInt playerConnector) {
		String parameters = "";
		int numberOfPlayers = 0, linksBetweenCities = 0, rewardTokenBonusNumber = 0, permitTileBonusNumber = 0,
				nobilityTrackBonusNumber = 0;
		boolean stop = false;
		try {
			playerConnector.writeToClient(
					"NEW CONFIGURATION:\nInsert the configuration parameters in this order, and each number must be separated by a space");
			playerConnector.writeToClient(
					"Maximum number of players, Reward Token bonus number, Permit Tiles bonus number, Nobility Track bonus number, Maximum number of outgoing connections from each City");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: could't write to client", e);
		}
		try {
			parameters = playerConnector.receiveStringFromClient();
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: could't receive from client", e);
		}

		while (!stop) {

			// now we received all parameters

			try {
				configFileManager.createConfiguration(numberOfPlayers, rewardTokenBonusNumber, permitTileBonusNumber,
						nobilityTrackBonusNumber, linksBetweenCities);
				parametersValidation(numberOfPlayers, rewardTokenBonusNumber, permitTileBonusNumber,
						nobilityTrackBonusNumber, linksBetweenCities);
				stop = true;
			} catch (InvalidInputException e) {
				try {
					playerConnector.writeToClient(e.printError());
				} catch (RemoteException e1) {
					logger.log(Level.INFO, "Error: could't write to client", e1);
				}
			} catch (ConfigAlreadyExistingException e) {
				try {
					playerConnector.writeToClient(e.printError());
				} catch (RemoteException e1) {
					logger.log(Level.INFO, "Error: could't write to client", e1);
				}
			}
		}
	}

	/**
	 * Checks whether the specified parameters respect the rules or not.
	 * 
	 * @param numberOfPlayers
	 * @param rewardTokenBonusNumber
	 * @param permitTileBonusNumber
	 * @param nobilityTrackBonusNumber
	 * @param linksBetweenCities
	 * @throws InvalidInputException
	 */
	public void parametersValidation(int numberOfPlayers, int rewardTokenBonusNumber, int permitTileBonusNumber,
			int nobilityTrackBonusNumber, int linksBetweenCities) throws InvalidInputException {
		if (numberOfPlayers < 2 || numberOfPlayers > 8)
			throw new InvalidInputException();
		if ((rewardTokenBonusNumber < 1 || rewardTokenBonusNumber > 3)
				&& (permitTileBonusNumber < 1 || permitTileBonusNumber > 3)
				&& (nobilityTrackBonusNumber < 1 || nobilityTrackBonusNumber > 3))
			throw new InvalidInputException();
		if (linksBetweenCities < 2 && linksBetweenCities > 4)
			throw new InvalidInputException();
	}

	/**
	 * This method is invoked to setup the map before a match starts. The
	 * parameters are set by the first player that joins the match.
	 * 
	 * @param numberOfPlayers
	 *            the number of players of the match
	 * @param linksBetweenCities
	 *            the number of MAXIMUM outgoing connections from each city
	 */
	public void boardSetup(int numberOfPlayers, int rewardTokenBonusNumber, int permitTileBonusNumber,
			int nobilityTrackBonusNumber, int linksBetweenCities) {
		board = new Board(numberOfPlayers, rewardTokenBonusNumber, permitTileBonusNumber, nobilityTrackBonusNumber,
				linksBetweenCities);
	}

	/**
	 * NEEDS IMPLEMENTATION
	 */
	public void startGame() {// To add UML scheme
		Player player;
		for (int i = 0; i < this.players.size(); i++) {

		}

	}

	/**
	 * INCOMPLETE IMPLEMENTATION
	 */
	public String toString() {
		String string = "";
		string += "Match numero " + this.id + "\n";
		string += "Lanciato in data: ";
		DateFormat dateFormat = new SimpleDateFormat();
		string += dateFormat.format(date) + "\n";
		return string;
	}

	/**
	 * This method allows to know whether the current match is pending or not
	 * 
	 * @return true is it currently pending, false otherwise
	 */
	public boolean isPending() {
		return this.pending;
	}

	/**
	 * NEEDS REVISION AFTER IMPLEMENTATION. Especially the try/catch and the
	 * exceptions.
	 * 
	 * @param player
	 * @param regionName
	 */
	public void buyPermitTile(Player player, String regionName) {
		int playerPayment;
		int numberOfCouncillorSatisfied;
		PermitTileDeck regionDeck;
		Region region = this.getRegion(regionName);
		region = this.getRegion(regionName);
		ArrayList<PoliticCard> cardsChosenForCouncilSatisfaction = player.cardsToCouncilSatisfaction();
		numberOfCouncillorSatisfied = region.numberOfCouncillorsSatisfied(cardsChosenForCouncilSatisfaction);
		Scanner input = new Scanner(System.in);
		if (numberOfCouncillorSatisfied > 0) {
			System.out.println("You are able to satisfy the region Council with " + numberOfCouncillorSatisfied
					+ " Politic Cards!");
			playerPayment = CoinsManager.paymentForPermitTile(numberOfCouncillorSatisfied);
			player.performPayment(playerPayment);
			player.removeCardsFromHand(cardsChosenForCouncilSatisfaction);
			regionDeck = region.getDeck();
			System.out.println("Choose slot: 1 or 2?");
			int slot = input.nextInt();
			try {
				player.addUnusedPermitTiles(regionDeck.drawPermitTile(slot));
			} catch (InvalidSlotException e) {
				logger.log(Level.SEVERE, e.showError(), e);
			}
		} else
			System.out.println("You were not able to satisfy the specified Council with these Politic Cards");
	}

	/**
	 * This method allows a player to draw a Politic Card at the beginning of
	 * his turn.
	 */
	public void drawPoliticCard(Player player) {
		player.addCardOnHand(PoliticCardDeck.generateRandomPoliticCard());
	}

	/**
	 * @return the connector of the player with the specified player number.
	 */
	public ConnectorInt getPlayerConnector(int playerNumber) {// To add UML
																// scheme
		Player player = players.get(playerNumber);
		return player.getConnector();
	}

	/**
	 * NEEDS REVISION: this method must not return a boolean: the try/catch must
	 * be handled inside a while loop untile the move is correctly performed.
	 * 
	 * @return
	 */
	public boolean electCoucillor(Player player, String regionName, String councillorColor) {
		regionName = regionName.toUpperCase();
		regionName = regionName.trim();
		Region region = this.getRegion(regionName);
		try {
			region.electCouncillor(councillorColor);
		} catch (CouncillorNotFoundException e) {
			logger.log(Level.SEVERE, e.showError(), e);
			return false;
		}
		player.addCoins(4);
		return true;
	}

	/**
	 * NEEDS REVISION: this method must not return a boolean: the try/catch must
	 * be handled inside a while loop untile the move is correctly performed.
	 * 
	 * @return
	 */
	public boolean engageAssistant(Player player) {
		int coins = player.getCoins();
		if (coins >= 3) {
			player.removeCoins(3);
			player.addAssistant();
			return true;
		}
		return false;
	}

	/**
	 * NEEDS REVISION: the parameters communication must be implemented inside
	 * the method.
	 * 
	 * @return
	 */
	public boolean changeBusinessPermitTiles(Player player, String regionName) {
		Region region = this.getRegion(regionName);
		if (player.getNumberOfAssistants() >= 1) {
			region.getDeck().switchPermitTiles();
			player.removeAssistant();
			return true;
		}
		return false;
	}

	/**
	 * MUST BE FIXED IMMEDIATELY! COMPILATION ERRORS
	 * 
	 * @return
	 */
	/*
	 * public boolean buildEmporiumWithPermitTile(Player player,String cityName)
	 * { ArrayList<City> city; int i; PermitTile
	 * permitTile=player.getUnusedPermitTile(tileChose);
	 * city=permitTile.getCities(); for(i=0;i<city.size();i++)
	 * if(city.get(i).getName().equals(cityName) &&
	 * !(city.get(i).checkPresenceOfEmporium(player))){
	 * 
	 * }
	 * 
	 * 
	 * 
	 * }
	 */

	/**
	 * @return
	 */
	public boolean sendAssistantToElectCouncillor(Player player, String regionName, String councillorColor) {
		if (player.getNumberOfAssistants() >= 1) {
			Region region = this.getRegion(regionName);
			try {
				region.electCouncillor(councillorColor);
			} catch (CouncillorNotFoundException e) {

				logger.log(Level.SEVERE, e.showError(), e);

			}
			return true;
		} else
			return false;
	}

	/**
	 * @return
	 */
	public void addPlayer(ConnectorInt connectorInt) {// To add UML scheme
		Player player = new Player(connectorInt);
		this.players.add(player);
		if (isFull())
			this.startGame();
	}

	/**
	 * NEEDS REVISION: the specified name may be incorrect or invalid.
	 * Exception?
	 * 
	 * @return
	 */
	public Region getRegion(String regionName) {
		boolean regionFound = false;
		Region region = null;
		Region regions[] = this.board.getRegions();
		regionName = regionName.toUpperCase();
		regionName = regionName.trim();
		for (int i = 0; i < regions.length && !regionFound; i++) {
			if (regions[i].getName().equals(regionName)) {
				regionFound = true;
				region = regions[i];
			}
		}
		return region;
	}

	/**
	 * @return
	 */
	public boolean isFull() {
		return this.players.size() >= this.numberOfPlayers;
	}

}