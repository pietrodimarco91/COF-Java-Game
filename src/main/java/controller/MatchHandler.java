package controller;

import exceptions.*;
import model.*;
import server.view.cli.ServerOutputPrinter;

import java.io.PrintStream;
import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
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

	/**
	 * This constant represents the number of the parameters decided for the
	 * board configuration
	 */
	private static final int NUMBER_OF_PARAMETERS = 5;

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
	 * This attribute represents the creator of the match
	 */
	private Player creator;

	/**
	 * Number of players in this Match
	 */
	private int numberOfPlayers; // To add UML scheme

	/**
	 * This attribute is needed to temporary save the parameters after the
	 * configuration has been set up
	 */
	private int[] configParameters;

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

	public MatchHandler(int id, Date date, ConnectorInt connector) {
		this.players = new ArrayList<Player>();

		this.creator = new Player(connector, 1);

		this.players.add(creator);
		this.id = id;
		this.date = date;
		this.configParameters = new int[NUMBER_OF_PARAMETERS];
		this.configFileManager = new ConfigFileManager();
		this.pending = false;
		logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
		ServerOutputPrinter.printLine("[MATCH " + id + "]: Started running...");
	}

	public void run() {
		boardConfiguration(creator);
		waitingForPlayers();
		countdown();
		setDefinitiveNumberOfPlayers();
		boardInitialization();
		mapConfiguration(creator.getConnector());
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
				playerConnector.writeToClient("1) Create a new board configuration\n2) Choose an existing configuration\n");
						
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
							saveConfig(config);
							this.numberOfPlayers = config.getNumberOfPlayers();
							playerConnector.writeToClient(
									"You've chosen the Board Configuration number "+id+": Now waiting for new players...");
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
		pending=true;
	}

	/**
	 *
	 */
	public void mapConfiguration(ConnectorInt connector) {
		boolean stop = false;
		int choice = 0;
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
				try {
					generateConnection(this.board, connector);
					break;
				} catch (InvalidInputException e1) {
					try {
						creator.getConnector().writeToClient(e1.printError());
					} catch (RemoteException e) {
						logger.log(Level.INFO, "Error: couldn't write to client", e);
					}
				}
				break;
			case 2:
				try {
					removeConnection(this.board, connector);
				} catch (InvalidInputException e1) {
					try {
						creator.getConnector().writeToClient(e1.printError());
					} catch (RemoteException e) {
						logger.log(Level.INFO, "Error: couldn't write to client", e);
					}
				}
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
				try {
					connector.writeToClient(this.board.printMatrix());
				} catch (RemoteException e) {
					logger.log(Level.INFO, "Error: couldn't write to client", e);
				}

				break;
			case 5:
				try {
					connector.writeToClient(this.board.printConnections());
				} catch (RemoteException e) {
					logger.log(Level.INFO, "Error: couldn't write to client", e);
				}

				break;
			case 6:
				try {
					connector.writeToClient(this.board.toString());
				} catch (RemoteException e) {
					logger.log(Level.INFO, "Error: couldn't write to client", e);
				}
				break;
			case 7:
				try {
					countDistance(this.board, connector);
				} catch (InvalidInputException e1) {
					try {
						creator.getConnector().writeToClient(e1.printError());
					} catch (RemoteException e) {
						logger.log(Level.INFO, "Error: couldn't write to client", e);
					}
				}
				break;
			case 8:
				try {
					connector.writeToClient(this.board.printDistances());
				} catch (RemoteException e) {
					logger.log(Level.INFO, "Error: couldn't write to client", e);
				}

				break;
			default:
				try {
					connector.writeToClient("Error: invalid number\n");
				} catch (RemoteException e) {
					logger.log(Level.INFO, "Error: couldn't write to client", e);
				}
			}

		}
		pending=true;
	}

	/**
	 * @throws InvalidInputException
	 * 
	 */
	public void generateConnection(Board map, ConnectorInt connector) throws InvalidInputException {
		String first = null;
		String second = null;
		City city1 = null, city2 = null, tempCity;
		List<City> cities = map.getMap();
		Iterator<City> cityIterator = cities.iterator();
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
			try {
				first = connector.receiveStringFromClient();
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't receive from client", e);
			}

		} while (first.length() > 1);
		do {
			try {
				connector.writeToClient("Insert the FIRST letter of the second city:\n");
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't write to client", e);
			}
			try {
				second = connector.receiveStringFromClient();
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't receive from client", e);
			}

		} while (second.length() > 1 || second.equals(first));
		if (first == null || second == null || !("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".contains(first))
				|| !("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".contains(second))) {
			throw new InvalidInputException();
		}
		first = first.toUpperCase();
		second = second.toUpperCase();
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
	 * @throws InvalidInputException
	 * 
	 */
	public void removeConnection(Board map, ConnectorInt connector) throws InvalidInputException {
		String first = null;
		String second = null;
		City city1 = null, city2 = null, tempCity;
		List<City> cities = map.getMap();
		Iterator<City> cityIterator = cities.iterator();

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
			try {
				first = connector.receiveStringFromClient();
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't receive from client", e);
			}
		} while (first.length() > 1);
		do {
			try {
				connector.writeToClient("Insert the FIRST letter of the second city:\n");
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't write to client", e);
			}
			try {
				second = connector.receiveStringFromClient();
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't receive from client", e);
			}
		} while (second.length() > 1 || second.equals(first));
		if (first == null || second == null || !("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".contains(first))
				|| !("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".contains(second))) {
			throw new InvalidInputException();
		}
		first = first.toUpperCase();
		second = second.toUpperCase();
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
	 * @throws InvalidInputException
	 * 
	 */
	public void countDistance(Board map, ConnectorInt connector) throws InvalidInputException {
		String first = null;
		String second = null;
		City city1 = null, city2 = null, tempCity;
		List<City> cities = map.getMap();
		Iterator<City> cityIterator = cities.iterator();

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
			try {
				first = connector.receiveStringFromClient();
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't receive from client", e);
			}
		} while (first.length() > 1);
		do {
			try {
				connector.writeToClient("Insert the FIRST letter of the second city:\n");
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't write to client", e);
			}
			try {
				second = connector.receiveStringFromClient();
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't receive from client", e);
			}
		} while (second.length() > 1 || second.equals(first));
		if (first == null || second == null || !("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".contains(first))
				|| !("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".contains(second))) {
			throw new InvalidInputException();
		}
		first = first.toUpperCase();
		second = second.toUpperCase();
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
				if(map.countDistance(city1, city2)!=-1) {
				connector.writeToClient("Distance between " + city1.getName() + " and " + city2.getName() + " is: "
						+ map.countDistance(city1, city2) + "\n");
				} else {
					connector.writeToClient(city1.getName() + " and " + city2.getName() + "are not connected\n");
				}
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
		saveConfig(numberOfPlayers, rewardTokenBonusNumber, permitTileBonusNumber, nobilityTrackBonusNumber, linksBetweenCities);
		this.numberOfPlayers = numberOfPlayers;
		try {
			playerConnector
					.writeToClient("Board correctly generated with selected parameters! Now we're about to start...");
		} catch (RemoteException e1) {
			logger.log(Level.INFO, "Error: couldn't write to client", e1);
		}
		pending=true;
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
	 * This method is invoked to initialize the board before a match starts. The
	 * parameters are set by the first player that joins the match.
	 */
	public void boardInitialization() {
		board = new Board(configParameters[0],configParameters[1],configParameters[2],configParameters[3],configParameters[4]);
	}
	
	/**
	 * NEEDS JAVADOC
	 */
	public void setDefinitiveNumberOfPlayers() {
		configParameters[0]=this.players.size();
	}

	/**
	 * NEEDS JAVADOC
	 * @param config
	 */
	public void saveConfig(ConfigObject config) {
		configParameters[0] = config.getNumberOfPlayers();
		configParameters[1] = config.getRewardTokenBonusNumber();
		configParameters[2] = config.getPermitTileBonusNumber();
		configParameters[3] = config.getNobilityTrackBonusNumber();
		configParameters[4] = config.getLinksBetweenCities();
	}
	
	/**
	 * NEEDS JAVADOC
	 * @param numberOfPlayers
	 * @param rewardTokenBonusNumber
	 * @param permitTileBonusNumber
	 * @param nobilityTrackBonusNumber
	 * @param linksBetweenCities
	 */
	public void saveConfig(int numberOfPlayers,int rewardTokenBonusNumber,int permitTileBonusNumber,int nobilityTrackBonusNumber,int linksBetweenCities) {
		configParameters[0]=numberOfPlayers;
		configParameters[1]=rewardTokenBonusNumber;
		configParameters[2]=permitTileBonusNumber;
		configParameters[3]=nobilityTrackBonusNumber;
		configParameters[4]=linksBetweenCities;
	}

	/**
	 * NEEDS JAVADOC
	 */
	public void waitingForPlayers() {

		ServerOutputPrinter.printLine("[MATCH " + id + "] Currently waiting for players...");
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

		Player player;
		for (int i = 0; i < this.players.size(); i++) {
			player=this.players.get(i);
			showMap(player);
			showRegionContent(player);
			showPlayerInfo(player);

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
				try {
					player.getConnector().writeToClient(e.showError());
				} catch (RemoteException e1) {
					logger.log(Level.SEVERE, "Error: couldn't write to client!", e1);
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
	public ConnectorInt getPlayerConnector(int playerNumber) {// To
																			// add
																			// UML
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

	public void buildEmporiumWithPermitTile(Player player) {
		ArrayList<City> cities;
		int permitTileChoice = -1;
		String cityChoice = null;

		ConnectorInt connector = player.getConnector();


		do {

			try {
				connector.writeToClient("Which card do you want to choose?");
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't write to client", e);
			}
			try {
				connector.writeToClient(player.showPermitTileCards());
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't write to client", e);
			}
			try {
				permitTileChoice = connector.receiveIntFromClient();
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't receive from client", e);
			}
		} while (permitTileChoice < 0 || permitTileChoice > (player.getNumberOfPermitTile() - 1));

		PermitTile permitTile = (PermitTile) player.getUnusedPermitTile(permitTileChoice);

		do {
			try {
				connector.writeToClient("Which city do you want to build?");
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't write to client", e);
			}
			try {
				cityChoice = connector.receiveStringFromClient();
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't receive from client", e);
			}
		} while (!checkCorrectCityNameChoice(permitTile, cityChoice)
				|| !checkPresenceOfEmporium(permitTile, player, cityChoice));
		buildEmporium(permitTile, player, cityChoice);
		try {
			connector.writeToClient("Your emporium has been successfully built!:D");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
		player.fromUnusedToUsedPermitTile(player, permitTile);

	}

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
	public void addPlayer(ConnectorInt connector, int id) {// To
																						// add
																						// UML
		// scheme
		Player player = new Player(connector, id);
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
	 * 
	 */
	public void showMap(Player player) {
		try {
			player.getConnector().writeToClient(this.board.printMatrix());
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
	}

	/**
	 * @return
	 */
	public void showRegionContent(Player player) {
		Region regions[] = this.board.getRegions();
		for (int i = 0; i < regions.length; i++)
			try {
				player.getConnector().writeToClient(regions[i].toString());
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't write to client", e);
			}
	}

	/**
	 * @return
	 */
	public void showPlayerPoliticCards(Player player) {
		try {
			player.getConnector().writeToClient("You have these Politic Cars: ");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
		try {
			player.getConnector().writeToClient(player.showPoliticCards());
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
	}
	/**
	 * 
	 * @param player
	 */
	public void showPlayerCoins(Player player) {
		int coins=player.getCoins();
		try {
			player.getConnector().writeToClient("COINS: "+ coins+" ");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
	}
	/**
	 * 
	 * @param player
	 */
	public void showPlayerAssistants(Player player) {
		int assistants=player.getNumberOfAssistants();
		try {
			player.getConnector().writeToClient("ASSISTANTS: "+ assistants+" ");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
	}
	
	/**
	 * 
	 * @param player
	 */
	public void showPlayerVictoryPoints(Player player) {
		int victoryPoints=player.getVictoryPoints();
		try {
			player.getConnector().writeToClient("VICTORY POINTS: "+ victoryPoints+" ");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
	}
	/**
	 * 
	 * @param player
	 */
	public void showPlayerNumberOfEmporium(Player player) {
		int emporium=player.getNumberOfEmporium();
		try {
			player.getConnector().writeToClient("NUMBER OF EMPORIUM: "+emporium+" ");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
	}


	/**
	 * 
	 * @param player
	 */
	public void showPlayerPermitTileUnused(Player player) {
		String permitTileUnused=player.showPermitTileCards();
		try {
			player.getConnector().writeToClient("PERMIT TILE UNUSED:\n");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
		try {
			player.getConnector().writeToClient(permitTileUnused);
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
	}
	
	/**
	 * 
	 * @param player
	 */
	public void showPlayerPermitTileUsed(Player player) {
		String permitTileUsed=player.showUsedPermitTileCards();
		try {
			player.getConnector().writeToClient("PERMIT TILE USED:\n");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
		try {
			player.getConnector().writeToClient(permitTileUsed);
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
	}
	/**
	 * 
	 * @param player
	 */
	public void showPlayerInfo(Player player) {
		showPlayerVictoryPoints(player);
		showPlayerCoins(player);
		showPlayerAssistants(player);
		showPlayerNumberOfEmporium(player);
		showPlayerPoliticCards(player);
		showPlayerPermitTileUnused(player);
		showPlayerPermitTileUsed(player);
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

	/**
	 * 
	 */
	public boolean checkCorrectCityNameChoice(PermitTile permitTile, String cityChoice) {
		List<City> cities = permitTile.getCities();
		cityChoice = cityChoice.trim();
		cityChoice = cityChoice.toUpperCase();
		String allCity = "";
		for (City tempCities : cities) {
			allCity += tempCities.getName();
		}
		return allCity.contains(cityChoice);
	}
/**
 * 
 * @param permitTile
 * @param player
 * @param cityChoice
 * @return
 */
	public boolean checkPresenceOfEmporium(PermitTile permitTile, Player player, String cityChoice) {
		List<City> cities = permitTile.getCities();
		City tempCity;
		boolean find = false;
		cityChoice = cityChoice.trim();
		cityChoice = cityChoice.toUpperCase();
		for (int i = 0; i < cities.size() && !find; i++) {
			tempCity = cities.get(i);
			if (tempCity.getName().equals(cityChoice))
				find = true;
		}
		return find;

	}

	public void buildEmporium(PermitTile permitTile, Player player, String cityChoice) {
		List<City> cities = permitTile.getCities();
		cityChoice = cityChoice.trim();
		cityChoice = cityChoice.toUpperCase();
		for (City tempCities : cities) {
			if (tempCities.getName().equals(cityChoice))
				tempCities.buildEmporium(player);
		}
	}

}