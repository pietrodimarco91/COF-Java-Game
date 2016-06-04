package client.actions;

import java.util.ArrayList;
import java.util.Scanner;

import client.view.cli.ClientOutputPrinter;
import exceptions.InvalidInputException;
import exceptions.InvalidSlotException;
import model.CouncillorColors;
import model.RegionName;

public class ActionCreator {

	Scanner input;
	
	public ActionCreator(String type, int num) {
		createAction(type, num);
		input = new Scanner(System.in);
	}

	public void createAction(String type, int id) {
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
			}
		}
	}

	public void createAdditionalMainAction(String type) {
		Action action = new AdditionalMainAction(type);
		//NEEDS COMPLETION
	}

	public void createSendAssistant(String type) {
		String color,regionName;
		ClientOutputPrinter.printLine("SEND ASSISTANT TO ELECT A COUNCILLOR");
		regionName=selectRegionName();
		color=selectCouncillorColor();
		Action action = new SendAssistantAction(type, regionName, color);
		//NEEDS COMPLETION
	}

	public void createSwitchPermitTiles(String type) {
		String regionName = null;
		boolean proceed=false;
		ClientOutputPrinter.printLine("SWITCH PERMIT TILES");
		ClientOutputPrinter.printLine(
				"Type the Region name which you would like to switch the uncovered Permit Tiles from: choose from 'HILLS', 'COAST' or 'MOUNTAINS'");
		while (!proceed) {
			regionName = input.nextLine();
			try {
				verifyRegionName(regionName);
				proceed = true;
			} catch (InvalidInputException e) {
				ClientOutputPrinter.printLine(e.printError());
			}
		}
		Action action = new SwitchPermitTilesAction(type, regionName);
		//NEEDS COMPLETION;
	}

	public void createEngageAssistant(String type) {
		Action action = new EngageAssistantAction(type);
		//NEEDS COMPLETION
	}

	public void createKingBuildEmporium(String type) {
		Action action;
		int numberOfCards=0;
		String color,cityName;
		ArrayList<String> colors = new ArrayList<>();
		ClientOutputPrinter.printLine("BUILD AN EMPOIUM WITH KING'S HELP");
		ClientOutputPrinter.printLine(
				"Type the color of the Politic Cards you would like to use to satisfy the King's Council ");
		ClientOutputPrinter.printLine("Type the colors one per line");
		while (numberOfCards < 4) {
			color = input.nextLine();
			try {
				verifyColor(color);
				colors.add(color);
				numberOfCards++;
			} catch (InvalidInputException e) {
				ClientOutputPrinter.printLine(e.printError());
			}
		}
		ClientOutputPrinter.printLine("Type the INITIAL LETTER of the city where you would like to build your emporium:");
		cityName=input.nextLine();
		action = new KingBuildEmporiumAction(type, cityName, colors);
		//NEEDS COMPLETION
	}

	public String selectCouncillorColor() {
		boolean proceed=false;
		String color=null;
		ClientOutputPrinter.printLine("Type the color of the Councillor to elect:");
		while (!proceed) {
			color = input.nextLine();
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
		String regionName=null;
		ClientOutputPrinter.printLine(
				"Type the Region name of the Council where you wish to elect the councillor: choose from 'HILLS', 'COAST' or 'MOUNTAINS'");
		while (!proceed) {
			regionName = input.nextLine();
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
		Action action;
		boolean proceed = false;
		String regionName=null, color=null;
		ClientOutputPrinter.printLine("ELECT A COUNCILLOR");
		regionName=selectRegionName();
		color=selectCouncillorColor();
		action = new ElectCouncillorAction(type, regionName, color);
		//NEEDS COMPLETION
	}

	public void createSimpleBuildEmporium(String type) {
		Action action;
		int id;
		String cityName=null;
		ClientOutputPrinter.printLine("BUILD AN EMPORIUM USING A PERMIT TILE");
		ClientOutputPrinter.printLine("Type the ID of the Permit Tile you would like to choose to build your emporium:");
		id=Integer.parseInt(input.nextLine());
		ClientOutputPrinter.printLine("Type the INITIAL LETTER of the city where you would like to build your emporium:");
		cityName=input.nextLine();
		action = new SimpleBuildEmporiumAction(type, id, cityName);
		//NEEDS IMPLEMENTATION
	}

	public void createBuyPermitTile(String type) {
		Action action;
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
				verifyColor(color);
				colors.add(color);
				numberOfCards++;
			} catch (InvalidInputException e) {
				ClientOutputPrinter.printLine(e.printError());
			}
		}
		while (!proceed) {
			slot = Integer.parseInt(input.nextLine());
			try {
				verifySlot(slot);
				proceed = true;
			} catch (InvalidSlotException e) {
				ClientOutputPrinter.printLine(e.showError());
			}
		}
		action = new BuyPermitTileAction(type, regionName, colors, slot);
		// NEEDS TO BE COMPLETED
	}

	public void verifyRegionName(String region) throws InvalidInputException {
		ArrayList<String> regionNames = RegionName.getRegionNames();
		if (!regionNames.contains(region))
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