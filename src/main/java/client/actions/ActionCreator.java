package client.actions;

import java.util.ArrayList;
import java.util.Scanner;

import client.view.cli.ClientOutputPrinter;
import exceptions.InvalidInputException;
import exceptions.InvalidSlotException;
import model.CouncillorColors;
import model.RegionName;

public class ActionCreator {

	public ActionCreator(String type, int num) {
		createAction(type, num);
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

	private void createAdditionalMainAction(String type) {
		// TODO Auto-generated method stub

	}

	private void createSendAssistant(String type) {
		// TODO Auto-generated method stub

	}

	private void createSwitchPermitTiles(String type) {
		// TODO Auto-generated method stub

	}

	private void createEngageAssistant(String type) {
		// TODO Auto-generated method stub

	}

	private void createKingBuildEmporium(String type) {
		// TODO Auto-generated method stub

	}

	private void createElectCouncillor(String type) {
		// TODO Auto-generated method stub

	}

	private void createSimpleBuildEmporium(String type) {
		// TODO Auto-generated method stub

	}

	private void createBuyPermitTile(String type) {
		Action action;
		String regionName = null;
		String color;
		ArrayList<String> colors=new ArrayList<String>();
		boolean proceed = false;
		int numberOfCards=0;
		int slot = 0;
		Scanner input = new Scanner(System.in);
		ClientOutputPrinter.printLine("BUY PERMIT TILE");
		ClientOutputPrinter.printLine(
				"Type the Region of the Council you wish to satisfy: choose from 'HILLS', 'COAST' or 'MOUNTAINS'");
		while (!proceed) {
			regionName = input.nextLine();
			try {
				verifyRegionName(regionName);
				proceed=true;
			} catch (InvalidInputException e) {
				ClientOutputPrinter.printLine(e.printError());
			}
		}
		proceed=false;
		ClientOutputPrinter.printLine(
				"Type the color of the Politic Cards you would like to use to satisfy the Council of"+regionName);
		ClientOutputPrinter.printLine("Type the colors one per line");
		while (numberOfCards<4) {
			try {
				color=input.nextLine();
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
				proceed=true;
			} catch (InvalidSlotException e) {
				ClientOutputPrinter.printLine(e.showError());
			}
		}
		action=new BuyPermitTileAction(type, regionName, colors, slot);
		//NEEDS TO BE COMPLETED
	}

	public void verifyRegionName(String region) throws InvalidInputException {
		ArrayList<String> regionNames = RegionName.getRegionNames();
		if (!regionNames.contains(region))
			throw new InvalidInputException();
	}

	public void verifyColor(String color) throws InvalidInputException {
		ArrayList<String> colors = CouncillorColors.getPoliticCardsColors();
		if(!colors.contains(color))
			throw new InvalidInputException();
	}
	
	public void verifySlot(int slot) throws InvalidSlotException {
		if(slot!=1&&slot!=2)
			throw new InvalidSlotException();
	}
	
	public void verifyElectCouncillor() {

	}

}
