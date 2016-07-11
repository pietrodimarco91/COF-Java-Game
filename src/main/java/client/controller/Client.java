package client.controller;

import client.view.cli.ClientOutputPrinter;
import exceptions.InvalidInputException;

import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * This class is the main object that will be execute Client-side to run the
 * game. Its fundamental duty is to choose the type of the client-controller to
 * use for the game, which could be chosen between CLI and GUI.
 */
public class Client {

	private Scanner input = new Scanner(System.in);

	/**
	 * The type of the controller chosen by the client for this game session
	 */
	private ClientController controller;

	private String args[];

	/**
	 * Default constructor: creates a new client and instantiates the controller
	 */
	public Client(String[] args) {
		controller = chooseController();
		this.args = args;
	}

	/**
	 * The main method executed client side to start playing. It allows to
	 * connect to the game server. This method will be executed in different
	 * ways depending on the controller chosen by the client
	 */
	public void start() {
		controller.welcome(args);
		try {
			if (controller instanceof ClientCLIController) {
				controller.connect();
				controller.initialConfiguration();
				controller.play();
			}
		} catch (RemoteException e) {
			ClientOutputPrinter.printLine(e.getMessage());
		}
	}

	/**
	 * This method allows to choose the controller used by the Client: GUI or
	 * CLI
	 * 
	 * @return the type of the controller chosen by the client
	 */
	private ClientController chooseController() {
		boolean proceed = false;
		ClientController controller = null;
		ClientOutputPrinter.printLine(
				"Please, type 'gui' if you want to play with Graphical User Interface or 'cli' if you want to play from Command Line...");
		while (!proceed) {
			try {
				String choice = input.nextLine();
				if (choice.equals("cli")) {
					proceed = true;
					controller = new ClientCLIController();
				} else if (choice.equals("gui")) {
					proceed = true;
					controller = new ClientGUIController();
				} else
					throw new InvalidInputException();
			} catch (InvalidInputException e) {
				ClientOutputPrinter.printLine(e.printError());
			}
		}
		return controller;
	}
}