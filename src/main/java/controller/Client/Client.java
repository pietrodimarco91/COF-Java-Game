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
			if (controller.connect()) {
				controller.boardConfiguration();
			} else
				controller.waitStart();
			play();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	public void play() {
		int choice;
		while (true) {
			ClientOutputPrinter.printLine("Next choice?\n1) Perform action\n2) Request board status\n3) Disconnect\n4) Sell Item on Market\n5) Buy Item on Market\n6) Request Player status");
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