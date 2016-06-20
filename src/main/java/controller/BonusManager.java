package controller;

import java.util.ArrayList;
import java.util.Random;

import model.City;
import model.NobilityCell;
import model.PermitTile;
import model.PoliticCard;
import model.Tile;

public abstract class BonusManager {
	/**
	 * Created by Gabriele Bressan on 31/05/16.
	 */

	/**
	 * Default constructor
	 */
	public BonusManager() {
	}

	/**
	 * 
	 * @param tile
	 * @return
	 */
	public static String takeBonusFromTile(Tile tile) {
		String bonus = "";
		return bonus;
	}

	/**
	 * 
	 * @param nobilityCell
	 * @return
	 */
	public static void takeBonusFromNobilityTrack(NobilityCell nobilityCell, Player player) {
		useBonus(nobilityCell.getBonus(), player);

	}

	public static void takeBonusFromTile(Tile tile, Player player) {
		useBonus(tile.getBonus(), player);
	}

	public static void useBonus(ArrayList<String> bonus, Player player) {
		Random randomBonus = new Random();
		int supLimit, infLimit;
		for (String singleBonus : bonus) {

			switch (singleBonus) {
			case "ASSISTANT":
				infLimit = 1;
				supLimit = 5 - infLimit;
				player.addMoreAssistant(randomBonus.nextInt(supLimit) + infLimit);
				break;
			case "VICTORYTRACK":
				infLimit = 1;
				supLimit = 15 - infLimit;
				player.setVoctoryPoints(randomBonus.nextInt(supLimit) + infLimit);
				break;
			case "POLITIC":
				player.addCardOnHand(new PoliticCard());
				break;
			case "COINS":
				infLimit = 1;
				supLimit = 7 - infLimit;
				player.addCoins(randomBonus.nextInt(supLimit) + infLimit);
				break;
			case "NOBILITYTRACK":
				infLimit = 1;
				supLimit = 3 - infLimit;
				break;
			case "DRAWPERMITTILE":
				player.addCardOnHand(new PoliticCard());
				break;
			case "BONUSPERMITTILE":
				int randomPermitTile;
				infLimit = 1;
				supLimit = 2 - infLimit;
				int deckPermitTileChoice = randomBonus.nextInt(supLimit) + infLimit;
				if (deckPermitTileChoice == 1) {
					infLimit=0;
					supLimit=player.getNumberOfUsedPermitTile()-1;
					randomPermitTile=randomBonus.nextInt(supLimit) + infLimit;
					Tile tempTile = player.getUsedPermitTile(randomPermitTile);
					useBonus(tempTile.bonus, player);
				} else {
					infLimit=0;
					supLimit=player.getNumberOfPermitTile()-1;
					randomPermitTile=randomBonus.nextInt(supLimit) + infLimit;
					Tile tempTile = player.getUnusedPermitTileFromId(randomPermitTile);
					useBonus(tempTile.bonus, player);
				}
				break;
			case "TWOEMPORIUMCITY":
				if (player.getNumberoOfControlledCities() >= 2) {
					supLimit = randomBonus.nextInt(player.getNumberoOfControlledCities() - 1);
					City tempCity = player.getSingleControlledCity(supLimit);
					useBonus(tempCity.winBonus().bonus, player);
					int secondSupLimit = randomBonus.nextInt(player.getNumberoOfControlledCities() - 1);
					while (supLimit == secondSupLimit)
						secondSupLimit = randomBonus.nextInt(player.getNumberoOfControlledCities() - 1);
					tempCity = player.getSingleControlledCity(supLimit);
					useBonus(tempCity.winBonus().bonus, player);
				} else {
					supLimit = randomBonus.nextInt(player.getNumberoOfControlledCities() - 1);
					City tempCity = player.getSingleControlledCity(supLimit);
					useBonus(tempCity.winBonus().bonus, player);
				}

				break;
			case "NEWMAINACTION":
				// da completare
				break;

			}

		}
	}
}
