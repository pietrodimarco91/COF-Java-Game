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

	/**
	 * Default constructor
	 */
	public Client() {
		controller = chooseController();
		start();
	}
	
	public void start() {
		controller.welcome();
		try {
			controller.connect();
			controller.initialConfiguration();
			controller.play();
		} catch (RemoteException e) {
			ClientOutputPrinter.printLine(e.getMessage());
		}
	}

	private ClientController chooseController() {
		boolean proceed = false;
		ClientOutputPrinter.printLine(
				"Please, type 'gui' if you want to play with Graphical User Interface or 'cli' if you want to play from Command Line...");
		while (!proceed) {
			try {
				String choice = input.nextLine();
				if (choice.equals("cli"))
					return new ClientCLIController();
				else if (choice.equals("gui"))
					return new ClientGUIController();
				else
					throw new InvalidInputException();
			} catch (InvalidInputException e) {
				ClientOutputPrinter.printLine(e.printError());
			}
		}
		return null;
	}
}