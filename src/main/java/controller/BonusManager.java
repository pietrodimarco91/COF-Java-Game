package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import exceptions.InvalidSlotException;
import exceptions.TileNotFoundException;
import model.Board;
import model.City;
import model.NobilityCell;
import model.NobilityTrack;
import model.PermitTile;
import model.PoliticCard;
import model.Region;
import model.Tile;
import server.view.cli.ServerOutputPrinter;

/**
 * Created by Gabriele Bressan on 31/05/16.
 */
public class BonusManager {

	private Board board;

	private List<Player> players;
	
	private NobilityTrack nobilityTrack;

	public BonusManager(List<Player> players, Board board) {
		this.board=board;
		this.players = players;
		this.nobilityTrack=board.getNobilityTrack();
	}

	/**
	 * 
	 * @param nobilityCell
	 * @return
	 */
	public void takeBonusFromNobilityTrack(NobilityCell nobilityCell, Player player) {
		useBonus(nobilityCell.winBonus(), player);

	}

	/**
	 * 
	 * @param tile
	 * @param player
	 */
	public void takeBonusFromTile(Tile tile, Player player) {
		useBonus(tile.getBonus(), player);
		winPoints(tile.getPoints(), player);
	}

	/**
	 * 
	 * @param points
	 * @param player
	 */
	private void winPoints(int points, Player player) {
		if (points > 0) {
			player.addVictoryPoints(points);
			PubSub.notifyAllClients(this.players, "Player with nickname '" + player.getNickName() + "' won " + points
					+ " points for the Victory Track!");
		}
	}

	/**
	 * 
	 * @param bonus
	 * @param player
	 */
	public void useBonus(ArrayList<String> bonus, Player player) {
		for (String singleBonus : bonus) {

			switch (singleBonus) {
			case "ASSISTANT":
				assistantBonus(bonus, player);
				break;
			case "VICTORYTRACK":
				victoryTrackBonus(bonus, player);
				break;
			case "POLITIC":
				politicBonus(bonus, player);
				break;
			case "COINS":
				coinsBonus(bonus, player);
				break;
			case "NOBILITYTRACK":
				nobilityTrackBonus(bonus, player);
				break;
			case "DRAWPERMITTILE": // WRONG IMPLEMENTATION!!!
				drawPermitTile(player);
				break;
			case "BONUSPERMITTILE":
				bonusPermitTile(bonus, player);
				break;
			case "TWOEMPORIUMCITY": 
				twoEmporiumCityBonus(bonus, player);
				break;
			case "NEWMAINACTION":
				newMainActionBonus(bonus, player);
				break;
			default:
			}
		}
	}

	private void assistantBonus(ArrayList<String> bonus, Player player) {
		int numberOfBonus;
		numberOfBonus = randomNumber(1, 5);
		player.addMoreAssistant(numberOfBonus);
		PubSub.notifyAllClients(this.players,
				"Player with nickname '" + player.getNickName() + "' won " + numberOfBonus + " Assistants!");
	}
	
	private void drawPermitTile(Player player){
		Region region[]=this.board.getRegions();
		Tile bonusTile;
		int slotChoice,regionChoice;
		regionChoice = randomNumber(1, 3);
		slotChoice = randomNumber(1, 2);
		try {
			bonusTile=region[regionChoice-1].getDeck().drawPermitTile(slotChoice);
			player.addUnusedPermitTiles(bonusTile);
			PubSub.notifyAllClients(this.players,"Player with nickname '" + player.getNickName() +"' won a bonus and draw a Permit tile!");
		} catch (InvalidSlotException e) {
			ServerOutputPrinter.printLine(e.getMessage());;
		}	
	}

	private void victoryTrackBonus(ArrayList<String> bonus, Player player) {
		int numberOfBonus;
		numberOfBonus = randomNumber(1, 15);
		player.addVictoryPoints(numberOfBonus);
		PubSub.notifyAllClients(this.players, "Player with nickname '" + player.getNickName() + "' won " + numberOfBonus
				+ " points for the Victory Track!");
	}

	private void politicBonus(ArrayList<String> bonus, Player player) {
		player.addCardOnHand(new PoliticCard());
		PubSub.notifyAllClients(this.players,
				"Player with nickname '" + player.getNickName() + "' won a bonus and draw a new Politic Card!");
	}

	private void coinsBonus(ArrayList<String> bonus, Player player) {
		int numberOfBonus;
		numberOfBonus = randomNumber(1,7);
		player.addCoins(numberOfBonus);
		PubSub.notifyAllClients(this.players,
				"Player with nickname '" + player.getNickName() + "' won " + numberOfBonus + " Coins!");
	}

	private void nobilityTrackBonus(ArrayList<String> bonus, Player player) {
		int numberOfBonus;
		numberOfBonus = randomNumber(1, 3);
		player.changePositionInNobilityTrack(numberOfBonus);
		int position = player.getPositionInNobilityTrack();
		NobilityCell cell = this.nobilityTrack.getNobilityTrackCell(position);
		PubSub.notifyAllClients(this.players, "Player with nickname '" + player.getNickName() + "' won " + numberOfBonus
				+ " bonus steps in Nobility Track!");
		takeBonusFromNobilityTrack(cell, player);
	}

	private void bonusPermitTile(ArrayList<String> bonus, Player player) {
		Random randomBonus = new Random();
		int supLimit, infLimit;
		infLimit = 1;
		supLimit = 2 - infLimit;
		Tile tempTile;
		String deck = "";
		int deckPermitTileChoice = randomBonus.nextInt(supLimit) + infLimit;
		try {
			if (deckPermitTileChoice == 1) {
				deck = "USED PERMIT TILES DECK";
				tempTile = player.getRandomUsedPermitTile();
			} else {
				deck = "UNUSED PERMIT TILES DECK";
				tempTile = player.getRandomUnusedPermitTile();
			}
			PubSub.notifyAllClients(this.players, "Player with nickname '" + player.getNickName()
					+ "' won a bonus and now can re-use bonus on a permit tile");
			useBonus(tempTile.getBonus(), player);
		} catch (TileNotFoundException e) {
			PubSub.notifyAllClients(this.players, "Player with nickname '" + player.getNickName()
					+ "' won the BONUSPERMITTILE but he hasn't got Permit Tiles in his " + deck);
		}
	}

	private void twoEmporiumCityBonus(ArrayList<String> bonus, Player player) {
		Random randomBonus = new Random();
		City tempCity=null;
		int supLimit;
		if (player.getNumberOfControlledCities() == 0)
			return;
		if (player.getNumberOfControlledCities() == 1){
			int cityControlled=player.getNumberOfControlledCities();
			tempCity = player.getSingleControlledCity(cityControlled-1);
		}
		if (player.getNumberOfControlledCities() >= 2) {
			supLimit = randomBonus.nextInt(player.getNumberOfControlledCities() - 1);
			tempCity = player.getSingleControlledCity(supLimit);
			int secondSupLimit = randomBonus.nextInt(player.getNumberOfControlledCities() - 1);
			while (supLimit == secondSupLimit)
				secondSupLimit = randomBonus.nextInt(player.getNumberOfControlledCities() - 1);
			tempCity = player.getSingleControlledCity(supLimit);
		}
		PubSub.notifyAllClients(this.players, "Player with nickname '" + player.getNickName()
				+ "' won a bonus and now can obtain bonus from two Reward Tokens");
		useBonus(tempCity.winBonus().getBonus(), player);
	}

	private void newMainActionBonus(ArrayList<String> bonus, Player player) {
		player.mainActionDone(false);
		PubSub.notifyAllClients(this.players,
				"The player with nickname: " + player.getNickName() + " won the 'NEW MAIN ACTION' bonus!");
	}
	
	private int randomNumber(int infLimit,int supLimit){
		int randomNumber;
		Random randomBonus = new Random();
		randomNumber= randomBonus.nextInt(supLimit) + infLimit;
		return randomNumber;
		
	}
}
