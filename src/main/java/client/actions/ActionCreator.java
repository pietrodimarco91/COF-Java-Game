package client.actions;

import client.view.cli.ClientOutputPrinter;
import controller.Packet;
import controller.ServerSideConnectorInt;
import exceptions.InvalidInputException;
import exceptions.InvalidSlotException;
import model.CouncillorColors;
import model.RegionName;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class ActionCreator {

	private Scanner input;
	
	private ServerSideConnectorInt actionSenderInt;

	public ActionCreator() {
		input = new Scanner(System.in);
	}

	public void createAction(String type, int id, ServerSideConnectorInt actionSenderInt) {
		this.actionSenderInt=actionSenderInt;
		if ("MAIN".equals(type)) {
			switch (id) {
			case 1:
				createBuyPermitTile(type);
				break;
			case 2:
				createSimpleBuildEmporium(type);
				break;
			case 3:
				createElectCouncillor(type);
				break;
			case 4:
				createKingBuildEmporium(type);
				break;
			default:
			}
		} else {
			switch (id) {
			case 1:
				createEngageAssistant(type);
				break;
			case 2:
				createSwitchPermitTiles(type);
				break;
			case 3:
				createSendAssistant(type);
				break;
			case 4:
				createAdditionalMainAction(type);
				break;
			default:
			}
		}
	}

	public void createAdditionalMainAction(String type) {
		this.sendAction(new AdditionalMainAction(type));
	}

	public void createSendAssistant(String type) {
		String color, regionName;
		ClientOutputPrinter.printLine("SEND ASSISTANT TO ELECT A COUNCILLOR");
		regionName = selectRegionName();
		color = selectCouncillorColor();
		this.sendAction(new SendAssistantAction(type, regionName, color));
	}

	public void createSwitchPermitTiles(String type) {
		String regionName = null;
		boolean proceed = false;
		ClientOutputPrinter.printLine("SWITCH PERMIT TILES");
		ClientOutputPrinter.printLine(
				"Type the Region name which you would like to switch the uncovered Permit Tiles from: choose from 'HILLS', 'COAST' or 'MOUNTAINS'");
		while (!proceed) {
			regionName = input.nextLine();
			regionName = regionName.toUpperCase();
			try {
				verifyRegionName(regionName);
				proceed = true;
			} catch (InvalidInputException e) {
				ClientOutputPrinter.printLine(e.printError());
			}
		}
		this.sendAction(new SwitchPermitTilesAction(type, regionName));
	}

	public void createEngageAssistant(String type) {
		sendAction(new EngageAssistantAction(type));
	}

	public void createKingBuildEmporium(String type) {
		int numberOfCards = 0;
		String color, cityName;
		ArrayList<String> colors = new ArrayList<>();
		ClientOutputPrinter.printLine("BUILD AN EMPORIUM WITH KING'S HELP");
		ClientOutputPrinter
				.printLine("Type the color of the Politic Cards you would like to use to satisfy the King's Council ");
		ClientOutputPrinter.printLine("Type the colors one per line, type 'stop' to stop");
		color = input.nextLine();
		color = color.toUpperCase();
		while (numberOfCards == 0 || !color.equals("stop")) {
			try {
				verifyColor(color);
				colors.add(color);
				numberOfCards++;
				if (numberOfCards == 4)
					break;
			} catch (InvalidInputException e) {
				ClientOutputPrinter.printLine(e.printError());
			}
		}
		ClientOutputPrinter
				.printLine("Type the INITIAL LETTER of the city where you would like to build your emporium:");
		cityName = input.nextLine();
		cityName = cityName.toUpperCase();
		this.sendAction(new KingBuildEmporiumAction(type, cityName, colors));
	}

	public String selectCouncillorColor() {
		boolean proceed = false;
		String color = null;
		ClientOutputPrinter.printLine("Type the color of the Councillor to elect:");
		while (!proceed) {
			color = input.nextLine();
			color = color.toUpperCase();
			try {
				verifyColor(color);
				proceed = true;
			} catch (InvalidInputException e) {
				ClientOutputPrinter.printLine(e.printError());
			}
		}
		return color;
	}

	public String selectRegionName() {
		boolean proceed = false;
		String regionName = null;
		ClientOutputPrinter.printLine(
				"Type the Region name of the Council where you wish to elect the councillor: choose from 'HILLS', 'COAST', 'MOUNTAINS' or 'KING'");
		while (!proceed) {
			regionName = input.nextLine();
			regionName = regionName.toUpperCase();
			try {
				verifyRegionName(regionName);
				proceed = true;
			} catch (InvalidInputException e) {
				ClientOutputPrinter.printLine(e.printError());
			}
		}
		return regionName;
	}

	public void createElectCouncillor(String type) {
		String regionName = null, color = null;
		ClientOutputPrinter.printLine("ELECT A COUNCILLOR");
		regionName = selectRegionName();
		color = selectCouncillorColor();
		this.sendAction(new ElectCouncillorAction(type, regionName, color));
	}

	public void createSimpleBuildEmporium(String type) {
		int id;
		String cityName = null;
		boolean proceed = false;

		ClientOutputPrinter.printLine("BUILD AN EMPORIUM USING A PERMIT TILE");
		while (!proceed) {
			try {
				ClientOutputPrinter
						.printLine("Type the ID of the Permit Tile you would like to choose to build your emporium:");
				id = Integer.parseInt(input.nextLine());
				ClientOutputPrinter
						.printLine("Type the INITIAL LETTER of the city where you would like to build your emporium:");
				cityName = input.nextLine();
				this.sendAction(new SimpleBuildEmporiumAction(type, id, cityName));
				proceed = true;
			} catch (NumberFormatException e) {
				ClientOutputPrinter.printLine("Invalid input! Please insert an integer");
			}
		}
	}

	public void createBuyPermitTile(String type) {
		String regionName = null;
		String color;
		ArrayList<String> colors = new ArrayList<String>();
		boolean proceed = false;
		int numberOfCards = 0;
		int slot = 0;
		ClientOutputPrinter.printLine("BUY PERMIT TILE");
		ClientOutputPrinter.printLine(
				"Type the Region of the Council you wish to satisfy: choose from 'HILLS', 'COAST' or 'MOUNTAINS'");
		while (!proceed) {
			regionName = input.nextLine();
			try {
				regionName = regionName.toUpperCase();
				verifyRegionName(regionName);
				proceed = true;
			} catch (InvalidInputException e) {
				ClientOutputPrinter.printLine(e.printError());
			}
		}
		proceed = false;
		ClientOutputPrinter.printLine(
				"Type the color of the Politic Cards you would like to use to satisfy the Council of" + regionName);
		ClientOutputPrinter.printLine("Type the colors one per line");
		while (numberOfCards < 4) {
			color = input.nextLine();
			try {
				color = color.toUpperCase();
				verifyColor(color);
				colors.add(color);
				numberOfCards++;
			} catch (InvalidInputException e) {
				ClientOutputPrinter.printLine(e.printError());
			}
		}
		while (!proceed) {
			try {
				slot = Integer.parseInt(input.nextLine());
				verifySlot(slot);
				proceed = true;
			} catch (InvalidSlotException e) {
				ClientOutputPrinter.printLine(e.showError());
			} catch (NumberFormatException e) {
				ClientOutputPrinter.printLine(e.getMessage());
			}
		}
		this.sendAction(new BuyPermitTileAction(type, regionName, colors, slot));
	}

	private void sendAction(Action action) {
		try {
			actionSenderInt.sendToServer(new Packet(action));
		} catch (RemoteException e) {
			ClientOutputPrinter.printLine(e.getMessage());
		}
	}

	public void verifyRegionName(String region) throws InvalidInputException {
		ArrayList<String> regionNames = RegionName.getRegionNames();
		if (!regionNames.contains(region) && !region.equals("KING"))
			throw new InvalidInputException();
	}

	public void verifyColor(String color) throws InvalidInputException {
		ArrayList<String> colors = CouncillorColors.getPoliticCardsColors();
		if (!colors.contains(color))
			throw new InvalidInputException();
	}

	public void verifySlot(int slot) throws InvalidSlotException {
		if (slot != 1 && slot != 2)
			throw new InvalidSlotException();
	}

}
