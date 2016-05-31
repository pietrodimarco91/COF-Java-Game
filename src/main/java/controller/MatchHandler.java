package controller;

import exceptions.ConfigAlreadyExistingException;
import exceptions.CouncillorNotFoundException;
import exceptions.InvalidInputException;
import exceptions.InvalidSlotException;
import exceptions.UnexistingConfigurationException;
import model.*;

import java.io.PrintStream;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 * Created by Gabriele on 22/05/16. This class represents the thread always
 * running while a match is on-going. It stores the core of the game engine.
 */
public class MatchHandler extends Thread {

	private static final Logger logger = Logger.getLogger(MatchHandler.class.getName());

	private PrintStream out;

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
	 * 
	 */
	private Player creator;

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
		this.creator = new Player(connectorInt, 1);
		this.players.add(creator);
		this.id = id;
		this.date = date;
		this.configFileManager = new ConfigFileManager();
		this.pending = false;
		out = new PrintStream(System.out);
		logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
		out.println("[MATCH " + id + "]: Started running...");
	}

	/**
	 * MUST BE ADAPTED TO THE LAST VERSION OF MAP CONSTRUCTOR
	 */

	public void run() {
		boardConfiguration(creator);
		boolean stop = false;
		while (!stop) {

		}
		mapConfiguration(creator.getConnector());
		pending = true; // Player has finished to set the match

		// Aggiungi controllo per verificare se ArrayList è pieno di giocatori

		// Start the match
		waitingForPlayers();
		countdown();
		play();
	}

	/**
	 * NEEDS JAVADOC
	 * 
	 * @param player
	 */
	public void boardConfiguration(Player player) {
		boolean correctAnswer = false;
		int choice = 0;
		ConfigObject config;
		ConnectorInt playerConnector = player.getConnector();
		try {
			playerConnector.writeToClient("BOARD CONFIGURATION:\n");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
		while (!correctAnswer) {
			try {
				playerConnector
						.writeToClient("1) Create a new board configuration\n2) Choose an existing configuration\n");
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't write to client", e);
			}
			try {
				choice = playerConnector.receiveIntFromClient();
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't receive from client", e);
			}
			if (choice != 1 && choice != 2) {
				try {
					playerConnector.writeToClient("ERROR: incorrect input. Please retry\n");
				} catch (RemoteException e) {
					logger.log(Level.INFO, "Error: couldn't write to client", e);
				}
			} else
				correctAnswer = true;
		}

		if (choice == 1) {
			newConfiguration(playerConnector);
		} else {
			if (configFileManager.getConfigurations().size() > 0) {
				try {
					playerConnector.writeToClient("These are the currently existing configurations:\n");
					ArrayList<ConfigObject> configurations = configFileManager.getConfigurations();
					for (ConfigObject configuration : configurations) {
						playerConnector.writeToClient(configuration.toString());
					}
					int id = -1;
					boolean correctID = false;
					while (!correctID) {
						playerConnector.writeToClient("Choose a configuration by typing its ID\n");
						id = playerConnector.receiveIntFromClient();
						try {
							config = configFileManager.getConfiguration(id);
							correctID = true;
							boardSetup(config);
							this.numberOfPlayers = config.getNumberOfPlayers();
							playerConnector.writeToClient(
									"Board correctly generated with selected parameters! Now we're about to start...");
						} catch (UnexistingConfigurationException e) {
							playerConnector.writeToClient(e.printError());
						}
					}
				} catch (RemoteException e) {
					logger.log(Level.INFO, "Error: couldn't write to client", e);
				}
			} else {
				try {
					playerConnector
							.writeToClient("There aren't any configurations yet! Please create a new one :-) \n");
					newConfiguration(playerConnector);

				} catch (RemoteException e) {
					logger.log(Level.INFO, "Error: couldn't write to client", e);
				}
			}
		}
	}

	/**
	 *
	 */
	public void mapConfiguration(ConnectorInt connector) {
		boolean stop = false;
		int choice=0;
		while (!stop) {
			try {
				connector.writeToClient(
						"Next choice?\n1) New connection\n2)Remove connection\n3) Go on\n4) View graphic map\n5) View links\n6) View map status\n7) Count distance\n8) Show all distances\n ");
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't write to client", e);
			}
			try {
				choice = connector.receiveIntFromClient();
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't receive to client", e);
			}
			switch (choice) {
			case 1:
				generateConnection(this.board,connector);
				break;
			case 2:
				removeConnection(this.board,connector);
				break;
			case 3:
				if (this.board.graphIsConnected()) {
					stop = true;
					break;
				} else
					try {
						connector.writeToClient("Error: map is not connected. Add the necessary connections.\n");
					} catch (RemoteException e) {
						logger.log(Level.INFO, "Error: couldn't write to client", e);
					}
				break;
			case 4:
				this.board.printMatrix();
				break;
			case 5:
				this.board.printConnections();
				break;
			case 6:
				try {
					connector.writeToClient(this.board.toString());
				} catch (RemoteException e) {
					logger.log(Level.INFO, "Error: couldn't write to client", e);
				}
				break;
			case 7:
				countDistance(this.board,connector);
				break;
			case 8:
				this.board.printDistances();
				break;
			default:
				try {
					connector.writeToClient("Error: invalid number\n");
				} catch (RemoteException e) {
					logger.log(Level.INFO, "Error: couldn't write to client", e);
				}
			}

		}

	}

	/**
	* 
	*/
	public void generateConnection(Board map, ConnectorInt connector) {
		String first, second;
		City city1 = null, city2 = null, tempCity;
		List<City> cities = map.getMap();
		Iterator<City> cityIterator = cities.iterator();
		Scanner input = new Scanner(System.in);
		try {
			connector.writeToClient("NEW CONNECTION\n");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}

		do {
			try {
				connector.writeToClient("Insert the FIRST letter of the first city:\n");
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't write to client", e);
			}
			System.out.println("");
			first = input.nextLine();
			first = first.toUpperCase();
		} while (first.length() > 1);
		do {
			try {
				connector.writeToClient("Insert the FIRST letter of the second city:\n");
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't write to client", e);
			}
			second = input.nextLine();
			second = second.toUpperCase();
		} while (second.length() > 1 || second.equals(first));

		while (cityIterator.hasNext()) {
			tempCity = cityIterator.next();
			if (tempCity.getName().charAt(0) == first.charAt(0)) {
				city1 = tempCity;
			} else if (tempCity.getName().charAt(0) == second.charAt(0)) {
				city2 = tempCity;
			}
		}
		if (map.checkPossibilityOfNewConnection(city1, city2))
			map.connectCities(city1, city2);
		else {
			try {
				connector.writeToClient("Error: cities cannot be connected\n");
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't write to client", e);
			}
		}
	}
	
	/**
	 * 
	 */
	public void removeConnection(Board map, ConnectorInt connector) {
		String first, second;
		City city1 = null, city2 = null, tempCity;
		List<City> cities = map.getMap();
		Iterator<City> cityIterator = cities.iterator();
		Scanner input = new Scanner(System.in);
		try {
			connector.writeToClient("REMOVE CONNECTION\n");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
		do {
			try {
				connector.writeToClient("Insert the FIRST letter of the first city:\n");
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't write to client", e);
			}
			first = input.nextLine();
			first = first.toUpperCase();
		} while (first.length() > 1);
		do {
			try {
				connector.writeToClient("Insert the FIRST letter of the second city:\n");
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't write to client", e);
			}	
			second = input.nextLine();
			second = second.toUpperCase();
		} while (second.length() > 1 || second.equals(first));

		while (cityIterator.hasNext()) {
			tempCity = cityIterator.next();
			if (tempCity.getName().charAt(0) == first.charAt(0)) {
				city1 = tempCity;
			} else if (tempCity.getName().charAt(0) == second.charAt(0)) {
				city2 = tempCity;
			}
		}
		map.unconnectCities(city1, city2);
	}
	/**
	 * 
	 */
	public void countDistance(Board map,ConnectorInt connector) {
		String first, second;
		City city1 = null, city2 = null, tempCity;
		List<City> cities = map.getMap();
		Iterator<City> cityIterator = cities.iterator();
		Scanner input = new Scanner(System.in);
		try {
			connector.writeToClient("COUNT DISTANCE:\n");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}

		do {
			try {
				connector.writeToClient("Insert the FIRST letter of the first city:\n");
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't write to client", e);
			}
			first = input.nextLine();
			first = input.nextLine();
		} while (first.length() > 1);
		do {
			try {
				connector.writeToClient("Insert the FIRST letter of the second city:\n");
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't write to client", e);
			}
			second = input.nextLine();
			second = second.toUpperCase();
		} while (second.length() > 1 || second.equals(first));

		while (cityIterator.hasNext()) {
			tempCity = cityIterator.next();
			if (tempCity.getName().charAt(0) == first.charAt(0)) {
				city1 = tempCity;
			} else if (tempCity.getName().charAt(0) == second.charAt(0)) {
				city2 = tempCity;
			}
		}
		if (city1 != null && city2 != null)
			try {
				connector.writeToClient("Distance between " + city1.getName() + " and " + city2.getName() + " is: "
						+ map.countDistance(city1, city2)+"\n");
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't write to client", e);
			}
			
	}

	/**
	 * NEEDS CODE QUALITY ADJUSTEMENTS
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
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}

		while (!stop) {
			try {
				parameters = playerConnector.receiveStringFromClient();
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't receive from client", e);
			}
			int[] par = new int[5];
			int i = 0;
			StringTokenizer tokenizer = new StringTokenizer(parameters, " ");
			try {
				while (tokenizer.hasMoreTokens()) {
					par[i] = Integer.parseInt(tokenizer.nextToken());
					i++;
				}
				numberOfPlayers = par[0];
				rewardTokenBonusNumber = par[1];
				permitTileBonusNumber = par[2];
				nobilityTrackBonusNumber = par[3];
				linksBetweenCities = par[4];
			} catch (NumberFormatException e) {
				try {
					playerConnector.writeToClient("Error: Expected integers values! Retry!");
				} catch (RemoteException e1) {
					logger.log(Level.INFO, "Error: couldn't write to client", e1);
				}
			}
			try {
				parametersValidation(numberOfPlayers, rewardTokenBonusNumber, permitTileBonusNumber,
						nobilityTrackBonusNumber, linksBetweenCities);
				configFileManager.createConfiguration(numberOfPlayers, rewardTokenBonusNumber, permitTileBonusNumber,
						nobilityTrackBonusNumber, linksBetweenCities);
				stop = true;
			} catch (InvalidInputException e) {
				try {
					playerConnector.writeToClient(e.printError());
				} catch (RemoteException e1) {
					logger.log(Level.INFO, "Error: couldn't write to client", e1);
				}
			} catch (ConfigAlreadyExistingException e) {
				try {
					playerConnector.writeToClient(e.printError());
				} catch (RemoteException e1) {
					logger.log(Level.INFO, "Error: couldn't write to client", e1);
				}
			}
		}
		boardSetup(numberOfPlayers, rewardTokenBonusNumber, permitTileBonusNumber, nobilityTrackBonusNumber,
				linksBetweenCities);
		this.numberOfPlayers = numberOfPlayers;
		try {
			playerConnector
					.writeToClient("Board correctly generated with selected parameters! Now we're about to start...");
		} catch (RemoteException e1) {
			logger.log(Level.INFO, "Error: couldn't write to client", e1);
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
				|| (permitTileBonusNumber < 1 || permitTileBonusNumber > 3)
				|| (nobilityTrackBonusNumber < 1 || nobilityTrackBonusNumber > 3))
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
	 * NEEDS JAVADOC
	 * 
	 * @param config
	 */
	public void boardSetup(ConfigObject config) {
		int numberOfPlayers = 0, linksBetweenCities = 0, rewardTokenBonusNumber = 0, permitTileBonusNumber = 0,
				nobilityTrackBonusNumber = 0;
		numberOfPlayers = config.getNumberOfPlayers();
		linksBetweenCities = config.getLinksBetweenCities();
		rewardTokenBonusNumber = config.getRewardTokenBonusNumber();
		permitTileBonusNumber = config.getPermitTileBonusNumber();
		nobilityTrackBonusNumber = config.getNobilityTrackBonusNumber();
		board = new Board(numberOfPlayers, rewardTokenBonusNumber, permitTileBonusNumber, nobilityTrackBonusNumber,
				linksBetweenCities);
	}

	/**
	 * NEEDS JAVADOC
	 */
	public void waitingForPlayers() {

		out.println("[Match ID: " + id + "] Currently waiting for players...");
		try {
			creator.getConnector().writeToClient("[Match ID: " + id + "] Currently waiting for players...");
		} catch (RemoteException e) {
			logger.log(Level.FINEST, "Error: couldn't write to client\n", e);
		}
		while (this.players.size() < 2) {
			// Match starts with at least two players
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, "ERROR TRYING TO SLEEP!", e);
			}
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			logger.log(Level.SEVERE, "ERROR TRYING TO SLEEP!", e);
		}
	}

	/**
	 * NEEDS JAVADOC
	 */
	public void countdown() {
		for (int i = 20; i > 0; i--) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.log(Level.SEVERE, "ERROR TRYING TO SLEEP!", e);
			}
			for (Player player : players) {
				try {
					player.getConnector().writeToClient("MATCH STARTING IN: " + i + "\n");
				} catch (RemoteException e) {
					logger.log(Level.FINEST, "Error: couldn't write to client\n", e);
				}
			}
		}
	}

	/**
	 * NEEDS IMPLEMENTATION
	 */
	public void play() {// To add UML scheme
		/*
		 * Player player; for (int i = 0; i < this.players.size(); i++) {
		 * 
		 * }
		 */

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
				try {
					player.getConnector().writeToClient(e.showError());
				} catch (RemoteException e1) {
					logger.log(Level.SEVERE, "Error: couldn't write to client!", e);
				}
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
			try {
				player.getConnector().writeToClient(e.showError());
			} catch (RemoteException e1) {
				logger.log(Level.SEVERE, "Error: couldn't write to client!", e);
			}
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
				try {
					player.getConnector().writeToClient(e.showError());
				} catch (RemoteException e1) {
					logger.log(Level.SEVERE, "Error: couldn't write to client!", e1);
				}
			}
			return true;
		} else
			return false;
	}

	/**
	 * @return
	 */
	public void addPlayer(ConnectorInt connectorInt, int id) {// To add UML
																// scheme
		Player player = new Player(connectorInt,id);
		this.players.add(player);
		if (isFull())
			this.play();
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

	/**
	 * 
	 */
	public ArrayList<Player> getPlayers() {
		return this.players;
	}

	/**
	 * 
	 */
	public int getIdentifier() {
		return this.id;
	}
}