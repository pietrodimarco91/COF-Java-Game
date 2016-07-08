package client.controller;

import client.view.cli.ClientOutputPrinter;
import exceptions.InvalidInputException;

import java.rmi.RemoteException;
import java.util.Scanner;

/**
 * 
 */
public class Client {

	private Scanner input = new Scanner(System.in);

	private ClientController controller;

	private String args[];

	/**
	 * Default constructor
	 */
	public Client(String[] args) {
		controller = chooseController();
		this.args = args;
	}

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