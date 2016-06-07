package client.actions;

import client.view.cli.ClientOutputPrinter;
import controller.Client.ClientSideRMIConnector;
import controller.Client.SocketInputOutputThread;
import controller.RMIConnectionInt;
import controller.ServerSideRMIConnectorInt;
import exceptions.InvalidInputException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
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
public class ActionController {

	private static final Logger logger = Logger.getLogger(ActionController.class.getName());
	private final String ADDRESS="localhost";
	private final int PORT=2000;
	private ClientSideRMIConnector clientSideRMIConnector;
	private RMIConnectionInt rmiConnectionInt;
	private ServerSideRMIConnectorInt actionSenderInt;
	private Scanner input;
	private SocketInputOutputThread socketInputOutputThread;

	public ActionController() {
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
				new ActionCreator(type, num,actionSenderInt);
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

	/**
	 * NEEDS IMPLEMENTATION
	 */
	public void requestBoardStatus() {

	}

	/**
	 * NEEDS IMPLEMENTATION
	 */
	public void requestMyPlayerStatus() {

	}

	public void connect() {
		ClientOutputPrinter.printLine("Choose your connection type:\n1)RMI\n2)Socket");
		int choice = Integer.parseInt(input.nextLine());
		boolean proceed = false;
		while (!proceed) {
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
			actionSenderInt=new SocketInputOutputThread(new Socket(ADDRESS,PORT));
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
}
