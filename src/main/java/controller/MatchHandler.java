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
import filehandler.ConfigFileManager;
import filehandler.ConfigObject;
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
	 * This class handles all the main and quick actions received from players
	 */
	private MatchActionsHandler actionsHandler;

	/**
	 * An ArrayList of players in this MatchHandler.
	 */
	private List<Player> players; // To add UML scheme

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
	 * Game Status: 0 wait board configuration 1 wait for players 2 wait map
	 * configuration 3 play 4 marketSellTime 5 marketBuyTime 6 finished
	 */
	private int gameStatus;

	private ExecutorService timers;

	/**
	 * A reference to the market of the match.
	 */
	private Market market;

	/**
	 * The Player whose turn is currently active
	 */
	private Player currentPlayer;

	/**
	 * The Player whose turn in the market is currently active
	 */
	private Player playerMarketTurn;

	/**
	 * The iterator for the player turns
	 */
	private PlayerTurnIterator playerTurnIterator;

	/**
	 * This random iterator is used to iterate over the players during the
	 * market buy time
	 */
	private RandomPlayerIterator randomPlayerIterator;

	/**
	 * Default constructor: this constructor is used during the game, and allows
	 * to initialize the match. The MatchHandler handles the core of the server
	 * game controller, which directly communicates with the model.
	 * 
	 * @param id
	 *            the match ID, in chronological order of creation
	 * @param date
	 *            the Date of when the match has been created
	 * @param connector
	 *            the connector of the first player (creator)
	 * @param serverSideConnector
	 *            it's used only to set the player ID to the creator's server
	 *            side connector
	 * @param creatorNickName
	 *            the nickname of the creator of the match
	 */
	public MatchHandler(int id, Date date, ClientSideConnectorInt connector, ServerSideConnectorInt serverSideConnector,
			String creatorNickName) {
		timers = Executors.newCachedThreadPool();
		logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
		this.players = Collections.synchronizedList(new ArrayList<Player>());
		gameStatus = GameStatusConstants.BOARD_CONFIG;
		configFileManager = new ConfigFileManager();
		try {
			serverSideConnector.setPlayerId(0);
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Remote Exception", e);
		}
		this.creator = new Player(connector, 0, creatorNickName);
		this.currentPlayer = creator;
		this.numberOfPlayers = MINUMUM_NUMBER_OF_PLAYERS;
		this.players.add(creator);
		this.id = id;
		this.date = date;
		this.configParameters = new int[NUMBER_OF_PARAMETERS];
		this.market = new Market();
		ServerOutputPrinter.printLine("[MATCH " + id + "] New Game Session started!");
	}

	/**
	 * IMPORTANT! This constructor is used only for testing purposes, as it
	 * doesn't initialize those components related to the connection.
	 * 
	 * @param id
	 *            the match id
	 * @param date
	 *            date of the match
	 */
	public MatchHandler(int id, Date date) {
		logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
		this.players = new ArrayList<Player>();
		gameStatus = GameStatusConstants.BOARD_CONFIG;
		configFileManager = new ConfigFileManager();
		this.creator = new Player(0);
		this.numberOfPlayers = MINUMUM_NUMBER_OF_PLAYERS;
		this.players.add(creator);
		this.id = id;
		this.date = date;
		this.configParameters = new int[NUMBER_OF_PARAMETERS];
		this.market = new Market();
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
	 * This method allows to know whether the current match is pending or not
	 * 
	 * @return true is it currently pending, false otherwise
	 */
	public boolean isPending() {
		return this.gameStatus == GameStatusConstants.WAIT_FOR_PLAYERS;
	}

	/**
	 * 
	 */
	public synchronized List<Player> getPlayers() {
		return this.players;
	}

	/**
	 * 
	 */
	public int getId() {
		return this.id;
	}

	public BonusManager getBonusManager() {
		return this.bonusManager;
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
		sendListOfPlayers();
	}

	/**
	 * IMPORTANT: this method is used only for test purposes, as the original
	 * one requires connection components.
	 * 
	 * @param id
	 *            the player id
	 */
	public void addPlayer(int id) {
		this.players.add(new Player(id));
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
			GameInitializator initializator = new GameInitializator(this.id, this.configParameters, this,
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
			GameInitializator initializator = new GameInitializator(this.id, this.configParameters, this,
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
			PubSub.notifyAllClients(players, "Map Configuration is over! Game status changed to 'PLAY'!", board);
			ServerOutputPrinter.printLine("[MATCH " + this.id + "] Game Status changed to 'PLAY'");
			initializeMatchComponents();
			startTurns();
		} else {
			Player player = players.get(playerId);
			try {
				if (!player.playerIsOffline())
					player.getConnector()
							.sendToClient(new Packet("Error: map is not connected. Add the necessary connections.\n"));
			} catch (RemoteException e) {
				player.setPlayerOffline();
				ServerOutputPrinter.printLine("[SERVER] Client with nickname '"
						+ this.players.get(playerId).getNickName() + "' and ID " + playerId + " disconnected!");
			}
		}
	}

	public void initializeMatchComponents() {
		bonusManager = new BonusManager(players, board, this);
		playerTurnIterator = new PlayerTurnIterator(players);
		actionsHandler = new MatchActionsHandler(this, board, players);
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
					+ "' connected " + city1.getName() + " with " + city2.getName(), board);
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
					+ "' removed connection between " + city1.getName() + " and " + city2.getName(), board);
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
				+ rewardToken + " after building an Emporium in " + city.getName(), board);
		updateClient(player.getId());
		bonusManager.takeBonusFromTile(rewardToken, player);
		for (City ownedCity : ownedCities) {
			rewardToken = ownedCity.winBonus();
			player.rewardTokenWon(); //TEST PURPOSES
			PubSub.notifyAllClients(players, "Player '" + player + "' has just won the following Reward Token:\n"
					+ rewardToken + " from  " + ownedCity.getName() + ", as it is connected to " + city.getName(),
					board);
			updateClient(player.getId());
			bonusManager.takeBonusFromTile(rewardToken, player);
		}
	}

	public void winImportantBonuses(City city, Player player) {
		Region region = city.getRegion();
		Tile colorBonus, regionBonus, kingReward;
		if (board.isEligibleForColorBonus(player, city.getColor())) {
			try {
				colorBonus = board.winColorBonus(city.getColor());
				player.colorBonusWon(); //TEST PURPOSES
				bonusManager.takeBonusFromTile(colorBonus, player);
			} catch (NoMoreBonusException e) {
				PubSub.notifyAllClients(players, e.showError(), board);
			}
			try {
				kingReward = board.winKingReward();
				player.kingRewardWon(); //TEST PURPOSES
				bonusManager.takeBonusFromTile(kingReward, player);
			} catch (NoMoreBonusException e) {
				PubSub.notifyAllClients(players, e.showError(), board);
			}
		}
		if (region.isEligibleForRegionBonus(player)) {
			try {
				regionBonus = region.winRegionBonus(player);
				player.regionBonusWon(); //TEST PURPOSES
				bonusManager.takeBonusFromTile(regionBonus, player);
			} catch (NoMoreBonusException e) {
				PubSub.notifyAllClients(players, e.showError(), board);
			}
			try {
				kingReward = board.winKingReward();
				player.kingRewardWon(); //TEST PURPOSES
				bonusManager.takeBonusFromTile(kingReward, player);
			} catch (NoMoreBonusException e) {
				PubSub.notifyAllClients(players, e.showError(), board);
			}
		}
	}

	/**
	 * This method allows a player to draw a Politic Card at the beginning of
	 * his turn.
	 */
	public void drawPoliticCard(Player player) {
		PoliticCard card = PoliticCardDeck.generateRandomPoliticCard();
		player.addCardOnHand(card);
		updateClient(player.getId());
		sendMessageToClient("You've drawn a " + card.getColorCard() + " Politic Card", player.getId());
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
		if (currentPlayer != players.get(playerId)) {
			sendErrorToClient("It's not your turn!", playerId);
			return;
		}
		if (action instanceof AdditionalMainAction) {
			AdditionalMainAction mainAction = (AdditionalMainAction) action;
			actionsHandler.performAdditionalMainAction(mainAction, playerId);
		} else if (action instanceof BuyPermitTileAction) {
			BuyPermitTileAction buyPermitTileAction = (BuyPermitTileAction) action;
			actionsHandler.buyPermitTile(buyPermitTileAction, playerId);
		} else if (action instanceof ElectCouncillorAction) {
			ElectCouncillorAction electConcillorAction = (ElectCouncillorAction) action;
			actionsHandler.electCouncillor(electConcillorAction, playerId);
		} else if (action instanceof EngageAssistantAction) {
			EngageAssistantAction engageAssistanAction = (EngageAssistantAction) action;
			actionsHandler.engageAssistant(engageAssistanAction, playerId);
		} else if (action instanceof KingBuildEmporiumAction) {
			KingBuildEmporiumAction kingBuildEmporiumAction = (KingBuildEmporiumAction) action;
			actionsHandler.buildEmporiumWithKingsHelp(kingBuildEmporiumAction, playerId);
		} else if (action instanceof SendAssistantAction) {
			SendAssistantAction sendAssistantAction = (SendAssistantAction) action;
			actionsHandler.sendAssistantToElectCouncillor(sendAssistantAction, playerId);
		} else if (action instanceof SimpleBuildEmporiumAction) {
			SimpleBuildEmporiumAction simpleBuildEmporium = (SimpleBuildEmporiumAction) action;
			actionsHandler.buildEmporiumWithPermitTile(simpleBuildEmporium, playerId);
		} else if (action instanceof SwitchPermitTilesAction) {
			SwitchPermitTilesAction switchPermitTilesAction = (SwitchPermitTilesAction) action;
			actionsHandler.switchPermitTile(switchPermitTilesAction, playerId);
		}
	}

	public void notifyMatchWinner() {// was setted private but now i changed in
										// public for the test
		List<Player> playersInDraw = new ArrayList<>();
		Player winner = null;
		int maxVictoryPoints = 0, maxAssistants = 0, maxPoliticCardsInHand = 0;

		assignFinalPermitTilePoints();
		assignFinalNobilityTrackPoints();

		for (Player player : players) {
			if (player.getVictoryPoints() > maxVictoryPoints) {
				maxVictoryPoints = player.getVictoryPoints();
				winner = player;
				playersInDraw.clear();
			} else if (player.getVictoryPoints() == maxVictoryPoints) {
				if (!playersInDraw.contains(winner))
					playersInDraw.add(winner);
				playersInDraw.add(player);
			}
		}

		if (playersInDraw.isEmpty() && winner != null) {
			winner.setPlayerWon();// added only for the test use;
			PubSub.notifyAllClients(this.players, "Player " + winner.getNickName() + " is the winner of the Match!",
					board);
		}

		else {
			for (Player player : playersInDraw) {
				if (player.getPoliticCards().size() + player.getNumberOfAssistants() > maxAssistants
						+ maxPoliticCardsInHand) {
					winner = player;
					maxPoliticCardsInHand = player.getPoliticCards().size();
					maxAssistants = player.getNumberOfAssistants();
				}
			}
			if (winner != null) {
				winner.setPlayerWon();// added only for the test uses
				PubSub.notifyAllClients(this.players, "Player " + winner.getNickName() + " is the winner of the Match!",
						board);
			}

		}
	}

	public void assignFinalNobilityTrackPoints() {
		List<Player> playersInFirstPosition = new ArrayList<>();
		List<Player> playersInSecondPosition = new ArrayList<>();
		int firstPosition = 0;
		int secondPosition = 0;
		for (Player player : players) {
			if (player.getPositionInNobilityTrack() >= firstPosition) {
				firstPosition = player.getPositionInNobilityTrack();
				playersInFirstPosition.add(player);
			}
		}
		for (Player player : players) {
			if (player.getPositionInNobilityTrack() >= secondPosition
					&& player.getPositionInNobilityTrack() < firstPosition) {
				secondPosition = player.getPositionInNobilityTrack();
				playersInSecondPosition.add(player);
			}
		}

		if (playersInFirstPosition.size() == 1) {
			playersInFirstPosition.get(0).addVictoryPoints(5);
			if (playersInSecondPosition.size() == 1)
				playersInSecondPosition.get(0).addVictoryPoints(2);
			else {
				for (Player player : playersInSecondPosition) {
					player.addVictoryPoints(2);
				}
			}
		} else {
			for (Player player : playersInFirstPosition)
				player.addVictoryPoints(5);
		}
	}

	public void assignFinalPermitTilePoints() {
		Iterator<Player> iterator = players.iterator();
		Player player, tempWinner = null;
		int maxNumberOfPermitTile = 0;
		while (iterator.hasNext()) {
			player = iterator.next();
			if (player.getNumberOfPermitTile() + player.getNumberOfUsedPermitTile() > maxNumberOfPermitTile) {
				maxNumberOfPermitTile = player.getNumberOfPermitTile() + player.getNumberOfUsedPermitTile();
				tempWinner = player;
			}
		}
		if (tempWinner != null)
			tempWinner.addVictoryPoints(3);
	}

	public void notifyEndOfTurn(Player player) {
		if (player == currentPlayer) {
			PubSub.notifyAllClients(players, "Player '" + player.getNickName() + "', your turn is over.", board);
			nextTurn();
		}
	}

	public void startTurns() {
		this.gameStatus = GameStatusConstants.PLAY; // we're ready to play!
		if (!currentPlayer.playerIsOffline()) {
			PubSub.notifyAllClients(players,
					"Player '" + currentPlayer.getNickName() + "', it's your turn. Perform your actions!", board);
			drawPoliticCard(currentPlayer);
			timers.submit(new TurnTimerThread(this, currentPlayer));
		} else
			nextTurn();
	}

	public void nextTurn() {
		if (playerTurnIterator.isLastPlayer(currentPlayer)) {
			if (GameStatusConstants.FINISH == gameStatus) {
				PubSub.notifyAllClients(this.players, "Turns are over!", board);
				notifyMatchWinner();
				return;
			}
			currentPlayer = playerTurnIterator.next();
			startMarketSellTime();
		} else {
			currentPlayer = playerTurnIterator.next();
			if (!currentPlayer.playerIsOffline()) {
				PubSub.notifyAllClients(players,
						"Player '" + currentPlayer.getNickName() + "', it's your turn. Perform your actions!", board);
				drawPoliticCard(currentPlayer);
				timers.submit(new TurnTimerThread(this, currentPlayer));
			} else
				nextTurn();
		}
	}

	private void startMarketSellTime() {
		gameStatus = GameStatusConstants.MARKET_SELL;
		PubSub.notifyAllClients(players, "Game Status changed to 'Market Sell Time'", board);
		sendMarketStatus();
		timers.submit(new MarketTimerThread(this, numberOfPlayers));
	}

	/**
	 * This method is invoked only and exclusively when the timer for buying in
	 * the market for a specified player is over. A new timer for the next
	 * player will be initialized
	 * 
	 * @param playerId
	 *            the current player in the marketBuyTurn array list
	 */
	public void nextMarketBuyTurn(Player player) {
		if (playerMarketTurn == player) {
			if (!randomPlayerIterator.hasNext()) {
				playerMarketTurn = null;
				return;
			}
			playerMarketTurn = randomPlayerIterator.next();
			PubSub.notifyAllClients(players,
					"Player '" + player.getNickName() + "' your turn for buying in the Market is over!", board);
			PubSub.notifyAllClients(players,
					"Player '" + playerMarketTurn.getNickName() + "' now it's your turn for buying in the Market!",
					board);
			timers.submit(new MarketBuyTurnTimer(playerMarketTurn, this));
		}
	}

	public void startMarketBuyTime() {
		this.gameStatus = GameStatusConstants.MARKET_BUY;
		PubSub.notifyAllClients(players, "Game Status changed to 'Market Buy Time'", board);
		PubSub.sendMarketStatus(players, market);
		randomPlayerIterator = new RandomPlayerIterator(players);
		String message = "In order to buy items from the Market, players must respect a random order\n";
		playerMarketTurn = randomPlayerIterator.next();
		message += "Player '" + playerMarketTurn.getNickName() + "' it's your turn! Be fast, your time is limited!\n";
		PubSub.notifyAllClients(players, message, board);
		timers.submit(new MarketBuyTurnTimer(playerMarketTurn, this));
	}

	public void buyEvent(MarketEvent marketEvent, int playerId) {
		if (gameStatus != GameStatusConstants.MARKET_BUY) {
			sendErrorToClient("Game status isn't 'Market Buy Time'", playerId);
			return;
		} else if (players.get(playerId) == playerMarketTurn) {
			MarketEventBuy event = (MarketEventBuy) marketEvent;
			try {
				market.buyItemOnSale(playerMarketTurn, event.getItemId());
				PubSub.notifyAllClients(
						players, "Player '" + players.get(playerId).getNickName()
								+ "' has just bought the item with ID " + event.getItemId() + " from the Market!",
						board);
				updateClient(playerId);
				nextMarketBuyTurn(playerMarketTurn);
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
			sendErrorToClient("Game status isn't 'Market Sell Time'", playerId);
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
			Tile permitTile;
			try {
				permitTile = player.sellPermitTile(permitTileId);
				item = factory.createPermitTileOnSale(permitTile, player, event.getPrice());
				market.putItemOnSale(item);
				PubSub.notifyAllClients(players, "Player '" + player.getNickName()
						+ "' has just put a new Item on sale in the Market!\nInfo:\n" + item.toString(), board);
			} catch (TileNotFoundException e) {
				sendErrorToClient("Ops! A PermitTile with the specified ID was not found!", playerId);
				return;
			}
			break;
		case "POLITICCARD":
			String politicCardColor = event.getPoliticCardColor();
			try {
				player.checkIfYouOwnThisCard(politicCardColor, player.getPoliticCards());
				item = factory.createPoliticCardOnSale(player.sellPoliticCard(politicCardColor), player,
						event.getPrice());
				market.putItemOnSale(item);
				PubSub.notifyAllClients(players, "Player '" + player.getNickName()
						+ "' has just put a new Item on sale in the Market!\nInfo:\n" + item.toString(), board);
			} catch (CardNotFoundException e) {
				sendErrorToClient(e.showError(), playerId);
			}
			break;
		case "ASSISTANT":
			if (player.getNumberOfAssistants() == 0) {
				sendErrorToClient("You haven't got Assistants in your pool!", playerId);
				return;
			}
			item = factory.createAssistantOnSale(player, event.getPrice());
			market.putItemOnSale(item);
			PubSub.notifyAllClients(players, "Player '" + player.getNickName()
					+ "' has just put a new Item on sale in the Market!\nInfo:\n" + item.toString(), board);
			break;
		default:
		}
	}

	public void sendMarketStatus() {
		PubSub.notifyAllClients(players, market.toString(), board);
		PubSub.sendMarketStatus(players, market);
	}

	public void sendErrorToClient(String error, int playerId) {
		String message = "[SERVER] Error: " + error;
		Player player = players.get(playerId);
		try {
			if (!player.playerIsOffline())
				player.getConnector().sendToClient(new Packet(message));
		} catch (RemoteException e) {
			player.setPlayerOffline();
			PubSub.notifyAllClientsExceptOne(player.getId(), players, "Client with nickname '" + player.getNickName()
			+ "' and ID " + player.getId() + " disconnected!");
			ServerOutputPrinter.printLine("[SERVER] Client with nickname '" + this.players.get(playerId).getNickName()
					+ "' and ID " + playerId + " disconnected!");

		}
	}

	public void updateClient(int playerId) {
		Player player = players.get(playerId);
		try {
			if (!player.playerIsOffline()) {
				player.getConnector().sendToClient(new Packet(new UpdateState(player)));
			}
		} catch (RemoteException e) {
			player.setPlayerOffline();
			PubSub.notifyAllClientsExceptOne(player.getId(), players, "Client with nickname '" + player.getNickName()
			+ "' and ID " + player.getId() + " disconnected!");
			ServerOutputPrinter.printLine("[SERVER] Client with nickname '" + this.players.get(playerId).getNickName()
					+ "' and ID " + playerId + " disconnected!");

		}
	}
	
	public synchronized void sendListOfPlayers() {
		for(Player player : players) {
			try {
				if (!player.playerIsOffline()) {
					List<Player> list = new ArrayList<>();
					list.addAll(players);
					player.getConnector().sendToClient(new Packet(new UpdateState(list)));
				}
			} catch (RemoteException e) {
				player.setPlayerOffline();
				PubSub.notifyAllClientsExceptOne(player.getId(), players, "Client with nickname '" + player.getNickName()
				+ "' and ID " + player.getId() + " disconnected!");
				ServerOutputPrinter.printLine("[SERVER] Client with nickname '" + player.getNickName()
						+ "' and ID " + player.getId() + " disconnected!");

			}
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
			PubSub.notifyAllClientsExceptOne(player.getId(), players, "Client with nickname '" + player.getNickName()
			+ "' and ID " + player.getId() + " disconnected!");
			ServerOutputPrinter.printLine("[SERVER] Client with nickname '" + this.players.get(playerId).getNickName()
					+ "' and ID " + playerId + " disconnected!");

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

	public synchronized void setPlayerNickName(int playerId, String nickName) {
		this.players.get(playerId).setPlayerNickName(nickName);
		
	}

	public void chat(int playerId, String messageString) {
		PubSub.chatMessage(playerId, players, messageString);
	}

	public void setPlayerOffline(int playerId) {
		Player player = this.players.get(playerId);
		player.setPlayerOffline();
	}

	public void messageFromClient(String messageString, int playerId) {
	}
}