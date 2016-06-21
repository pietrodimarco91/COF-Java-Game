package controller;

import client.actions.Action;
import client.actions.AdditionalMainAction;
import client.actions.BuyPermitTileAction;
import client.actions.ElectCouncillorAction;
import client.actions.EngageAssistantAction;
import client.actions.KingBuildEmporiumAction;
import client.actions.SendAssistantAction;
import client.actions.SimpleBuildEmporiumAction;
import client.actions.SwitchPermitTilesAction;
import exceptions.*;
import model.*;
import server.view.cli.ServerOutputPrinter;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 * Created by Gabriele on 22/05/16. This class represents the thread always
 * running while a match is on-going. It stores the core of the game engine.
 */
public class MatchHandler {

	private static final Logger logger = Logger.getLogger(MatchHandler.class.getName());

	/**
	 * This constant represents the number of the parameters decided for the
	 * board configuration
	 */
	private static final int NUMBER_OF_PARAMETERS = 5;

	private static final int MINUMUM_NUMBER_OF_PLAYERS = 2;

	private ConfigFileManager configFileManager;

	/**
	 * The ID of the match: IDs are assigned in a crescent way, starting from 0.
	 */
	private int id;

	/**
	 * The date of the launch of the match
	 */
	private Date date;

	/**
	 * A reference to the local Board for this match.
	 */
	private Board board;
	
	/**
	 * The bonus manager that assigns the bonuses to the players
	 */
	private BonusManager bonusManager;

	/**
	 * An ArrayList of players in this MatchHandler.
	 */
	private ArrayList<Player> players; // To add UML scheme

	/**
	 * This attribute represents the creator of the match
	 */
	private Player creator;

	/**
	 * Number of players in this Match
	 */
	private int numberOfPlayers; // To add UML scheme

	/**
	 * This attribute is needed to temporary save the parameters after the
	 * configuration has been set up
	 */
	private int[] configParameters;

	/**
	 * A boolean value used to know if the first player has decided the total
	 * number of players. It's true when he has finished to set the number false
	 * otherwise
	 */
	private boolean pending; // To add UML scheme

	private List<Integer> marketBuyTurn;

	/**
	 * Mapstatus: 0 wait board configuration 1 wait for players 2 wait map
	 * configuration 3 play 4 marketSellTime 5 marketBuyTime 6 finished
	 */
	private int gameStatus;

	private ExecutorService timers;

	/**
	 * 
	 */
	private Market market;

	/**
	 * id player
	 */
	private int turn;

	/**
	 * Default constructor
	 */

	public MatchHandler(int id, Date date, ClientSideConnectorInt connector, ServerSideConnectorInt serverSideConnector,
			String creatorNickName) {
		marketBuyTurn = new ArrayList<>();
		turn = 0;
		timers = Executors.newCachedThreadPool();
		logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
		this.players = new ArrayList<Player>();
		gameStatus = 0;
		configFileManager = new ConfigFileManager();
		try {
			serverSideConnector.setPlayerId(0);
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Remote Exception", e);
		}
		this.creator = new Player(connector, 0, creatorNickName);
		this.numberOfPlayers = MINUMUM_NUMBER_OF_PLAYERS;
		this.players.add(creator);
		this.id = id;
		this.date = date;
		this.configParameters = new int[NUMBER_OF_PARAMETERS];
		this.pending = false;
		this.market = new Market();
		logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
		ServerOutputPrinter.printLine("[MATCH " + id + "] New Game Session started!");
	}

	@Override
	public String toString() {
		String string = "";
		string += "Match number " + this.id + "\n";
		string += "Launched on: ";
		DateFormat dateFormat = new SimpleDateFormat();
		string += dateFormat.format(date) + "\n";
		return string;
	}

	/**
	 * @return
	 */
	public boolean isFull() {
		return this.players.size() >= this.numberOfPlayers;
	}

	/**
	 * 
	 */
	public ArrayList<Player> getPlayers() {
		return this.players;
	}

	/**
	 * 
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * @return
	 */
	public void addPlayer(ClientSideConnectorInt connector, ServerSideConnectorInt serverSideConnector, int id,
			String nickName) {
		try {
			serverSideConnector.setPlayerId(id);
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Remote Exception", e);
		}
		Player player = new Player(connector, id, nickName);
		this.players.add(player);
	}

	public void setConfigObject(ConfigObject config, int playerId) {
		if (gameStatus != GameStatusConstants.BOARD_CONFIG) {
			sendErrorToClient("Game status isn't 'Board Configuration'", playerId);
			return;
		}
		if (creator.getId() != playerId) {
			sendErrorToClient("You're not the match creator, you are not allowed to perform this action!", playerId);
			return;
		}
		try {
			configFileManager.createConfiguration(config.getNumberOfPlayers(), config.getRewardTokenBonusNumber(),
					config.getPermitTileBonusNumber(), config.getNobilityTrackBonusNumber(),
					config.getLinksBetweenCities());
			numberOfPlayers = config.getNumberOfPlayers();
			configParameters = new int[] { config.getNumberOfPlayers(), config.getRewardTokenBonusNumber(),
					config.getPermitTileBonusNumber(), config.getNobilityTrackBonusNumber(),
					config.getLinksBetweenCities() };
			GameInitializator initializator = new GameInitializator(this.id, this.configParameters, this, this.players,
					MINUMUM_NUMBER_OF_PLAYERS);
			initializator.start();
		} catch (ConfigAlreadyExistingException e) {
			sendErrorToClient(e.printError(), playerId);
		}
	}

	public void setExistingConf(int configId, int playerId) {
		if (gameStatus != GameStatusConstants.BOARD_CONFIG) {
			sendErrorToClient("Game status isn't 'Board Configuration'", playerId);
			return;
		}
		if (creator.getId() != playerId) {
			sendErrorToClient("You're not the match creator, you are not allowed to perform this action!", playerId);
			return;
		}
		try {
			ConfigObject chosenConfig = configFileManager.getConfiguration(configId);
			numberOfPlayers = chosenConfig.getNumberOfPlayers();
			configParameters = new int[] { chosenConfig.getNumberOfPlayers(), chosenConfig.getRewardTokenBonusNumber(),
					chosenConfig.getPermitTileBonusNumber(), chosenConfig.getNobilityTrackBonusNumber(),
					chosenConfig.getLinksBetweenCities() };
			GameInitializator initializator = new GameInitializator(this.id, this.configParameters, this, this.players,
					MINUMUM_NUMBER_OF_PLAYERS);
			initializator.start();
		} catch (UnexistingConfigurationException e) {
			sendErrorToClient(e.printError(), playerId);
		}
	}

	/**
	 * This method is performed when gameStatus=2 (MAP CONFIGURATION), and it
	 * checks if the graph map is connected or not. The graph map must be
	 * connected in order to play.
	 * 
	 * @param playerId
	 */
	public void checkIfGraphIsConnected(int playerId) {
		if (creator.getId() != playerId) {
			sendErrorToClient("You're not the match creator, you are not allowed to perform this action!", playerId);
			return;
		}
		if (gameStatus != GameStatusConstants.MAP_CONFIG) {
			sendErrorToClient("Game status isn't 'Map Configuration'", playerId);
			return;
		}
		if (this.board.graphIsConnected()) {
			PubSub.notifyAllClients(players, "Map Configuration is over! Game status changed to 'PLAY'!");
			ServerOutputPrinter.printLine("[MATCH " + this.id + "] Game Status changed to 'PLAY'");
			bonusManager=new BonusManager(players, board.getNobilityTrack());
			startTurns();
		} else {
			Player player = players.get(playerId);
			try {
				if (!player.playerIsOffline())
					player.getConnector()
							.sendToClient(new Packet("Error: map is not connected. Add the necessary connections.\n"));
			} catch (RemoteException e) {
				player.setPlayerOffline();
				ServerOutputPrinter
				.printLine("[SERVER] Client with nickname '" + this.players.get(playerId).getNickName()
						+ "' and ID " + playerId + " disconnected!");
				logger.log(Level.INFO, "Remote Exception", e);
			}
		}
	}

	/**
	 * 
	 * 
	 */
	public void generateConnection(String parameter, int playerId) {
		if (creator.getId() != playerId) {
			sendErrorToClient("You're not the match creator, you are not allowed to perform this action!", playerId);
			return;
		}
		if (gameStatus != GameStatusConstants.MAP_CONFIG) {
			sendErrorToClient("Game status isn't 'Map Configuration'", playerId);
			return;
		}
		City city1 = null, city2 = null, tempCity;
		List<City> cities = board.getMap();
		Iterator<City> cityIterator = cities.iterator();
		StringTokenizer tokenizer = new StringTokenizer(parameter, " ");
		String first, second;
		first = tokenizer.nextToken();
		second = tokenizer.nextToken();
		while (cityIterator.hasNext()) {
			tempCity = cityIterator.next();
			if (tempCity.getName().charAt(0) == first.charAt(0)) {
				city1 = tempCity;
			} else if (tempCity.getName().charAt(0) == second.charAt(0)) {
				city2 = tempCity;
			}
		}
		if (city1 == null || city2 == null) {
			sendErrorToClient("The specified cities were not found, make sure you choose existing city names",
					playerId);
		} else if (board.checkPossibilityOfNewConnection(city1, city2)) {
			board.connectCities(city1, city2);
			PubSub.notifyAllClients(players, "Player with nickname '" + players.get(playerId).getNickName()
					+ "' connected " + city1.getName() + " with " + city2.getName());
		} else {
			sendErrorToClient("Cities cannot be connected\n", playerId);
		}
	}

	/**
	 * 
	 * 
	 */
	public void removeConnection(String parameter, int playerId) {
		if (creator.getId() != playerId) {
			sendErrorToClient("You're not the match creator, you are not allowed to perform this action!", playerId);
			return;
		}
		if (gameStatus != GameStatusConstants.MAP_CONFIG) {
			sendErrorToClient("Game status isn't 'Map Configuration'", playerId);
			return;
		}
		City city1 = null, city2 = null, tempCity;
		List<City> cities = board.getMap();
		Iterator<City> cityIterator = cities.iterator();
		StringTokenizer tokenizer = new StringTokenizer(parameter, " ");
		String first, second;
		first = tokenizer.nextToken();
		second = tokenizer.nextToken();
		while (cityIterator.hasNext()) {
			tempCity = cityIterator.next();
			if (tempCity.getName().charAt(0) == first.charAt(0)) {
				city1 = tempCity;
			} else if (tempCity.getName().charAt(0) == second.charAt(0)) {
				city2 = tempCity;
			}
		}
		if (city1 == null || city2 == null) {
			sendErrorToClient("The specified cities were not found, make sure you choose existing city names",
					playerId);
		} else {
			board.unconnectCities(city1, city2);
			PubSub.notifyAllClients(players, "Player with nickname '" + players.get(playerId).getNickName()
					+ "' removed connection between " + city1.getName() + " and " + city2.getName());
		}
	}

	public void countDistance(String parameter, int playerId) {
		if (gameStatus < GameStatusConstants.MAP_CONFIG) {
			sendErrorToClient("Unable to perform this action, match isn't started yet", playerId);
			return;
		}
		City city1 = null, city2 = null, tempCity;
		List<City> cities = board.getMap();
		Iterator<City> cityIterator = cities.iterator();
		StringTokenizer tokenizer = new StringTokenizer(parameter, " ");
		String first, second;
		first = tokenizer.nextToken();
		second = tokenizer.nextToken();
		while (cityIterator.hasNext()) {
			tempCity = cityIterator.next();
			if (tempCity.getName().charAt(0) == first.charAt(0)) {
				city1 = tempCity;
			} else if (tempCity.getName().charAt(0) == second.charAt(0)) {
				city2 = tempCity;
			}
		}
		if (city1 != null && city2 != null) {
			if (board.countDistance(city1, city2) != -1) {
				sendMessageToClient("Distance between " + city1.getName() + " and " + city2.getName() + " is: "
						+ board.countDistance(city1, city2) + "\n", playerId);
			} else {
				sendMessageToClient(city1.getName() + " and " + city2.getName() + "are not connected\n", playerId);
			}
		} else {
			sendErrorToClient("Cities were not found", playerId);
		}
	}

	/**
	 * This method allows to know whether the current match is pending or not
	 * 
	 * @return true is it currently pending, false otherwise
	 */
	public boolean isPending() {
		return this.gameStatus == GameStatusConstants.WAIT_FOR_PLAYERS;
	}

	/**
	 * This method allows to make a player win all the possible bonuses after
	 * the construction of an emporium
	 * 
	 * @param city
	 *            the city where the emporium has been built
	 * @param player
	 *            the player who built the emporium
	 */
	public void winBuildingBonuses(City city, Player player) {
		winRewardTokensFromOwnedCities(city, player);
		winImportantBonuses(city, player);
	}

	public void winRewardTokensFromOwnedCities(City city, Player player) {
		Tile rewardToken = city.winBonus();
		List<City> ownedCities = board.getNearbyOwnedCities(player, city);
		PubSub.notifyAllClients(players, "Player '" + player + "' has just won the following Reward Token:\n"
				+ rewardToken + " after building an Emporium in " + city.getName());
		bonusManager.takeBonusFromTile(rewardToken, player);
		for (City ownedCity : ownedCities) {
			rewardToken = ownedCity.winBonus();
			PubSub.notifyAllClients(players, "Player '" + player + "' has just won the following Reward Token:\n"
					+ rewardToken + " from  " + ownedCity.getName() + ", as it is connected to " + city.getName());
			bonusManager.takeBonusFromTile(rewardToken, player);
		}
	}

	public void winImportantBonuses(City city, Player player) {
		Region region = city.getRegion();
		Tile colorBonus, regionBonus, kingReward;
		if (board.isEligibleForColorBonus(player, city.getColor())) {
			try {
				colorBonus = board.winColorBonus(city.getColor());
				bonusManager.takeBonusFromTile(colorBonus, player);
			} catch (NoMoreBonusException e) {
				PubSub.notifyAllClients(players, e.showError());
			}
			try {
				kingReward = board.winKingReward();
				bonusManager.takeBonusFromTile(kingReward, player);
			} catch (NoMoreBonusException e) {
				PubSub.notifyAllClients(players, e.showError());
			}
		}
		if (region.isEligibleForRegionBonus(player)) {
			try {
				regionBonus = region.winRegionBonus(player);
				bonusManager.takeBonusFromTile(regionBonus, player);
			} catch (NoMoreBonusException e) {
				PubSub.notifyAllClients(players, e.showError());
			}
			try {
				kingReward = board.winKingReward();
				bonusManager.takeBonusFromTile(kingReward, player);
			} catch (NoMoreBonusException e) {
				PubSub.notifyAllClients(players, e.showError());
			}
		}
	}

	public void buyPermitTile(BuyPermitTileAction buyPermitTileAction, int playerId) {
		if (players.get(playerId).hasPerformedMainAction()) {
			sendErrorToClient("You've already performed a Main Action for this turn!", playerId);
			return;
		}
		String regionName;
		ArrayList<String> chosenPoliticCards;
		int slot;
		int numberOfCouncillorSatisfied;
		int playerPayment = 0;
		Region region;
		PermitTileDeck regionDeck;

		regionName = buyPermitTileAction.getRegion();
		chosenPoliticCards = buyPermitTileAction.getPoliticCardColors();
		slot = buyPermitTileAction.getSlot();
		region = getRegion(regionName);

		numberOfCouncillorSatisfied = region.numberOfCouncillorsSatisfied(chosenPoliticCards);
		Player player = this.players.get(playerId);
		try {
			if (numberOfCouncillorSatisfied > 0)
				throw new UnsufficientCouncillorsSatisfiedException();
			playerPayment = CoinsManager.paymentForPermitTile(numberOfCouncillorSatisfied);
			player.removeCardsFromHand(chosenPoliticCards);
			regionDeck = region.getDeck();
			player.performPayment(playerPayment);
			Tile permitTile = regionDeck.drawPermitTile(slot);
			player.addUnusedPermitTiles(permitTile);
			PubSub.notifyAllClients(players, "Player '" + player.getNickName()
					+ "' satisfied the Council of the region '" + regionName + "' and bought a Permit Tile");
			bonusManager.takeBonusFromTile(permitTile, player);
			player.mainActionDone(true);
			if (player.hasPerformedQuickAction()) {
				notifyEndOfTurn(playerId);
				player.resetTurn();
			}
		} catch (UnsufficientCouncillorsSatisfiedException e) {
			sendErrorToClient(e.showError(), playerId);
		} catch (UnsufficientCoinsException e) {
			for (int i = 0; i < chosenPoliticCards.size(); i++)
				player.addCardOnHand(new PoliticCard(chosenPoliticCards.get(i)));
			sendErrorToClient(e.showError(), playerId);
		} catch (InvalidSlotException e) {
			player.addCoins(playerPayment);
			for (int i = 0; i < chosenPoliticCards.size(); i++)
				player.addCardOnHand(new PoliticCard(chosenPoliticCards.get(i)));
			sendErrorToClient(e.showError(), playerId);
		}
	}

	/**
	 * This method allows a player to draw a Politic Card at the beginning of
	 * his turn.
	 */
	public void drawPoliticCard(Player player) {
		PoliticCard card = PoliticCardDeck.generateRandomPoliticCard();
		player.addCardOnHand(card);
		sendMessageToClient("You've drawn a " + card.getColorCard() + " Politic Card", player.getId());
	}

	/**
	 * NEEDS IMPLEMENTATION
	 * 
	 * @return
	 * @throws UnsufficientCoinsException
	 */
	public void buildEmporiumWithKingsHelp(KingBuildEmporiumAction kingBuildEmporiumAction, int playerId) {
		if (players.get(playerId).hasPerformedMainAction()) {
			sendErrorToClient("You've already performed a Main Action for this turn!", playerId);
			return;
		}
		String cityName;
		ArrayList<String> chosenPoliticCards;
		int numberOfCouncillorSatisfied;
		int playerPayment = 0;
		int coinsToPay = 0;
		Player player = this.players.get(playerId);
		cityName = kingBuildEmporiumAction.getCityName();
		chosenPoliticCards = kingBuildEmporiumAction.getPoliticCardColors();
		numberOfCouncillorSatisfied = this.board.numberOfCouncillorsSatisfied(chosenPoliticCards);
		try {
			if (numberOfCouncillorSatisfied == 0)
				throw new UnsufficientCouncillorsSatisfiedException();

			City cityTo = board.getCityFromName(cityName);
			City cityFrom = board.findKingCity();
			coinsToPay = board.countDistance(cityFrom, cityTo) * 2;
			playerPayment = CoinsManager.paymentForPermitTile(numberOfCouncillorSatisfied);
			player.performPayment(playerPayment + coinsToPay);
			player.removeCardsFromHand(chosenPoliticCards);
			if (coinsToPay > 0) {
				board.moveKing(cityTo);
			}
			if (!cityTo.buildEmporium(player))
				throw new AlreadyOwnedEmporiumException();
			else {
				PubSub.notifyAllClients(players, "Player " + player.getNickName() + " has built an Emporium in "
						+ cityTo.getName() + " with king's help");
				winBuildingBonuses(cityTo, player);
				player.mainActionDone(true);
				if (hasBuiltLastEmporium(player)) {
					PubSub.notifyAllClients(this.players, "Player " + player.getNickName()
							+ " has built his last Emporium!!\n This is your last turn!");
					gameStatus = GameStatusConstants.FINISH;
					player.addVictoryPoints(3);
				}
				if (player.hasPerformedQuickAction()) {
					notifyEndOfTurn(playerId);
					player.resetTurn();
				}
			}
		} catch (UnsufficientCouncillorsSatisfiedException e) {
			sendErrorToClient(e.showError(), playerId);
		} catch (UnsufficientCoinsException e) {
			sendErrorToClient(e.showError(), playerId);
		} catch (AlreadyOwnedEmporiumException e) {
			player.addCoins(playerPayment + coinsToPay);
			for (int i = 0; i < chosenPoliticCards.size(); i++)
				player.addCardOnHand(new PoliticCard(chosenPoliticCards.get(i)));
			sendErrorToClient(e.showError(), playerId);
		}
	}

	/**
	 * @return
	 */

	public void performAdditionalMainAction(AdditionalMainAction action, int playerId) {
		if (players.get(playerId).hasPerformedQuickAction()) {
			sendErrorToClient("You've already performed a Quick Action for this turn!", playerId);
			return;
		}
		Player player = players.get(playerId);
		try {
			if (player.getNumberOfAssistants() < 3)
				throw new UnsufficientCoinsException();
			player.removeMoreAssistants(3);
			player.mainActionDone(false);
		} catch (UnsufficientCoinsException e) {
			sendErrorToClient(e.showError(), playerId);
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
			sendErrorToClient("You've already performed a Main Action for this turn!", playerId);
			return;
		}
		String regionName;
		String councillorColor;
		regionName = electCouncillorAction.getRegion();
		councillorColor = electCouncillorAction.getColor();
		Player player = this.players.get(playerId);
		Region region = this.getRegion(regionName);
		try {
			region.electCouncillor(councillorColor);
			player.addCoins(4);
			PubSub.notifyAllClients(players, "Player '" + player.getNickName() + "' elected a " + councillorColor
					+ " Councillor in " + regionName);
			player.mainActionDone(true);
			if (player.hasPerformedQuickAction()) {
				notifyEndOfTurn(playerId);
				player.resetTurn();
			}
		} catch (CouncillorNotFoundException e) {
			sendErrorToClient(e.getMessage(), playerId);
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
			sendErrorToClient("You've already performed a Quick Action for this turn!", playerId);
			return;
		}
		Player player = this.players.get(playerId);
		int coins = player.getCoins();
		try {
			if (coins < 3)
				throw new UnsufficientCoinsException();
			player.performPayment(3);
			player.addAssistant();
			PubSub.notifyAllClients(players, "Player " + player.getNickName() + " bought an Assistant!");
			player.quickActionDone();
			if (player.hasPerformedMainAction()) {
				notifyEndOfTurn(playerId);
				player.resetTurn();
			}
		} catch (UnsufficientCoinsException e) {
			sendErrorToClient(e.showError(), playerId);
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
			sendErrorToClient("You've already performed a Quick Action for this turn!", playerId);
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
					"Player " + player.getNickName() + " swhitched Permit Tile in " + regionName + "!");
			player.quickActionDone();
			if (player.hasPerformedMainAction()) {
				notifyEndOfTurn(playerId);
				player.resetTurn();
			}
		} catch (UnsufficientAssistantNumberException e) {
			sendErrorToClient(e.showError(), playerId);
		}

	}

	/**
	 * MUST BE FIXED IMMEDIATELY! COMPILATION ERRORS
	 * 
	 * @return
	 */

	public void buildEmporiumWithPermitTile(SimpleBuildEmporiumAction simpleBuildEmporium, int playerId) {
		if (players.get(playerId).hasPerformedMainAction()) {
			sendErrorToClient("You've already performed a Quick Action for this turn!", playerId);
			return;
		}
		int permitTileId;
		String cityName;
		PermitTile tempPermitTile;
		Player player = this.players.get(playerId);

		permitTileId = simpleBuildEmporium.getPermitTileID();
		cityName = simpleBuildEmporium.getCityName();
		tempPermitTile = (PermitTile) player.getUnusedPermitTileFromId(permitTileId);
		try {
			if (!buildEmporium(tempPermitTile, player, cityName))
				throw new CityNotFoundFromPermitTileException();
			PubSub.notifyAllClients(players,
					"Player " + player.getNickName() + " build an Emporium in " + cityName + "!");
			winBuildingBonuses(board.getCityFromName(cityName), player);
			player.mainActionDone(true);
			if (hasBuiltLastEmporium(player)) {
				PubSub.notifyAllClients(this.players,
						"Player " + player.getNickName() + " has built his last Emporium!!\n This is your last turn!");
				gameStatus = GameStatusConstants.FINISH;
				player.addVictoryPoints(3);
			}
			if (player.hasPerformedQuickAction()) {
				notifyEndOfTurn(playerId);
				player.resetTurn();
			}
		} catch (CityNotFoundFromPermitTileException e) {
			sendErrorToClient(e.showError(), playerId);
		} catch (AlreadyOwnedEmporiumException e) {
			sendErrorToClient(e.showError(), playerId);
		}
	}

	/**
	 * 
	 * @return
	 */
	public void sendAssistantToElectCouncillor(SendAssistantAction sendAssistantAction, int playerId) {
		if (players.get(playerId).hasPerformedQuickAction()) {
			sendErrorToClient("You've already performed a Quick Action for this turn!", playerId);
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
					+ councillorColor + "Councillor in " + regionName + "!");
			player.quickActionDone();
			if (player.hasPerformedMainAction()) {
				notifyEndOfTurn(playerId);
				player.resetTurn();
			}
		} catch (UnsufficientAssistantNumberException e) {
			sendErrorToClient(e.showError(), playerId);
		} catch (CouncillorNotFoundException e) {
			sendErrorToClient(e.showError(), playerId);
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

	/**
	 * 
	 */
	public boolean checkCorrectCityNameChoice(PermitTile permitTile, String cityChoice) {
		List<City> cities = permitTile.getCities();
		cityChoice = cityChoice.trim();
		cityChoice = cityChoice.toUpperCase();
		String allCity = "";
		for (City tempCities : cities) {
			allCity += tempCities.getName();
		}
		return allCity.contains(cityChoice);
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
		for (City tempCities : cities) {
			if (tempCities.getName().equals(cityChoice)) {
				if (tempCities.buildEmporium(player))
					found = true;
				else
					throw new AlreadyOwnedEmporiumException();
			}
		}
		return found;
	}

	/**
	 * This method must understand which action to perform for the specified
	 * player, depending on the dynamic dispatching of the Action
	 * 
	 * @param action
	 * @param playerId
	 */

	public void evaluateAction(Action action, int playerId) {
		if (gameStatus != GameStatusConstants.PLAY) {
			sendErrorToClient("You can't perform an action at the moment!", playerId);
			return;
		}
		if (this.turn != playerId) {
			sendErrorToClient("It's not your turn!", playerId);
			return;
		}
		if (action instanceof AdditionalMainAction) {
			AdditionalMainAction mainAction = (AdditionalMainAction) action;
			this.performAdditionalMainAction(mainAction, playerId);
		} else if (action instanceof BuyPermitTileAction) {
			BuyPermitTileAction buyPermitTileAction = (BuyPermitTileAction) action;
			this.buyPermitTile(buyPermitTileAction, playerId);
		} else if (action instanceof ElectCouncillorAction) {
			ElectCouncillorAction electConcillorAction = (ElectCouncillorAction) action;
			this.electCouncillor(electConcillorAction, playerId);
		} else if (action instanceof EngageAssistantAction) {
			EngageAssistantAction engageAssistanAction = (EngageAssistantAction) action;
			this.engageAssistant(engageAssistanAction, playerId);
		} else if (action instanceof KingBuildEmporiumAction) {
			KingBuildEmporiumAction kingBuildEmporiumAction = (KingBuildEmporiumAction) action;
			this.buildEmporiumWithKingsHelp(kingBuildEmporiumAction, playerId);
		} else if (action instanceof SendAssistantAction) {
			SendAssistantAction sendAssistantAction = (SendAssistantAction) action;
			this.sendAssistantToElectCouncillor(sendAssistantAction, playerId);
		} else if (action instanceof SimpleBuildEmporiumAction) {
			SimpleBuildEmporiumAction simpleBuildEmporium = (SimpleBuildEmporiumAction) action;
			this.buildEmporiumWithPermitTile(simpleBuildEmporium, playerId);
		} else if (action instanceof SwitchPermitTilesAction) {
			SwitchPermitTilesAction switchPermitTilesAction = (SwitchPermitTilesAction) action;
			this.switchPermitTile(switchPermitTilesAction, playerId);
		}

	}

	public void messageFromClient(String messageString, int playerId) {
	}

	public void buyEvent(MarketEvent marketEvent, int playerId) {
		if (gameStatus != GameStatusConstants.MARKET_BUY) {
			sendErrorToClient("Game status isn't 'Market'", playerId);
			return;
		} else if (playerId == marketBuyTurn.get(0)) {
			MarketEventBuy event = (MarketEventBuy) marketEvent;
			try {
				market.buyItemOnSale(players.get(playerId), event.getItemId());
				marketBuyTurn.remove(0);
				PubSub.notifyAllClients(players, "Player '" + players.get(playerId).getNickName()
						+ "' has just bought the item with ID " + event.getItemId() + " from the Market!");
			} catch (UnsufficientCoinsException e) {
				sendErrorToClient(e.showError(), playerId);
			} catch (ItemNotFoundException e) {
				sendErrorToClient(e.showError(), playerId);
			}
		} else {
			sendErrorToClient("You're not allowed to buy now, please wait for your turn!", playerId);
		}
	}

	public void sellEvent(MarketEvent marketEvent, int playerId) {
		if (gameStatus != GameStatusConstants.MARKET_SELL) {
			sendErrorToClient("Game status isn't 'Market'", playerId);
			return;
		}

		Player player = players.get(playerId);
		MarketEventSell event = (MarketEventSell) marketEvent;
		ItemFactory factory = new ConcreteItemFactory();
		ItemOnSale item;
		String header = event.getHeader();
		switch (header) {
		case "PERMITTILE":
			int permitTileId = event.getPermitTileId();
			Tile permitTile = player.sellPermitTile(permitTileId); // exception
																	// handling
																	// should be
																	// done!
			if (permitTile == null) {
				sendErrorToClient("Ops! A PermitTile with the specified ID was not found!", playerId);
				return;
			}
			item = factory.createPermitTileOnSale(permitTile, player, event.getPrice());
			market.putItemOnSale(item);
			PubSub.notifyAllClients(players, "Player '" + player.getNickName()
					+ "' has just put a new Item on sale in the Market!\nInfo:\n" + item.toString());
			break;
		case "POLITICCARD":
			boolean cardFound = false;
			String politicCardColor = event.getPoliticCardColor();
			cardFound = player.checkIfYouOwnThisCard(politicCardColor, player.getPoliticCards());
			if (cardFound) {
				item = factory.createPoliticCardOnSale(player.sellPoliticCard(politicCardColor), player,
						event.getPrice());
				market.putItemOnSale(item);
				PubSub.notifyAllClients(players, "Player '" + player.getNickName()
						+ "' has just put a new Item on sale in the Market!\nInfo:\n" + item.toString());
			} else {
				sendErrorToClient("You don't own a PoliticCard of the color you specified!", playerId);
			}
			break;
		case "ASSISTANT":
			if (player.getNumberOfAssistants() > 0) {
				item = factory.createAssistantOnSale(player, event.getPrice());
				market.putItemOnSale(item);
				PubSub.notifyAllClients(players, "Player '" + player.getNickName()
						+ "' has just put a new Item on sale in the Market!\nInfo:\n" + item.toString());
			} else {
				sendErrorToClient("You haven't got Assistants in your pool!", playerId);
			}
			break;
		default:
		}
	}

	public void sendMarketStatus() {
		PubSub.notifyAllClients(players, market.toString());
	}

	public void sendErrorToClient(String error, int playerId) {
		String message = "[SERVER] Error: " + error;
		Player player = players.get(playerId);
		try {
			if (!player.playerIsOffline())
				player.getConnector().sendToClient(new Packet(message));
		} catch (RemoteException e) {
			player.setPlayerOffline();

			ServerOutputPrinter
			.printLine("[SERVER] Client with nickname '" + this.players.get(playerId).getNickName()
					+ "' and ID " + playerId + " disconnected!");
			logger.log(Level.INFO, "Remote Exception", e);

		}
	}

	public void sendMessageToClient(String s, int playerId) {
		String message = "[MATCH " + this.id + "] " + s;
		Player player = players.get(playerId);
		try {
			if (!player.playerIsOffline())
				player.getConnector().sendToClient(new Packet(message));
		} catch (RemoteException e) {
			player.setPlayerOffline();

			ServerOutputPrinter
			.printLine("[SERVER] Client with nickname '" + this.players.get(playerId).getNickName()
					+ "' and ID " + playerId + " disconnected!");
			e.printStackTrace();

		}
	}

	public void sendBoardStatus(int playerId) {
		if (gameStatus > GameStatusConstants.WAIT_FOR_PLAYERS) {
			sendMessageToClient(
					board.toString() + board.printMatrix() + board.printConnections() + board.printDistances(),
					playerId);
		} else {
			sendErrorToClient("Board isn't configured yet", playerId);
		}
	}

	public void sendConfigurations(int playerId) {
		if (gameStatus != GameStatusConstants.BOARD_CONFIG) {
			sendErrorToClient("Game status isn't 'Board Configuration'", playerId);
			return;
		}
		ArrayList<ConfigObject> configurations = configFileManager.getConfigurations();
		String string = "";
		for (ConfigObject config : configurations) {
			string += config.toString() + "\n";
		}
		sendMessageToClient(string, playerId);
	}

	public void sendPlayerStatus(int playerId) {
		if (gameStatus < GameStatusConstants.PLAY) {
			sendErrorToClient("The match isn't started yet", playerId);
			return;
		}
		sendMessageToClient(players.get(playerId).toString(), playerId);
	}

	public void setGameStatus(int i) {
		this.gameStatus = i;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public void setNumberOfPlayers(int i) {
		this.numberOfPlayers = i;
	}

	public void setPlayerNickName(int playerId, String nickName) {
		this.players.get(playerId).setPlayerNickName(nickName);
	}

	public void notifyEndOfTurn(int playerId) {
		if (playerId == turn) {
			PubSub.notifyAllClients(players,
					"Player '" + players.get(playerId).getNickName() + "', your turn is over.");
			nextTurn();
		}
	}

	public void startTurns() {
		this.gameStatus = GameStatusConstants.PLAY; // we're ready to play!
		if (!players.get(turn).playerIsOffline()) {
			PubSub.notifyAllClients(players,
					"Player '" + players.get(turn).getNickName() + "', it's your turn. Perform your actions!");
			drawPoliticCard(players.get(turn));
			timers.submit(new TurnTimerThread(this, turn));
		} else
			nextTurn();
	}

	public void nextTurn() {

		if (turn == (players.size() - 1)) {
			if (GameStatusConstants.FINISH == gameStatus) {
				PubSub.notifyAllClients(this.players, "Turns are over!");
				notifyMatchWinner();
				return;
			}

			turn = 0;
			startMarketSellTime();
		} else {
			turn++;
			if (!players.get(turn).playerIsOffline()) {
				PubSub.notifyAllClients(players,
						"Player '" + players.get(turn).getNickName() + "', it's your turn. Perform your actions!");
				drawPoliticCard(players.get(turn));
				timers.submit(new TurnTimerThread(this, turn));
			} else
				nextTurn();
		}
	}

	private void notifyMatchWinner() {
		List<Player> playersInDraw=new ArrayList<>();
		Player winner = null;
		int maxVictoryPoints = 0, maxAssistants=0, maxPoliticCardsInHand=0;		
		
		assignFinalPermitTilePoints();
		assignFinalNobilityTrackPoints();
		
		for(Player player : players) {
			if (player.getVictoryPoints() > maxVictoryPoints) {
				maxVictoryPoints = player.getVictoryPoints();
				winner = player;
				playersInDraw.clear();
			} else if(player.getVictoryPoints() == maxVictoryPoints) {
				if(!playersInDraw.contains(winner))
					playersInDraw.add(winner);
				playersInDraw.add(player);
			}
		}
		if(playersInDraw.isEmpty()&&winner!=null)
			PubSub.notifyAllClients(this.players, "Player " +winner.getNickName() + " is the winner of the Match!");
		else {
			for(Player player : playersInDraw) {
				if(player.getPoliticCards().size()+player.getNumberOfAssistants()>maxAssistants+maxPoliticCardsInHand) {
					winner=player;
					maxPoliticCardsInHand=player.getPoliticCards().size();
					maxAssistants=player.getNumberOfAssistants();
				}
			}
			if(winner!=null)
				PubSub.notifyAllClients(this.players, "Player " +winner.getNickName() + " is the winner of the Match!");
		}
	}
	
	public void assignFinalNobilityTrackPoints() {
		NobilityTrack nobilityTrack=board.getNobilityTrack();
		List<Player> playersInFirstPosition = new ArrayList<>();
		List<Player> playersInSecondPosition = new ArrayList<>();
		for(Player player : players ) {
			if(player.getPositionInNobilityTrack()==nobilityTrack.getLength()) {
				playersInFirstPosition.add(player);
			}
			if(player.getPositionInNobilityTrack()==nobilityTrack.getLength()-1) {
				playersInSecondPosition.add(player);
			}
		}
		if(playersInFirstPosition.size()==1) {
			playersInFirstPosition.get(0).addVictoryPoints(5);
			if(playersInSecondPosition.size()==1)
				playersInSecondPosition.get(0).addVictoryPoints(2);
			else {
				for(Player player : playersInSecondPosition) {
					player.addVictoryPoints(2);
				}
			}
		} else {
			for(Player player : playersInFirstPosition)
				player.addVictoryPoints(5);
		}
	}
	
	public void assignFinalPermitTilePoints() {
		Iterator<Player> iterator = players.iterator();
		Player player, tempWinner=null;
		int maxNumberOfPermitTile=0;
		while(iterator.hasNext()){
			player=iterator.next();
			if(player.getNumberOfPermitTile()+player.getNumberOfUsedPermitTile()>maxNumberOfPermitTile){
				maxNumberOfPermitTile=player.getNumberOfPermitTile()+player.getNumberOfUsedPermitTile();
				tempWinner=player;
			}
		}
		if(tempWinner!=null)
			tempWinner.addVictoryPoints(3);
	}

	private void startMarketSellTime() {
		gameStatus = GameStatusConstants.MARKET_SELL;
		PubSub.notifyAllClients(players, "Game Status changed to 'Market Sell Time'");
		sendMarketStatus();
		timers.submit(new MarketTimerThread(this));
	}

	public void startMarketBuyTime() {
		this.gameStatus = GameStatusConstants.MARKET_BUY;
		PubSub.notifyAllClients(players, "Game Status changed to 'Market Buy Time'");
		marketBuyTurn.clear();
		for (Player player : players) {
			marketBuyTurn.add(new Integer(player.getId()));
		}
		Collections.shuffle(marketBuyTurn);
		String message = "In order to buy items from the Market, players must respect this random order:" + "\n";
		for (Integer id : marketBuyTurn) {
			message += "Player '" + players.get(id.intValue()).getNickName() + " ID: " + id + "\n";
		}
		PubSub.notifyAllClients(players, message);
	}

	public void chat(int playerId, String messageString) {
		PubSub.chatMessage(playerId, players, messageString);
	}

	public void setPlayerOffline(int playerId) {
		Player player = this.players.get(playerId);
		player.setPlayerOffline();
	}
}