package controller;

import client.actions.Action;
import controller.Client.ClientSideConnector;
import exceptions.CouncillorNotFoundException;
import exceptions.InvalidInputException;
import exceptions.InvalidSlotException;
import exceptions.UnsufficientCoinsException;
import model.*;
import server.view.cli.ServerOutputPrinter;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 * Created by Gabriele on 22/05/16. This class represents the thread always
 * running while a match is on-going. It stores the core of the game engine.
 */
public class MatchHandler{

	private static final Logger logger = Logger.getLogger(MatchHandler.class.getName());

	/**
	 * This constant represents the number of the parameters decided for the
	 * board configuration
	 */
	private static final int NUMBER_OF_PARAMETERS = 5;

	private static final int MINUMUM_NUMBER_OF_PLAYERS = 2;

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


	/**
	 * Mapstatus:
	 * -0 wait board configuration
	 * -1 wait map configuration
	 * -2 play
	 * -3 market
	 * -4 finisched
	 */
	private int mapStatus;

	/**
	 * Default constructor
	 */

	public MatchHandler(int id, Date date, ClientSideConnectorInt connector, ServerSideConnectorInt serverSideConnector) {
		this.players = new ArrayList<Player>();
		mapStatus=0;
		serverSideConnector.setPlayerId(0);
		this.creator = new Player(connector,0);
		this.numberOfPlayers = MINUMUM_NUMBER_OF_PLAYERS;
		this.players.add(creator);
		this.id = id;
		this.date = date;
		this.configParameters = new int[NUMBER_OF_PARAMETERS];
		this.pending = false;
		logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
		ServerOutputPrinter.printLine("[MATCH " + id + "]: Started running...");
	}


	public void run() {
		new BoardConfiguration(creator,configParameters,numberOfPlayers);
		waitingForPlayers();
		countdown();
		setDefinitiveNumberOfPlayers();
		boardInitialization();
		mapConfiguration(creator.getConnector());
		play();
	}



	/**
	 *
	 */
	public void mapConfiguration(ClientSideConnector connector) {
		boolean stop = false;
		int choice = 0;
		while (!stop) {
			try {
				connector.sendToClient(new Packet("Next choice?\n1) New connection\n2)Remove connection\n3) Go on\n4) View graphic map\n5) View links\n6) View map status\n7) Count distance\n8) Show all distances\n ");
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
				if (map.countDistance(city1, city2) != -1) {
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
	 * This method is invoked to initialize the board before a match starts. The
	 * parameters are set by the first player that joins the match.
	 */
	public void boardInitialization() {
		board = new Board(configParameters[0], configParameters[1], configParameters[2], configParameters[3],
				configParameters[4]);
	}

	/**
	 * NEEDS JAVADOC
	 */
	public void setDefinitiveNumberOfPlayers() {
		configParameters[0] = this.players.size();
		this.numberOfPlayers=this.players.size();
	}





	/**
	 * NEEDS JAVADOC
	 */
	public void waitingForPlayers() {
		ServerOutputPrinter.printLine("[MATCH " + id + "] Currently waiting for players...");
		pending = true;
		try {
			creator.getConnector().writeToClient("[Match ID: " + id + "] Currently waiting for players...");
		} catch (RemoteException e) {
			logger.log(Level.FINEST, "Error: couldn't write to client\n", e);
		}
		while (this.players.size() < MINUMUM_NUMBER_OF_PLAYERS) {
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

		while (true) {
			roundsOfPlayer();
		}

	}

	/**
	 * INCOMPLETE IMPLEMENTATION
	 */
	public String toString() {
		String string = "";
		string += "Match number " + this.id + "\n";
		string += "Launched on: ";
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

	public void roundsOfPlayer() {
		Player player;
		int choice = 0;
		ConnectorInt connector;
		for (int i = 0; i < this.players.size(); i++) {
			player = this.players.get(i);
			connector = player.getConnector();
			showMap(player);
			showRegionContent(player);
			drawPoliticCard(player);
			showPlayerInfo(player);
			try {
				connector.writeToClient("You have drawn a Politic Card!");
			} catch (RemoteException e) {
				logger.log(Level.FINEST, "Error: couldn't write to client\n", e);
			}
			do {
				try {
					connector.writeToClient("Do you wanna choose firstly:\n1)Main Actions\n2)Quick Actions?");
				} catch (RemoteException e) {
					logger.log(Level.FINEST, "Error: couldn't write to client\n", e);
				}
				try {
					choice = connector.receiveIntFromClient();
					ServerOutputPrinter.printLine(String.valueOf(choice));

				} catch (RemoteException e) {
					logger.log(Level.FINEST, "Error: couldn't receive from client\n", e);

				}
			} while (choice != 1 && choice != 2);
			if (choice == 1) {
				do {
					showMainActions(player);
					try {
						choice = connector.receiveIntFromClient();
					} catch (RemoteException e) {
						logger.log(Level.FINEST, "Error: couldn't receive from client\n", e);
					}
				} while (choice != 1 && choice != 2 && choice != 3 && choice != 4);

				mainActions(player, choice);

				do {
					showQuickActions(player);
					try {
						choice = connector.receiveIntFromClient();
					} catch (RemoteException e) {
						logger.log(Level.FINEST, "Error: couldn't receive from client\n", e);
					}
				} while (choice != 1 && choice != 2 && choice != 3 && choice != 4 && choice != 5);
				quickActions(player, choice);
			} else {
				do {
					showQuickActions(player);

					try {
						choice = connector.receiveIntFromClient();
					} catch (RemoteException e) {
						logger.log(Level.FINEST, "Error: couldn't receive from client\n", e);
					}
				} while (choice != 1 && choice != 2 && choice != 3 && choice != 4 && choice != 5);
				quickActions(player, choice);
				do {
					showMainActions(player);
					try {
						choice = connector.receiveIntFromClient();
					} catch (RemoteException e) {
						logger.log(Level.FINEST, "Error: couldn't receive from client\n", e);
					}
				} while (choice != 1 && choice != 2 && choice != 3 && choice != 4);
				mainActions(player, choice);
			}
			try {
				connector.writeToClient("These are your update information! Think very well for the next action... ");
			} catch (RemoteException e) {
				logger.log(Level.FINEST, "Error: couldn't write to client\n", e);
			}
			showPlayerInfo(player);
		}
	}

	/**
	 * 
	 * @param player
	 * @param choice
	 */
	public void mainActions(Player player, int choice) {
		switch (choice) {
		case 1:
			showMap(player);
			buyPermitTile(player);
			break;
		case 2:
			// da implementare
			break;
		case 3:
			showMap(player);
			electCouncillor(player);
			break;
		case 4:
			showMap(player);
			buildEmporiumWithPermitTile(player);
			break;
		}
	}

	/**
	 * 
	 * @param player
	 * @param choice
	 */
	public void quickActions(Player player, int choice) {
		switch (choice) {
		case 1:
			showMap(player);
			engageAssistant(player);
			break;
		case 2:
			showMap(player);
			changeBusinessPermitTiles(player);
			break;
		case 3:
			showMap(player);
			sendAssistantToElectCouncillor(player);
			break;
		case 4:
			showMap(player);
			performAdditionalMainAction(player);
			break;
		}
	}

	/**
	 * NEEDS REVISION AFTER IMPLEMENTATION. Especially the try/catch and the
	 * exceptions.
	 * 
	 * @param player
	 * @param regionName
	 */
	public void buyPermitTile(Player player) {
		int playerPayment;
		int numberOfCouncillorSatisfied;
		int slot = 0;
		String regionName = "";
		PermitTileDeck regionDeck;

		try {
			player.getConnector().writeToClient("Which region do you want to buy a Permit Tile? ");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			logger.log(Level.FINEST, "Error: couldn't write to client\n", e);
		}

		try {
			regionName = player.getConnector().receiveStringFromClient();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			logger.log(Level.FINEST, "Error: couldn't receive from client\n", e);
		}
		regionName = regionName.trim();
		regionName = regionName.toUpperCase();
		Region region = this.getRegion(regionName);
		region = this.getRegion(regionName);
		ArrayList<PoliticCard> cardsChosenForCouncilSatisfaction = player.cardsToCouncilSatisfaction();
		numberOfCouncillorSatisfied = region.numberOfCouncillorsSatisfied(cardsChosenForCouncilSatisfaction);

		if (numberOfCouncillorSatisfied > 0) {
			try {
				player.getConnector().writeToClient("You are able to satisfy the region Council with "
						+ numberOfCouncillorSatisfied + " Politic Cards!");
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				logger.log(Level.FINEST, "Error: couldn't write to client\n", e);
			}
			playerPayment = CoinsManager.paymentForPermitTile(numberOfCouncillorSatisfied);
			player.performPayment(playerPayment);
			player.removeCardsFromHand(cardsChosenForCouncilSatisfaction);
			regionDeck = region.getDeck();
			do {
				try {
					player.getConnector().writeToClient("Choose slot: 1 or 2?");
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					logger.log(Level.FINEST, "Error: couldn't write to client\n", e);
				}
				try {
					slot = player.getConnector().receiveIntFromClient();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					logger.log(Level.FINEST, "Error: couldn't receive from client\n", e);
				}
			} while (slot != 1 && slot != 2);
			try {
				player.addUnusedPermitTiles(regionDeck.drawPermitTile(slot));
			} catch (InvalidSlotException e) {
				try {
					player.getConnector().writeToClient(e.showError());
				} catch (RemoteException e1) {
					logger.log(Level.SEVERE, "Error: couldn't write to client!", e1);
				}
			}
			try {
				player.getConnector().writeToClient("You bought a Permit Tile");
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				logger.log(Level.FINEST, "Error: couldn't write to client\n", e);
			}

		} else {
			try {
				player.getConnector()
						.writeToClient("You were not able to satisfy the specified Council with these Politic Cards");
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				logger.log(Level.FINEST, "Error: couldn't write to client\n", e);
			}
		}
	}

	/**
	 * This method allows a player to draw a Politic Card at the beginning of
	 * his turn.
	 */
	public void drawPoliticCard(Player player) {
		player.addCardOnHand(PoliticCardDeck.generateRandomPoliticCard());
	}
	
	/**
	 * NEEDS IMPLEMENTATION
	 * @return
	 * @throws UnsufficientCoinsException 
	 */
	public void buildEmporiumWithKingsHelp(Player player) throws UnsufficientCoinsException {
		String city=null;
		ArrayList<String> chosenPoliticCards = new ArrayList<String>();
		// I obtain the information I need
		int coinsToPay;
		City cityTo=null;
		City cityFrom=board.findKingCity();
		coinsToPay=board.countDistance(cityFrom, cityTo)*2;
		if(player.getCoins()>=coinsToPay)
			player.removeCoins(coinsToPay);
		else throw new UnsufficientCoinsException();
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
	 * @return
	 */
	public void performAdditionalMainAction(Player player) {
		int choice=0;
		if (player.getNumberOfAssistants() > 3) {
			
			player.removeMoreAssistants(3);
			do{
			showMainActions(player);
			try {
				choice = player.getConnector().receiveIntFromClient();
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				logger.log(Level.FINEST, "Error: couldn't receive from client\n", e);
			}
			}while(choice != 1 && choice != 2 && choice != 3 && choice != 4);
			mainActions(player, choice);
		}
	}

	/**
	 * NEEDS REVISION: this method must not return a boolean: the try/catch must
	 * be handled inside a while loop untile the move is correctly performed.
	 * 
	 * @return
	 */
	public void electCouncillor(Player player) {
		String regionName = "";
		String councillorColor = "";
		boolean checkCouncillorColor = true;
		do {
			try {
				player.getConnector().writeToClient("Which region do you want elect a councillor?");
			} catch (RemoteException e) {
				logger.log(Level.SEVERE, "Error: couldn't write to client!", e);
			}
			try {
				regionName = player.getConnector().receiveStringFromClient();
			} catch (RemoteException e) {
				logger.log(Level.SEVERE, "Error: couldn't write to client!", e);
			}

			regionName = regionName.toUpperCase();
			regionName = regionName.trim();
		} while (!checkCorrectRegionName(regionName));
		do {
			try {
				player.getConnector().writeToClient("Which color do you want to choose?");
			} catch (RemoteException e) {
				logger.log(Level.SEVERE, "Error: couldn't write to client!", e);
			}
			try {
				councillorColor = player.getConnector().receiveStringFromClient();
			} catch (RemoteException e) {
				logger.log(Level.SEVERE, "Error: couldn't write to client!", e);
			}
			councillorColor = councillorColor.toUpperCase();
			councillorColor = councillorColor.trim();

			Region region = this.getRegion(regionName);
			try {
				region.electCouncillor(councillorColor);
			} catch (CouncillorNotFoundException e) {
				try {
					player.getConnector().writeToClient(e.showError());
				} catch (RemoteException e1) {
					logger.log(Level.SEVERE, "Error: couldn't write to client!", e1);
				}
				checkCouncillorColor = false;
			}
		} while (!checkCouncillorColor);

		player.addCoins(4);
		try {
			player.getConnector().writeToClient("You have elect a new councillor!");
		} catch (RemoteException e) {
			logger.log(Level.SEVERE, "Error: couldn't write to client!", e);
		}
	}

	/**
	 * NEEDS REVISION: this method must not return a boolean: the try/catch must
	 * be handled inside a while loop until the move is correctly performed.
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
	public boolean changeBusinessPermitTiles(Player player) {
		String regionName = "";
		do {
			try {
				player.getConnector().writeToClient("Which region name do you want to switch permit tile?");
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't write to client", e);
			}
			try {
				regionName = player.getConnector().receiveStringFromClient();
			} catch (RemoteException e) {
				logger.log(Level.INFO, "Error: couldn't receive from client", e);
			}
		} while (!checkCorrectRegionName(regionName));

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

		try {
			connector.writeToClient("You have built a new Emporium!");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
	}

	/**
	 * @return
	 */
	public boolean sendAssistantToElectCouncillor(Player player) {
		boolean checkCouncillorColor = true;
		String councillorColor = "";
		String regionName = "";
		if (player.getNumberOfAssistants() >= 1) {

			do {
				try {
					player.getConnector().writeToClient("Which region do you want elect a councillor?");
				} catch (RemoteException e) {
					logger.log(Level.SEVERE, "Error: couldn't write to client!", e);
				}
				try {
					regionName = player.getConnector().receiveStringFromClient();
				} catch (RemoteException e) {
					logger.log(Level.SEVERE, "Error: couldn't write to client!", e);
				}

				regionName = regionName.toUpperCase();
				regionName = regionName.trim();
			} while (!checkCorrectRegionName(regionName));

			do {
				try {
					player.getConnector().writeToClient("Which color do you want to choose?");
				} catch (RemoteException e) {
					logger.log(Level.SEVERE, "Error: couldn't write to client!", e);
				}
				try {
					councillorColor = player.getConnector().receiveStringFromClient();
				} catch (RemoteException e) {
					logger.log(Level.SEVERE, "Error: couldn't write to client!", e);
				}
				councillorColor = councillorColor.toUpperCase();
				councillorColor = councillorColor.trim();

				Region region = this.getRegion(regionName);
				try {
					region.electCouncillor(councillorColor);
				} catch (CouncillorNotFoundException e) {
					try {
						player.getConnector().writeToClient(e.showError());
					} catch (RemoteException e1) {
						logger.log(Level.SEVERE, "Error: couldn't write to client!", e1);
					}
					checkCouncillorColor = false;
				}
			} while (!checkCouncillorColor);
			return true;
		} else
			return false;
	}

	/**
	 * @return
	 */
	public void addPlayer(ClientSideConnectorInt connector, ServerSideConnectorInt serverSideConnector, int id) {// To
															// add
															// UML
		// scheme
		serverSideConnector.setPlayerId(id);
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
	 * @param player
	 */
	public void showMainActions(Player player) {
		try {
			player.getConnector().writeToClient(
					"MAIN ACTIONS\n1)Buy Permit Tile\n2)Build and emporium using King's help\n3)Elect Councillor\n4)Build and emporium using Permit Tile");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
	}

	/**
	 * 
	 * @param player
	 */
	public void showQuickActions(Player player) {
		try {
			player.getConnector().writeToClient(
					"QUICK ACTIONS\n1)Engage an Assistant\n2)Switch Permit Tile\n3)Send an Assistant to Elect a Coucillor\n4)Do another Main Action\n5)Jump Quick Action");
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
		int coins = player.getCoins();
		try {
			player.getConnector().writeToClient("COINS: " + coins + " ");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
	}

	/**
	 * 
	 * @param player
	 */
	public void showPlayerAssistants(Player player) {
		int assistants = player.getNumberOfAssistants();
		try {
			player.getConnector().writeToClient("ASSISTANTS: " + assistants + " ");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
	}

	/**
	 * 
	 * @param player
	 */
	public void showPlayerVictoryPoints(Player player) {
		int victoryPoints = player.getVictoryPoints();
		try {
			player.getConnector().writeToClient("VICTORY POINTS: " + victoryPoints + " ");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
	}

	/**
	 * 
	 * @param player
	 */
	public void showPlayerNumberOfEmporium(Player player) {
		int emporium = player.getNumberOfEmporium();
		try {
			player.getConnector().writeToClient("NUMBER OF EMPORIUM: " + emporium + " ");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: couldn't write to client", e);
		}
	}

	/**
	 * 
	 * @param player
	 */
	public void showPlayerPermitTileUnused(Player player) {
		String permitTileUnused = player.showPermitTileCards();
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
		String permitTileUsed = player.showUsedPermitTileCards();
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
	public boolean checkCorrectRegionName(String regionName) {
		Region tempRegion = null;
		tempRegion = getRegion(regionName);
		return tempRegion != null;
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

	public boolean hasBuiltLastEmporium(Player player) {
		return player.getNumberOfEmporium() > 0;
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

	public void getBoardStatus(int playerId) {
		try {
			players.get(playerId).getConnector().sendToClient(new Packet(board.toString()));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void setConfigObject(String messageString, int playerId) {
		if(mapStatus==0 && creator.getId()==playerId)
			new BoardConfiguration(playerId,messageString,numberOfPlayers);
		waitingForPlayers();
		countdown();
		setDefinitiveNumberOfPlayers();
		boardInitialization();
		mapConfiguration(creator.getConnector());
		play();
	}

	public void evaluateAction(Action action, int playerId) {

	}

	public void addLink(String messageString, int playerId) {
	}

	public void removeLink(String messageString, int playerId) {
	}

	public void messageFromClient(String messageString, int playerId) {
	}

	public void setExistingConf(int configId, int playerId) {
	}

	public void buyEvent(MarketEvent marketEvent, int playerId) {
	}

	public void sellEvent(MarketEvent marketEvent, int playerId) {
	}
}