package controller.Client;

import client.actions.ClientPacketController;
import client.view.cli.ClientOutputPrinter;

import java.rmi.RemoteException;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * 
 */
public class Client {

	private Scanner input = new Scanner(System.in);

	private ClientPacketController controller;

	private String nickName;

	/**
	 * Default constructor
	 */
	public Client() {
		controller = new ClientPacketController();
		welcome();
		ClientOutputPrinter.printLine("Please, first of all you need to connect to the game server...");
		try {
			controller.connect(nickName);
			initialConfiguration();
			play();
		} catch (RemoteException e) {
			ClientOutputPrinter.printLine(e.getMessage());
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
					controller.boardConfiguration();
					controller.mapConfiguration();
				}
				correct=true;
			} catch (InputMismatchException e) {
				ClientOutputPrinter.printLine("Please insert a correct input!");
			} catch(NoSuchElementException e) {
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
					quit = true;
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
				case 8:
					controller.chat();
					break;
				default:
					ClientOutputPrinter.printLine("Invalid choice... please retry!");
				}
			} catch (InputMismatchException e) {
				ClientOutputPrinter.printLine("Invalid choice... please retry!");
			}
		}
	}

	public void welcome() {
		ClientOutputPrinter.printLine("Welcome to a new session of 'Council Of Four' Game!");
		do {
			ClientOutputPrinter.printLine("Please, choose a nickname with at least 4 char and without space:");
			nickName = input.nextLine();
			checkCorrectNickName(nickName);
		} while (checkCorrectNickName(nickName));
	}

	public boolean checkCorrectNickName(String nickName) {
		boolean correct = false;
		if (nickName.contains(" ") || nickName.length() < 4)
			correct = true;
		return correct;
	}
}