package controller.Client;

import client.actions.ActionController;
import client.view.cli.ClientOutputPrinter;

import java.util.Scanner;

/**
 * 
 */
public class Client {

	private int currentPlayerID;

	private Scanner input = new Scanner(System.in);

	private ActionController controller;

	/**
	 * Default constructor
	 */
	public Client() {
		controller = new ActionController();
		currentPlayerID = -1; // this is an important parameter that each client
								// should be given after correctly connected. It
								// may be used to state whether he is a creator
								// or not, and if not he needs to quit all the
								// input/output streams with the server during
								// the configuration
		welcome();
		ClientOutputPrinter.printLine("Please, first of all you need to connect to the game server...");
		controller.connect();
		controller.waitStart();
		play();
	}

	public void play() {
		int choice;
		while (true) {
			ClientOutputPrinter.printLine(
					"Next choice?\n1) Perform action\n2) Request board status\n3) Request my player's status4) Disconnect");
			choice = input.nextInt();
			switch (choice) {
			case 1:
				controller.performNewAction();
				break;
			case 2:
				controller.requestBoardStatus();
				break;
			case 3:
				controller.requestMyPlayerStatus();
				break;
			case 4:
				controller.disconnect();
				break;
			default:
				ClientOutputPrinter.printLine("Invalid choice... please retry!");
			}
		}
	}

	public void welcome() {
		ClientOutputPrinter.printLine("Welcome to a new session of 'Council Of Four' Game!");
	}
}