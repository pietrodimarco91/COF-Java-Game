package client.controller;

import client.actions.ActionCreator;
import client.view.cli.ClientOutputPrinter;
import controller.*;
import exceptions.InvalidInputException;
import model.ConfigObject;
import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 * This class represents the CLI controller client side
 * which controls everything the client wants to do
 * 
 * @author Riccardo
 *
 */
public class ClientCLIController extends ClientController {

	private static final Logger logger = Logger.getLogger(ClientCLIController.class.getName());
	private Scanner input;
	private String nickName;

	public ClientCLIController() {
		logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
		input = new Scanner(System.in);
	}
	
	public void welcome() {
		ClientOutputPrinter.printLine("Welcome to a new session of 'Council Of Four' Game!");
		do {
			ClientOutputPrinter.printLine("Please, choose a nickname with at least 4 char and without space:");
			nickName = input.nextLine();
			checkCorrectNickName(nickName);
		} while (checkCorrectNickName(nickName));
	}
	
	/**
	 * @throws RemoteException
	 */
	public void connect() throws RemoteException {
		ClientOutputPrinter.printLine("Please, you need to connect to the Game Server...");
		ClientOutputPrinter.printLine("Choose your connection type:\n1)RMI\n2)Socket");
		boolean proceed = false;
		while (!proceed) {
			try {
				int choice = Integer.parseInt(input.nextLine());
				switch (choice) {
				case 1:
					this.startRMIConnection(nickName);
					proceed = true;
					break;
				case 2:
					this.startSocketConnection(nickName);
					proceed = true;
					break;
				default:
					ClientOutputPrinter.printLine("Error: invalid input... please retry!");
				}
			} catch (NumberFormatException e) {
				ClientOutputPrinter.printLine("Invalid input! Please insert an integer");
			}
		}
	}

	public void initialConfiguration() {
		boolean correct = false;
		do {
			ClientOutputPrinter.printLine(
					"If you are the match creator press 1 otherwise wait for the board and the map to be configured...Then press any key but 1 to continue..");
			try {
				String choice = input.nextLine();
				if (choice.equals("1")) {
					boardConfiguration();
					mapConfiguration();
				}
				correct = true;
			} catch (InputMismatchException e) {
				ClientOutputPrinter.printLine("Please insert a correct input!");
			} catch (NoSuchElementException e) {
				ClientOutputPrinter.printLine("Please insert a correct input!");
			}
		} while (!correct);
	}

	public void play() {
		int choice;
		boolean quit = false;
		while (!quit) {
			ClientOutputPrinter.printLine(
					"||*** MAIN MENU ***||\n1) Perform action\n2) Request board status\n3) Quit\n4) Sell Item on Market\n5) Buy Item on Market\n6) Request Player status\n7) Map configuration\n8) Chat");
			try {
				choice = Integer.parseInt(input.nextLine());
				switch (choice) {
				case 1:
					performNewAction();
					break;
				case 2:
					requestBoardStatus();
					break;
				case 3:
					disconnect();
					quit = true;
					break;
				case 4:
					sellItemOnMarket();
					break;
				case 5:
					buyItemOnMarket();
					break;
				case 6:
					requestPlayerStatus();
					break;
				case 7:
					mapConfiguration();
					break;
				case 8:
					chat();
					break;
				default:
					ClientOutputPrinter.printLine("Invalid choice... please retry!");
				}
			} catch (NumberFormatException e) {
				ClientOutputPrinter.printLine("Invalid choice... please retry!");
			}
		}
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
				new ActionCreator(type, num, super.getServerConnector());
			} catch (InvalidInputException e) {
				ClientOutputPrinter.printLine(e.printError());
			} catch (NumberFormatException e) {
				ClientOutputPrinter.printLine("Invalid input! Please insert an integer");
			}
		}
		proceed = false;
	}

	public void sellItemOnMarket() {
		int choice, price;
		boolean proceed = false;
		while (!proceed) {
			try {
				ClientOutputPrinter.printLine(
						"What item would you like to sell?\n1) Assistant\n2)Politic Card\n3) Permit Tile\n4) Return back...");
				choice = Integer.parseInt(input.nextLine());
				ClientOutputPrinter.printLine("Choose the price (in coins) for your item:");
				price = Integer.parseInt(input.nextLine());

				switch (choice) {

				case 1:
					super.getServerConnector().sendToServer(new Packet(new MarketEventSell(price)));
					proceed = true;
					break;
				case 2:
					ClientOutputPrinter.printLine("Type the color of the Politic Card to sell:");
					String color = input.nextLine();
					super.getServerConnector().sendToServer(new Packet(new MarketEventSell(color, price)));
					proceed = true;
					break;
				case 3:
					ClientOutputPrinter.printLine("Choose the ID of the Permit Tile to sell:");
					int id = Integer.parseInt(input.nextLine());
					super.getServerConnector().sendToServer(new Packet(new MarketEventSell(id, price)));
					proceed = true;
					break;
				case 4:
					return;
				default:
					ClientOutputPrinter.printLine("Invalid input, please retry...");
					break;

				}
			} catch (RemoteException e) {
				ClientOutputPrinter.printLine(e.getMessage());
			} catch (NumberFormatException e) {
				ClientOutputPrinter.printLine("Invalid input! Please insert an integer");
			}
		}
	}

	public void buyItemOnMarket() {
		int id;
		ClientOutputPrinter.printLine("Type the Item ID you would like to buy:");
		try {
			id = Integer.parseInt(input.nextLine());
			super.getServerConnector().sendToServer(new Packet(new MarketEventBuy(id)));
		} catch (RemoteException e) {
			ClientOutputPrinter.printLine(e.getMessage());
		} catch (NumberFormatException e) {
			ClientOutputPrinter.printLine("Invalid input! Please insert an integer");
		}
	}

	/**
	 * 
	 */
	public void requestBoardStatus() {
		try {
			super.getServerConnector().sendToServer(new Packet());
		} catch (RemoteException e) {
			ClientOutputPrinter.printLine(e.getMessage());
		}
	}

	/**
	 * 
	 */
	public void requestPlayerStatus() {
		try {
			super.getServerConnector().sendToServer(new Packet("REQUESTPLAYERSTATUS"));
		} catch (RemoteException e) {
			ClientOutputPrinter.printLine(e.getMessage());
		}
	}

	public void boardConfiguration() {
		boolean correctAnswer = false;
		int choice = 0;
		while (!correctAnswer) {
			ClientOutputPrinter.printLine("1) Create a new board configuration\n2) Choose an existing configuration");
			choice = Integer.parseInt(input.nextLine());
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
					super.getServerConnector().sendToServer(new Packet("REQUESTCONFIG"));
				} catch (RemoteException e) {
					ClientOutputPrinter.printLine(e.getMessage());
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					ClientOutputPrinter.printLine(e.getMessage());
					Thread.currentThread().interrupt();
				}
				ClientOutputPrinter.printLine("Choose the configuration ID:");
				try {
					choice = Integer.parseInt(input.nextLine());
					super.getServerConnector().sendToServer(new Packet(new Integer(choice)));
				} catch (RemoteException e) {
					ClientOutputPrinter.printLine(e.getMessage());
				} catch (NumberFormatException e) {
					ClientOutputPrinter.printLine("Invalid input! Please insert an integer");
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					ClientOutputPrinter.printLine(e.getMessage());
					Thread.currentThread().interrupt();
				}
				ClientOutputPrinter.printLine("Press any key to continue or press 1 to repeat the confguration");
				try {
					choice = Integer.parseInt(input.nextLine());
					if (choice != 1)
						finish = true;
				} catch (NumberFormatException e) {
					ClientOutputPrinter.printLine("Error! Expected integer but it was a string...");
				}

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

			ClientOutputPrinter.printLine(
					"||*** MAP CONFIGURATION MENU ***||\n1) New connection\n2)Remove connection\n3) Go on\n4) View board status\n5) Count distance");
			choice = Integer.parseInt(input.nextLine());

			switch (choice) {
			case 1:
				editConnection("ADD");
				break;
			case 2:
				editConnection("REMOVE");
				break;
			case 3:
				try {
					super.getServerConnector().sendToServer(new Packet("FINISHMAPCONFIG"));
				} catch (RemoteException e) {
					ClientOutputPrinter.printLine(e.getMessage());
				}
				stop = true;
				break;
			case 4:
				try {
					super.getServerConnector().sendToServer(new Packet());
				} catch (RemoteException e) {
					ClientOutputPrinter.printLine(e.getMessage());
				}
				break;
			case 5:
				countDistance();
				break;
			default:
				ClientOutputPrinter.printLine("Invalid input.");
			}
		}
	}

	public void countDistance() {
		String city1 = "null";
		String city2 = "null";

		do {
			ClientOutputPrinter.printLine("Insert the FIRST letter of the first city:\n");
			city1 = input.nextLine();
			ClientOutputPrinter.printLine("Insert the FIRST letter of the second city:\n");
			city2 = input.nextLine();
		} while (city1.length() > 1 || city2.length() > 1 || city2.equals(city1)
				|| !("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".contains(city1))
				|| !("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".contains(city2)));
		city1 = city1.toUpperCase();
		city2 = city2.toUpperCase();
		try {
			super.getServerConnector().sendToServer(new Packet(city1, city2, "COUNTDISTANCE"));
		} catch (RemoteException e) {
			ClientOutputPrinter.printLine(e.getMessage());
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
		} while (city1.length() > 1 || city2.length() > 1 || city2.equals(city1)
				|| !("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".contains(city1))
				|| !("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".contains(city2)));
		city1 = city1.toUpperCase();
		city2 = city2.toUpperCase();
		try {
			if (choice.equals("ADD"))
				super.getServerConnector().sendToServer(new Packet(city1, city2, "ADD"));
			else
				super.getServerConnector().sendToServer(new Packet(city1, city2, "REMOVE"));
		} catch (RemoteException e) {
			ClientOutputPrinter.printLine(e.getMessage());
		}

	}

	/**
	 * 
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
				super.getServerConnector().sendToServer(new Packet(new ConfigObject(numberOfPlayers, rewardTokenBonusNumber,
						permitTileBonusNumber, nobilityTrackBonusNumber, linksBetweenCities)));
				stop = true;
			} catch (InvalidInputException e) {
				ClientOutputPrinter.printLine(e.printError());
			} catch (RemoteException e) {
				ClientOutputPrinter.printLine(e.getMessage());
			}
		}
	}

	public void chat() {
		ClientOutputPrinter.printLine("Write the message to send to the other players:");
		String message = input.nextLine();
		try {
			super.getServerConnector().sendToServer(new Packet(message, "***"));
		} catch (RemoteException e) {
			ClientOutputPrinter.printLine(e.getMessage());
		}
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
	
	public int verifyActionID(String id) throws InvalidInputException, NumberFormatException {
		int num = Integer.parseInt(id);
		if (num < 1 || num > 4)
			throw new InvalidInputException();
		return num;
	}
}