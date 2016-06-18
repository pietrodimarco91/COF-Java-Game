package controller.Client;

import client.actions.ClientPacketController;
import client.view.cli.ClientOutputPrinter;

import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * 
 */
public class Client {

	private int currentPlayerID;

	private Scanner input = new Scanner(System.in);

	private ClientPacketController controller;

	/**
	 * Default constructor
	 */
	public Client() {
		controller = new ClientPacketController();
		currentPlayerID = -1; // this is an important parameter that each client
								// should be given after correctly connected. It
								// may be used to state whether he is a creator
								// or not
		welcome();
		ClientOutputPrinter.printLine("Please, first of all you need to connect to the game server...");
		try {
			controller.connect();
			initialConfiguration();
			play();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void initialConfiguration() {
		ClientOutputPrinter.printLine(
				"If you are the match creator press 1 otherwise wait for the board and the map to be configured...Then press any number but 1 to continue..");
		int choice = input.nextInt();
		if (choice == 1) {
			controller.boardConfiguration();
			controller.mapConfiguration();
		}
	}

	public void play() {
		int choice;
		while (true) {
			ClientOutputPrinter.printLine(
					"||*** MAIN MENU ***||\n1) Perform action\n2) Request board status\n3) Disconnect\n4) Sell Item on Market\n5) Buy Item on Market\n6) Request Player status\n7) Map configuration");
			try {
				choice = input.nextInt();
				switch (choice) {
				case 1:
					controller.performNewAction();
					break;
				case 2:
					controller.requestBoardStatus();
					break;
				case 3:
					controller.disconnect();
					break;
				case 4:
					controller.sellItemOnMarket();
					break;
				case 5:
					controller.buyItemOnMarket();
					break;
				case 6:
					controller.requestPlayerStatus();
					break;
				case 7:
					controller.mapConfiguration();
					break;
				default:
					ClientOutputPrinter.printLine("Invalid choice... please retry!");
				}
			} catch (NumberFormatException e) {
				ClientOutputPrinter.printLine("Invalid choice... please retry!");
			}
		}
	}

	public void welcome() {
		ClientOutputPrinter.printLine("Welcome to a new session of 'Council Of Four' Game!");
	}
}