package client.actions;

import java.util.Scanner;

import client.view.cli.ClientOutputPrinter;
import exceptions.InvalidInputException;

/**
 * This class represents the controller client side to verify that a specified
 * move is valid in order to send it to the server
 * 
 * @author Riccardo
 *
 */
public abstract class ActionController {

	Scanner input = new Scanner(System.in);

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
				new ActionCreator(type,num);
			} catch (InvalidInputException e) {
				ClientOutputPrinter.printLine(e.printError());
			}
		}
		proceed = false;
	}

	public void showActions(String type) {
		if ("MAIN".equals(type)) {
			ClientOutputPrinter.printLine(
					"MAIN ACTIONS\n1)Buy Permit Tile\n2)Build an emporium using King's help\n3)Elect Councillor\n4)Build an emporium using Permit Tile");
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

	public static int verifyActionID(String id) throws InvalidInputException {
		int num = Integer.parseInt(id);
		if (num < 1 || num > 4)
			throw new InvalidInputException();
		return num;
	}

	public static void requestBoardStatus() {
		
	}
}
