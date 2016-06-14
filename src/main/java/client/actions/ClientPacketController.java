package client.actions;

import client.view.cli.ClientOutputPrinter;
import controller.Client.ClientSideConnector;
import controller.Client.SocketInputOutputThread;
import controller.*;
import exceptions.InvalidInputException;
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
	private ServerSideConnectorInt actionSenderInt;
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
				new ActionCreator(type, num, actionSenderInt);
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
			switch (choice) {
			case 1:
				actionSenderInt.sendToServer(new Packet(new MarketEventSell()));
				break;
			case 2:
				ClientOutputPrinter.printLine("Type the color of the Politic Card to sell:");
				String color = stringInput.nextLine();
				actionSenderInt.sendToServer(new Packet(new MarketEventSell(color)));
				break;
			case 3:
				ClientOutputPrinter.printLine("Choose the ID of the Permit Tile to sell:");
				int id = input.nextInt();
				actionSenderInt.sendToServer(new Packet(new MarketEventSell(id)));
				break;
			case 4:
				return;
			default:
				ClientOutputPrinter.printLine("Invalid input, please retry...");
				break;
			}
		}
	}

	public void buyItemOnMarket() {
		int id;
		ClientOutputPrinter.printLine("Type the Item ID you would like to buy:");
		id=input.nextInt();
		actionSenderInt.sendToServer(new Packet(new MarketEventBuy(id)));
	}

	/**
	 * NEEDS IMPLEMENTATION
	 */
	public void requestBoardStatus() {

	}

	/**
	 * NEEDS IMPLEMENTATION
	 */
	public void requestPlayerStatus() {

	}

	/**
	 * @return true if the client is the creator of the match or false otherwise
	 * @throws RemoteException
	 */
	public boolean connect() throws RemoteException {
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
		return actionSenderInt.checkCreator();
	}

	/**
	 * NEEDS IMPLEMENTATION
	 */
	public void disconnect() {

	}

	public void startSocketConnection() {
		try {
			actionSenderInt = new SocketInputOutputThread(new Socket(ADDRESS, PORT));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void startRMIConnection() {
		try {
			clientSideRMIConnector = new ClientSideRMIConnector();
			rmiConnectionInt = (RMIConnectionInt) Naming.lookup("rmi://localhost/registry");
			actionSenderInt = rmiConnectionInt.connect(clientSideRMIConnector);
		} catch (NotBoundException e) {
			logger.log(Level.FINEST, "Error: the object you were looking for is not bounded", e);
		} catch (MalformedURLException e) {
			logger.log(Level.FINEST, "Error: the URL specified is invalid", e);
		} catch (RemoteException e) {
			logger.log(Level.FINEST, "Error: RemoteException was thrown", e);
		}
	}

	public void waitStart() {
		ClientOutputPrinter.printLine("Wait while the creator configures the gameboard...");
		actionSenderInt.waitStart();
	}

	public void boardConfiguration() {
		boolean correctAnswer = false;
		int choice = 0;
		while (!correctAnswer) {
			ClientOutputPrinter.printLine(
					"1) Create a new board configuration\n2) Choose an existing configuration\n3) Return back...");
			choice = input.nextInt();
			if (choice == 3)
				return;
			if (choice != 1 && choice != 2) {
				ClientOutputPrinter.printLine("ERROR: incorrect input. Please retry\n");
			} else
				correctAnswer = true;
		}
		if (choice == 1) {
			newConfiguration();
		} else {
			int id;
			boolean correctID = false;
			while (!correctID) {
				ClientOutputPrinter.printLine("Choose a configuration by typing its ID\n");
				try {
					id = input.nextInt();
					correctID = true;
				} catch (NumberFormatException e) {
					ClientOutputPrinter.printLine("Invalid input... please retry");
				}
			}
			actionSenderInt.sendToServer(new Packet(id));
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
				actionSenderInt.sendToServer(new Packet(new ConfigObject(numberOfPlayers, rewardTokenBonusNumber,
						permitTileBonusNumber, nobilityTrackBonusNumber, linksBetweenCities)));
				stop = true;
			} catch (InvalidInputException e) {
				ClientOutputPrinter.printLine(e.printError());
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