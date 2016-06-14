package client.actions;

import client.view.cli.ClientOutputPrinter;
import controller.Client.ClientSideConnector;
import controller.Client.SocketInputOutputThread;
import controller.*;
import exceptions.InvalidInputException;
import model.Board;
import model.ConfigObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 * This class represents the controller client side to verify that a specified
 * move is valid in order to send it to the server
 * 
 * @author Riccardo
 *
 */
public class ClientPacketController {

	private static final Logger logger = Logger.getLogger(ClientPacketController.class.getName());
	private final String ADDRESS = "localhost";
	private final int PORT = 2000;
	private ClientSideConnector clientSideConnector;
	private RMIConnectionInt rmiConnectionInt;
	private ServerSideConnectorInt packetSenderInt;
	private Scanner input;
	private SocketInputOutputThread socketInputOutputThread;

	public ClientPacketController() {
		logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
		input = new Scanner(System.in);
	}

	public void performNewAction() {
		String type = null;
		String id;
		int num;
		boolean proceed = false;
		ClientOutputPrinter.printLine(
				"Insert the type of action you would like to perform:\n Type 'main' for Main Action or 'quick' for Quick Action");
		while (!proceed) {
			type = input.nextLine();
			try {
				type = verifyTypeOfAction(type);
				proceed = true;
			} catch (InvalidInputException e) {
				ClientOutputPrinter.printLine(e.printError());
			}
		}
		proceed = false;
		ClientOutputPrinter.printLine("Insert the ID of the action you would like to perform:");
		showActions(type);
		while (!proceed) {
			id = input.nextLine();
			try {
				num = verifyActionID(id);
				proceed = true;
				new ActionCreator(type, num, packetSenderInt);
			} catch (InvalidInputException e) {
				ClientOutputPrinter.printLine(e.printError());
			}
		}
		proceed = false;
	}

	public void showActions(String type) {
		if ("MAIN".equals(type)) {
			ClientOutputPrinter.printLine(
					"MAIN ACTIONS\n1)Buy Permit Tile\n2)Build an emporium using King's help\n3)Elect a Councillor\n4)Build an emporium using Permit Tile");
		} else {
			ClientOutputPrinter.printLine(
					"QUICK ACTIONS\n1)Engage an Assistant\n2)Switch Permit Tile\n3)Send an Assistant to Elect a Councillor\n4)Perform an additional Main Action\n5)Skip Quick Action");
		}
	}

	public String verifyTypeOfAction(String s) throws InvalidInputException {
		s = s.toUpperCase();
		if (!"QUICK".equals(s) && !"MAIN".equals(s))
			throw new InvalidInputException();
		return s;
	}

	public int verifyActionID(String id) throws InvalidInputException {
		int num = Integer.parseInt(id);
		if (num < 1 || num > 4)
			throw new InvalidInputException();
		return num;
	}

	public void sellItemOnMarket() {
		int choice;
		Scanner stringInput = new Scanner(System.in);
		boolean proceed = false;
		while (!proceed) {
			ClientOutputPrinter.printLine(
					"What item would you like to sell?\n1) Assistant\n2)Politic Card\n3) Permit Tile\b4) Return back...");
			choice = input.nextInt();
			try {
				switch (choice) {

				case 1:
					packetSenderInt.sendToServer(new Packet(new MarketEventSell()));
					break;
				case 2:
					ClientOutputPrinter.printLine("Type the color of the Politic Card to sell:");
					String color = stringInput.nextLine();
					packetSenderInt.sendToServer(new Packet(new MarketEventSell(color)));
					break;
				case 3:
					ClientOutputPrinter.printLine("Choose the ID of the Permit Tile to sell:");
					int id = input.nextInt();
					packetSenderInt.sendToServer(new Packet(new MarketEventSell(id)));
					break;
				case 4:
					return;
				default:
					ClientOutputPrinter.printLine("Invalid input, please retry...");
					break;

				}
			} catch (RemoteException e) {
				ClientOutputPrinter.printLine(e.getMessage());
			}
		}
	}

	public void buyItemOnMarket() {
		int id;
		ClientOutputPrinter.printLine("Type the Item ID you would like to buy:");
		id = input.nextInt();
		try {
			packetSenderInt.sendToServer(new Packet(new MarketEventBuy(id)));
		} catch (RemoteException e) {
			ClientOutputPrinter.printLine(e.getMessage());
		}
	}

	/**
	 * NEEDS IMPLEMENTATION
	 */
	public void requestBoardStatus() {
		try {
			packetSenderInt.sendToServer(new Packet("REQUESTBOARDSTATUS"));
		} catch (RemoteException e) {
			ClientOutputPrinter.printLine(e.getMessage());
		}
	}

	/**
	 * NEEDS IMPLEMENTATION
	 */
	public void requestPlayerStatus() {
		try {
			packetSenderInt.sendToServer(new Packet("REQUESTPLAYERSTATUS"));
		} catch (RemoteException e) {
			ClientOutputPrinter.printLine(e.getMessage());
		}
	}

	/**
	 * @return true if the client is the creator of the match or false otherwise
	 * @throws RemoteException
	 */
	public void connect() throws RemoteException {
		ClientOutputPrinter.printLine("Choose your connection type:\n1)RMI\n2)Socket");
		boolean proceed = false;
		while (!proceed) {
			int choice = Integer.parseInt(input.nextLine());
			switch (choice) {
			case 1:
				this.startRMIConnection();
				proceed = true;
				break;
			case 2:
				this.startSocketConnection();
				proceed = true;
				break;
			default:
				ClientOutputPrinter.printLine("Error: invalid input... please retry!");
			}
		}

	}

	/**
	 * NEEDS IMPLEMENTATION
	 */
	public void disconnect() {

	}

	public void startSocketConnection() {
		try {
			packetSenderInt = new SocketInputOutputThread(new Socket(ADDRESS, PORT));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startRMIConnection() {
		try {
			clientSideConnector = new ClientSideConnector();
			rmiConnectionInt = (RMIConnectionInt) Naming.lookup("rmi://localhost/registry");
			packetSenderInt = rmiConnectionInt.connect(clientSideConnector);
		} catch (NotBoundException e) {
			logger.log(Level.FINEST, "Error: the object you were looking for is not bounded", e);
		} catch (MalformedURLException e) {
			logger.log(Level.FINEST, "Error: the URL specified is invalid", e);
		} catch (RemoteException e) {
			logger.log(Level.FINEST, "Error: RemoteException was thrown", e);
		}
	}

	public void boardConfiguration() {
		boolean correctAnswer = false;
		int choice = 0;
		while (!correctAnswer) {
			ClientOutputPrinter.printLine("1) Create a new board configuration\n2) Choose an existing configuration");
			choice = input.nextInt();
			if (choice != 1 && choice != 2) {
				ClientOutputPrinter.printLine("ERROR: incorrect input. Please retry");
			} else
				correctAnswer = true;
		}
		if (choice == 1) {
			newConfiguration();
		} else {
			boolean finish = false;
			while (!finish) {
				try {
					packetSenderInt.sendToServer(new Packet("REQUESTCONFIGURATIONS"));
				} catch (RemoteException e) {
					ClientOutputPrinter.printLine(e.getMessage());
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					ClientOutputPrinter.printLine(e.getMessage());
				}
				ClientOutputPrinter.printLine("Choose the configuration ID:");
				choice = input.nextInt();

				try {
					packetSenderInt.sendToServer(new Packet(new Integer(choice)));
				} catch (RemoteException e) {
					ClientOutputPrinter.printLine(e.getMessage());
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					ClientOutputPrinter.printLine(e.getMessage());
				}
				ClientOutputPrinter.printLine("Press any key to continue or press 1 to repeat the confguration");
				choice = input.nextInt();
				if (choice != 1)
					finish = true;

			}

		}

	}

	/**
	 * This method permit to modify the game map
	 *
	 * @param player
	 *            connector
	 */
	public void mapConfiguration() {
		boolean stop = false;
		int choice = 0;
		while (!stop) {

			ClientOutputPrinter
					.printLine("Next choice?\n1) New connection\n2)Remove connection\n3) Go on\n4) View board status ");
			choice = input.nextInt();

			switch (choice) {
			case 1:
				editConnection("ADD");
				break;
			case 2:
				editConnection("REMOVE");
				break;
			case 3:
				stop = true;
				break;
			case 4:
				try {
					packetSenderInt.sendToServer(new Packet());
				} catch (RemoteException e) {
					ClientOutputPrinter.printLine(e.getMessage());
				}
				break;
			default:
				ClientOutputPrinter.printLine("Invalid input.");
			}
		}
	}

	public void editConnection(String choice) {
		String city1 = "null";
		String city2 = "null";

		do {
			ClientOutputPrinter.printLine("Insert the FIRST letter of the first city:\n");
			city1 = input.nextLine();
			ClientOutputPrinter.printLine("Insert the FIRST letter of the second city:\n");
			city2 = input.nextLine();
		} while (city1.length() > 1 || city2.length() > 1 || city2.equals(city1));
		city1 = city1.toUpperCase();
		city2 = city2.toUpperCase();
		try {
			if (choice.equals("ADD"))
				packetSenderInt.sendToServer(new Packet(city1, city2, "ADD"));
			else
				packetSenderInt.sendToServer(new Packet(city1, city2, "REMOVE"));
		} catch (RemoteException e) {
			ClientOutputPrinter.printLine(e.getMessage());
		}

	}

	/**
	 * NEEDS CODE QUALITY ADJUSTEMENTS
	 *
	 * @param playerConnector
	 */
	public void newConfiguration() {
		String parameters = "";
		int numberOfPlayers = 0, linksBetweenCities = 0, rewardTokenBonusNumber = 0, permitTileBonusNumber = 0,
				nobilityTrackBonusNumber = 0;
		boolean stop = false;
		ClientOutputPrinter.printLine(
				"NEW CONFIGURATION:\nInsert the configuration parameters in this order, and each number must be separated by a space");
		ClientOutputPrinter.printLine(
				"Maximum number of players, Reward Token bonus number, Permit Tiles bonus number, Nobility Track bonus number, Maximum number of outgoing connections from each City");

		while (!stop) {
			parameters = input.nextLine();
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
				ClientOutputPrinter.printLine("Error: Expected integers values! Retry!");
			}
			try {
				parametersValidation(numberOfPlayers, rewardTokenBonusNumber, permitTileBonusNumber,
						nobilityTrackBonusNumber, linksBetweenCities);
				packetSenderInt.sendToServer(new Packet(new ConfigObject(numberOfPlayers, rewardTokenBonusNumber,
						permitTileBonusNumber, nobilityTrackBonusNumber, linksBetweenCities)));
				stop = true;
			} catch (InvalidInputException e) {
				ClientOutputPrinter.printLine(e.printError());
			} catch (RemoteException e) {
				ClientOutputPrinter.printLine(e.getMessage());
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
				|| (permitTileBonusNumber < 1 || permitTileBonusNumber > 3)
				|| (nobilityTrackBonusNumber < 1 || nobilityTrackBonusNumber > 3))
			throw new InvalidInputException();
		if (linksBetweenCities < 2 && linksBetweenCities > 4)
			throw new InvalidInputException();
	}
}