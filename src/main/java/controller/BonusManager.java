package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.City;
import model.NobilityCell;
import model.NobilityTrack;
import model.PoliticCard;
import model.Tile;

/**
 * Created by Gabriele Bressan on 31/05/16.
 */
public class BonusManager {
	
	private NobilityTrack track;
	
	private List<Player> players;
	
	
	public BonusManager(List<Player> players, NobilityTrack track) {
		this.track=track;
		this.players=players;
	}
	
	/**
	 * 
	 * @param nobilityCell
	 * @return
	 */
	public void takeBonusFromNobilityTrack(NobilityCell nobilityCell, Player player) {
		useBonus(nobilityCell.getBonus(), player);

	}

	public void takeBonusFromTile(Tile tile, Player player) {
		useBonus(tile.getBonus(), player);
	}

	public void useBonus(ArrayList<String> bonus, Player player) {
		Random randomBonus = new Random();
		int supLimit, infLimit,numberOfBonus;
		for (String singleBonus : bonus) {

			switch (singleBonus) {
			case "ASSISTANT":
				infLimit = 1;
				supLimit = 5 - infLimit;
				numberOfBonus=randomBonus.nextInt(supLimit) + infLimit;
				player.addMoreAssistant(numberOfBonus);
				PubSub.notifyAllClients(this.players, "The player with nickname: "+player.getNickName()+" won "+numberOfBonus+" Assistants bonus!");
				break;
			case "VICTORYTRACK":
				infLimit = 1;
				supLimit = 15 - infLimit;
				numberOfBonus=randomBonus.nextInt(supLimit) + infLimit;
				player.setVoctoryPoints(numberOfBonus);
				PubSub.notifyAllClients(this.players, "The player with nickname: "+player.getNickName()+" won "+numberOfBonus+" bonus steps in Victory Track!");
				break;
			case "POLITIC":
				player.addCardOnHand(new PoliticCard());
				PubSub.notifyAllClients(this.players, "The player with nickname: "+player.getNickName()+" won a bonus and draw a Politic card!!");
				break;
			case "COINS":
				infLimit = 1;
				supLimit = 7 - infLimit;
				numberOfBonus=randomBonus.nextInt(supLimit) + infLimit;
				player.addCoins(numberOfBonus);
				PubSub.notifyAllClients(this.players, "The player with nickname: "+player.getNickName()+" won "+numberOfBonus+" Coins bonus!");
				break;
			case "NOBILITYTRACK":
				infLimit = 1;
				supLimit = 3 - infLimit;
				numberOfBonus=randomBonus.nextInt(supLimit) + infLimit;
				player.changePositionInNobilityTrack(numberOfBonus);
				int position = player.getPositionInNobilityTrack();
				NobilityCell cell = this.track.getNobilityTrackCell(position);
				takeBonusFromNobilityTrack(cell, player);
				PubSub.notifyAllClients(this.players, "The player with nickname: "+player.getNickName()+" won "+numberOfBonus+" bonus steps in Nobility Track!");
				break;
			case "DRAWPERMITTILE":
				player.addCardOnHand(new PoliticCard());
				PubSub.notifyAllClients(this.players, "The player with nickname: "+player.getNickName()+" won a bonus and draw a Permit tile!");
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
				PubSub.notifyAllClients(this.players, "The player with nickname: "+player.getNickName()+" won a bonus and now can re-use bonus on a permit tile");
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
				PubSub.notifyAllClients(this.players, "The player with nickname: "+player.getNickName()+" won a bonus and now can obtain bonus about two reward tokens");
				break;
			case "NEWMAINACTION":
				// da completare
				break;

			}

		}
	}
}
