package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import client.actions.AdditionalMainAction;
import client.actions.BuyPermitTileAction;
import client.actions.ElectCouncillorAction;
import client.actions.EngageAssistantAction;
import client.actions.KingBuildEmporiumAction;
import client.actions.SendAssistantAction;
import client.actions.SimpleBuildEmporiumAction;
import client.actions.SwitchPermitTilesAction;
import exceptions.AlreadyOwnedEmporiumException;
import exceptions.CityNotFoundFromPermitTileException;
import exceptions.CouncillorNotFoundException;
import exceptions.InvalidSlotException;
import exceptions.TileNotFoundException;
import exceptions.UnsufficientAssistantNumberException;
import exceptions.UnsufficientCoinsException;
import exceptions.UnsufficientCouncillorsSatisfiedException;
import model.Board;
import model.City;
import model.Council;
import model.KingCouncil;
import model.PermitTile;
import model.PermitTileDeck;
import model.PoliticCard;
import model.Region;
import model.Tile;

/**
 * This class has all the method used to perform the actions of the game.
 * 
 * @author Riccardo
 *
 */
public class MatchActionsHandler {

	private Board board;

	private MatchHandler match;

	private List<Player> players;

	public MatchActionsHandler(MatchHandler match, Board board, List<Player> players) {
		this.board = board;
		this.match = match;
		this.players = players;
	}

	public void buyPermitTile(BuyPermitTileAction buyPermitTileAction, int playerId) {
		if (players.get(playerId).hasPerformedMainAction()) {
			match.sendErrorToClient("You've already performed a Main Action for this turn!", playerId);
			return;
		}
		String regionName;
		ArrayList<String> chosenPoliticCards;
		ArrayList<String> cardToRemove;
		int slot;
		int playerPayment = 0;
		Region region;
		PermitTileDeck regionDeck;
		Council council;

		regionName = buyPermitTileAction.getRegion();
		chosenPoliticCards = buyPermitTileAction.getPoliticCardColors();
		slot = buyPermitTileAction.getSlot();
		region = getRegion(regionName);
		council = region.getCouncil();

		cardToRemove = council.numberOfCouncillorsSatisfied(chosenPoliticCards);
		Player player = this.players.get(playerId);
		try {
			if (cardToRemove.size() == 0)
				throw new UnsufficientCouncillorsSatisfiedException();
			playerPayment = CoinsManager.paymentForPermitTile(cardToRemove.size());
			player.removeCardsFromHand(cardToRemove);
			regionDeck = region.getDeck();
			player.performPayment(playerPayment);
			Tile permitTile = regionDeck.drawPermitTile(slot);
			player.addUnusedPermitTiles(permitTile);
			PubSub.notifyAllClients(players, "Player '" + player.getNickName()
					+ "' satisfied the Council of the region '" + regionName + "' and bought a Permit Tile", board);
			match.updateClient(playerId);
			match.getBonusManager().takeBonusFromTile(permitTile, player);
			player.mainActionDone(true);
			if (player.hasPerformedQuickAction()) {
				match.notifyEndOfTurn(player);
				player.resetTurn();
			}
		} catch (UnsufficientCouncillorsSatisfiedException e) {
			match.sendErrorToClient(e.showError(), playerId);
		} catch (UnsufficientCoinsException e) {
			for (int i = 0; i < chosenPoliticCards.size(); i++)
				player.addCardOnHand(new PoliticCard(chosenPoliticCards.get(i)));
			match.sendErrorToClient(e.showError(), playerId);
		} catch (InvalidSlotException e) {
			player.addCoins(playerPayment);
			for (int i = 0; i < chosenPoliticCards.size(); i++)
				player.addCardOnHand(new PoliticCard(chosenPoliticCards.get(i)));
			match.sendErrorToClient(e.showError(), playerId);
		} catch (NoSuchElementException e) {
			player.addCoins(playerPayment);
			for (int i = 0; i < chosenPoliticCards.size(); i++)
				player.addCardOnHand(new PoliticCard(chosenPoliticCards.get(i)));
			match.sendErrorToClient("The queue of PermitTiles for this region is empty!", playerId);
		}
	}

	/**
	 * NEEDS IMPLEMENTATION
	 * 
	 * @return
	 * @throws UnsufficientCoinsException
	 */
	public void buildEmporiumWithKingsHelp(KingBuildEmporiumAction kingBuildEmporiumAction, int playerId) {
		if (players.get(playerId).hasPerformedMainAction()) {
			match.sendErrorToClient("You've already performed a Main Action for this turn!", playerId);
			return;
		}
		String cityName;
		ArrayList<String> chosenPoliticCards;
		ArrayList<String> cardToRemove;
		Council council;
		int playerPayment = 0;
		int coinsToPay = 0;
		Player player = this.players.get(playerId);
		cityName = kingBuildEmporiumAction.getCityName();
		chosenPoliticCards = kingBuildEmporiumAction.getPoliticCardColors();
		council = this.board.getKingCouncil();
		cardToRemove = council.numberOfCouncillorsSatisfied(chosenPoliticCards);
		try {
			if (cardToRemove.size() == 0)
				throw new UnsufficientCouncillorsSatisfiedException();

			City cityTo = board.getCityFromName(cityName);
			City cityFrom = board.findKingCity();
			coinsToPay = board.countDistance(cityFrom, cityTo) * 2;
			playerPayment = CoinsManager.paymentForPermitTile(cardToRemove.size());
			player.performPayment(playerPayment + coinsToPay);
			player.removeCardsFromHand(cardToRemove);
			if (!cityTo.buildEmporium(player))
				throw new AlreadyOwnedEmporiumException();
			if (coinsToPay > 0) {
				board.moveKing(cityTo);
			}
			PubSub.notifyAllClients(players, "Player " + player.getNickName() + " has built an Emporium in "
					+ cityTo.getName() + " with king's help", board);
			match.updateClient(playerId);
			match.winBuildingBonuses(cityTo, player);
			player.mainActionDone(true);
			if (hasBuiltLastEmporium(player)) {
				PubSub.notifyAllClients(this.players,
						"Player " + player.getNickName() + " has built his last Emporium!!\n This is your last turn!",
						board);
				match.setGameStatus(GameStatusConstants.FINISH);
				player.addVictoryPoints(3);
				PubSub.notifyAllClients(this.players,
						"Player " + player.getNickName() + " has won 3 bonus Victory Points!", board);
			}
			if (player.hasPerformedQuickAction()) {
				match.notifyEndOfTurn(player);
				player.resetTurn();
			}
		} catch (UnsufficientCouncillorsSatisfiedException e) {
			match.sendErrorToClient(e.showError(), playerId);
		} catch (UnsufficientCoinsException e) {
			match.sendErrorToClient(e.showError(), playerId);
		} catch (AlreadyOwnedEmporiumException e) {
			player.addCoins(playerPayment + coinsToPay);
			for (int i = 0; i < chosenPoliticCards.size(); i++)
				player.addCardOnHand(new PoliticCard(chosenPoliticCards.get(i)));
			match.sendErrorToClient(e.showError(), playerId);
		}
	}

	/**
	 * @return
	 */
	public void performAdditionalMainAction(AdditionalMainAction action, int playerId) {
		if (players.get(playerId).hasPerformedQuickAction()) {
			match.sendErrorToClient("You've already performed a Quick Action for this turn!", playerId);
			return;
		}
		Player player = players.get(playerId);
		try {
			if (player.getNumberOfAssistants() < 3)
				throw new UnsufficientCoinsException();
			player.removeMoreAssistants(3);
			match.updateClient(playerId);
			player.mainActionDone(false);
			player.quickActionDone();
		} catch (UnsufficientCoinsException e) {
			match.sendErrorToClient(e.showError(), playerId);
		}
	}

	/**
	 * NEEDS REVISION: this method must not return a boolean: the try/catch must
	 * be handled inside a while loop untile the move is correctly performed.
	 * 
	 * @return
	 */
	public void electCouncillor(ElectCouncillorAction electCouncillorAction, int playerId) {
		if (players.get(playerId).hasPerformedMainAction()) {
			match.sendErrorToClient("You've already performed a Main Action for this turn!", playerId);
			return;
		}
		String regionName;
		String councillorColor;
		regionName = electCouncillorAction.getRegion();
		councillorColor = electCouncillorAction.getColor();
		Player player = this.players.get(playerId);
		try {
			if (regionName.equals("KING")) {
				KingCouncil council = (KingCouncil) this.board.getKingCouncil();
				council.electCouncillor(councillorColor);
			} else {
				Region region = this.getRegion(regionName);
				region.electCouncillor(councillorColor);
			}
			player.addCoins(4);
			PubSub.notifyAllClients(players, "Player '" + player.getNickName() + "' elected a " + councillorColor
					+ " Councillor in " + regionName + "Council", board);
			match.updateClient(playerId);
			player.mainActionDone(true);
			if (player.hasPerformedQuickAction()) {
				match.notifyEndOfTurn(player);
				player.resetTurn();
			}
		} catch (CouncillorNotFoundException e) {
			match.sendErrorToClient(e.showError(), playerId);
		}
	}

	/**
	 * NEEDS REVISION: this method must not return a boolean: the try/catch must
	 * be handled inside a while loop until the move is correctly performed.
	 * 
	 * @return
	 */
	public void engageAssistant(EngageAssistantAction engageAssistantAction, int playerId) {
		if (players.get(playerId).hasPerformedQuickAction()) {
			match.sendErrorToClient("You've already performed a Quick Action for this turn!", playerId);
			return;
		}
		Player player = this.players.get(playerId);
		int coins = player.getCoins();
		try {
			if (coins < 3)
				throw new UnsufficientCoinsException();
			player.performPayment(3);
			player.addAssistant();
			PubSub.notifyAllClients(players, "Player " + player.getNickName() + " bought an Assistant!", board);
			match.updateClient(playerId);
			player.quickActionDone();
			if (player.hasPerformedMainAction()) {
				match.notifyEndOfTurn(player);
				player.resetTurn();
			}
		} catch (UnsufficientCoinsException e) {
			match.sendErrorToClient(e.showError(), playerId);
		}
	}

	/**
	 * NEEDS REVISION: the parameters communication must be implemented inside
	 * the method.
	 * 
	 * @return
	 */
	public void switchPermitTile(SwitchPermitTilesAction switchPermitTilesAction, int playerId) {
		if (players.get(playerId).hasPerformedQuickAction()) {
			match.sendErrorToClient("You've already performed a Quick Action for this turn!", playerId);
			return;
		}
		String regionName;
		Player player = this.players.get(playerId);
		regionName = switchPermitTilesAction.getRegionName();

		Region region = this.getRegion(regionName);
		try {
			if (player.getNumberOfAssistants() < 1)
				throw new UnsufficientAssistantNumberException();
			region.getDeck().switchPermitTiles();
			player.removeAssistant();
			PubSub.notifyAllClients(players,
					"Player " + player.getNickName() + " swhitched Permit Tile in " + regionName + "!", board);
			match.updateClient(playerId);
			player.quickActionDone();
			if (player.hasPerformedMainAction()) {
				match.notifyEndOfTurn(player);
				player.resetTurn();
			}
		} catch (UnsufficientAssistantNumberException e) {
			match.sendErrorToClient(e.showError(), playerId);
		}
	}

	/**
	 * MUST BE FIXED IMMEDIATELY! COMPILATION ERRORS
	 * 
	 * @return
	 */
	public void buildEmporiumWithPermitTile(SimpleBuildEmporiumAction simpleBuildEmporium, int playerId) {
		if (players.get(playerId).hasPerformedMainAction()) {
			match.sendErrorToClient("You've already performed a Main Action for this turn!", playerId);
			return;
		}
		int permitTileId;
		String cityName;
		PermitTile tempPermitTile;
		Player player = this.players.get(playerId);

		permitTileId = simpleBuildEmporium.getPermitTileID();
		cityName = simpleBuildEmporium.getCityName();
		try {
			tempPermitTile = (PermitTile) player.getUnusedPermitTileFromId(permitTileId);
			if (!buildEmporium(tempPermitTile, player, cityName))
				throw new CityNotFoundFromPermitTileException();
			PubSub.notifyAllClients(players,
					"Player " + player.getNickName() + " has built an Emporium in " + cityName + "!", board);
			player.fromUnusedToUsedPermitTile(tempPermitTile);
			match.updateClient(playerId);
			match.winBuildingBonuses(board.getCityFromName(cityName), player);
			player.mainActionDone(true);
			if (hasBuiltLastEmporium(player)) {
				PubSub.notifyAllClients(this.players,
						"Player " + player.getNickName() + " has built his last Emporium!!\n This is your last turn!",
						board);
				match.setGameStatus(GameStatusConstants.FINISH);
				player.addVictoryPoints(3);
			}
			if (player.hasPerformedQuickAction()) {
				match.notifyEndOfTurn(player);
				player.resetTurn();
			}
		} catch (CityNotFoundFromPermitTileException e) {
			match.sendErrorToClient(e.showError(), playerId);
		} catch (AlreadyOwnedEmporiumException e) {
			match.sendErrorToClient(e.showError(), playerId);
		} catch (TileNotFoundException e) {
			match.sendErrorToClient(e.showError(), playerId);
		}
	}

	/**
	 * 
	 * @return
	 */
	public void sendAssistantToElectCouncillor(SendAssistantAction sendAssistantAction, int playerId) {
		if (players.get(playerId).hasPerformedQuickAction()) {
			match.sendErrorToClient("You've already performed a Quick Action for this turn!", playerId);
			return;
		}
		String councillorColor = sendAssistantAction.getColor();
		String regionName = sendAssistantAction.getRegion();
		Player player = this.players.get(playerId);
		try {
			if (player.getNumberOfAssistants() == 0)
				throw new UnsufficientAssistantNumberException();
			Region region = this.getRegion(regionName);
			region.electCouncillor(councillorColor);
			player.removeAssistant();
			PubSub.notifyAllClients(players, "Player " + player.getNickName() + " sent an Assistant to elect a "
					+ councillorColor + "Councillor in " + regionName + "!", board);
			match.updateClient(playerId);
			player.quickActionDone();
			if (player.hasPerformedMainAction()) {
				match.notifyEndOfTurn(player);
				player.resetTurn();
			}
		} catch (UnsufficientAssistantNumberException e) {
			match.sendErrorToClient(e.showError(), playerId);
		} catch (CouncillorNotFoundException e) {
			match.sendErrorToClient(e.showError(), playerId);
		}
	}

	/**
	 * NEEDS REVISION: the specified name may be incorrect or invalid.
	 * Exception?
	 * 
	 * @return
	 */
	public Region getRegion(String regionName) {
		boolean regionFound = false;
		Region region = null;
		Region regions[] = this.board.getRegions();
		regionName = regionName.toUpperCase();
		regionName = regionName.trim();
		for (int i = 0; i < regions.length && !regionFound; i++) {
			if (regions[i].getName().equals(regionName)) {
				regionFound = true;
				region = regions[i];
			}
		}
		return region;
	}

	public boolean hasBuiltLastEmporium(Player player) {
		return player.getNumberOfEmporium() == 0;
	}

	public boolean buildEmporium(PermitTile permitTile, Player player, String cityChoice)
			throws AlreadyOwnedEmporiumException {
		boolean found = false;
		List<City> cities = permitTile.getCities();
		cityChoice = cityChoice.trim();
		cityChoice = cityChoice.toUpperCase();
		for (City tempCity : cities) {
			String cityName = String.valueOf(tempCity.getName().charAt(0));
			if (cityName.equals(cityChoice)) {
				if (tempCity.buildEmporium(player))
					found = true;
				else
					throw new AlreadyOwnedEmporiumException();
			}
		}
		return found;
	}
}
