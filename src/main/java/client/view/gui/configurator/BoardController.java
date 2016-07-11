package client.view.gui.configurator;

import client.controller.ClientGUIController;
import controller.Packet;
import controller.Player;
import controller.ServerSideConnectorInt;
import controller.UpdateState;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.*;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BoardController extends ClientGUIController {

	@FXML // ResourceBundle that was given to the FXMLLoader
	private ResourceBundle resources;

	@FXML // URL location of the FXML file that was given to the FXMLLoader
	private URL location;

	@FXML // fx:id="reset"
	private Button reset; // Value injected by FXMLLoader

	@FXML // fx:id="grid"
	private GridPane grid1; // Value injected by FXMLLoader

	@FXML // fx:id="grid"
	private GridPane grid2;

	@FXML // fx:id="grid"
	private GridPane grid3;

	@FXML
	private Pane linesPane;

	@FXML
	private GridPane grid;

	@FXML
	private StackPane stackPane;

	@FXML
	private TextArea serverOutput;

	@FXML
	private TextArea chat;

	@FXML
	private Button playerCards;

	@FXML
	private GridPane topIndicatorPane;

	@FXML
	private GridPane councillors;

	@FXML
	private GridPane balcony;

	@FXML
	private GridPane indicatorPane;

	@FXML
	private GridPane kingCouncil;

	@FXML
	private GridPane permitTileSlot;

	@FXML
	private FlowPane buttonsPane;

	private Painter painter;

	private CitiesListener citiesListener;
	private Stage stage;
	private Board board;
	private Player playerStatus;
	private char city1, city2;
	private ServerSideConnectorInt connector;
	private List<ItemOnSale> itemsOnSale;

	public void initialize() {
		grid.setPickOnBounds(false);
		balcony.setPickOnBounds(false);
		councillors.setPickOnBounds(false);
		citiesListener = new CitiesListener(this);
		painter = new Painter(stackPane, grid1, grid2, grid3, linesPane, citiesListener, this);
		showButtonPane();
	}

	@Override
	public void sendPacketToGUIController(Packet packet) {
		switch (packet.getHeader()) {
		case "MESSAGESTRING":
			serverOutput.appendText(packet.getMessageString() + "\n");

			break;
		case "UPDATE":
			handleUpdate(packet.getUpdate());
			break;
		case "CHAT":
			super.playSound("audio/messageIn.mp3");
			chat.appendText(packet.getMessageString() + "\n");
			break;
		}
	}

	@Override
	public void editConnection(String choice) {
		super.playSound("audio/linkCity.m4a");
		try {
			connector.sendToServer(new Packet(String.valueOf(city1), String.valueOf(city2), choice));
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method allows to invoke the necessary methods when receiving a
	 * packet from the client controllers
	 * 
	 * @param update
	 */
	public void handleUpdate(UpdateState update) {
		switch (update.getHeader()) {
		case "BOARD":
			setBoard(update);
			repaintBoard();
			break;
		case "PLAYER_UPDATE":
			setPlayerStatus(update);
			repaintPlayerStatus(update.getPlayer());
			break;
		case "MESSAGE":
			handleMessageFromServer(update.getMessage());
			serverOutput.appendText(update.getMessage() + "\n");
			break;
		case "MARKET":
			setItemsOnSale(update);
			break;
		}
	}

	private void handleMessageFromServer(String message) {
		if (message.contains("Error")||message.contains("disconnected")) {
			showErrorMessage(message);
		}
		if (message.contains("it's your turn. Perform your actions!") || message.contains("won")
				|| message.contains("points") || message.contains("bonus")) {
			showDialogMessage(message);
		}
	}

	private void showDialogMessage(String message) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Turn Informtation");
		alert.setHeaderText("It's your turn");
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void showErrorMessage(String message) {
		Alert alert = new Alert(AlertType.WARNING);
		alert.setTitle("ERROR");
		alert.setHeaderText("Negative response from SERVER");
		alert.setContentText(message);

		alert.showAndWait();

	}

	private void repaintPlayerStatus(Player player) {
		painter.repaintPlayerStatus(player, indicatorPane, topIndicatorPane);

	}

	public void setStage(Stage stage) {
		this.stage = stage;
		stage.setOnCloseRequest(event -> {
			super.disconnect();
		});
	}

	public void setBoard(UpdateState update) {
		this.board = update.getBoard();

	}

	public void setPlayerStatus(UpdateState update) {
		this.playerStatus = update.getPlayer();
	}

	public void setItemsOnSale(UpdateState update) {
		this.itemsOnSale = update.getItemsOnSale();
	}

	/**
	 * This method is invoked in order to repaint the GUI when receiving a
	 * packet containing the board
	 */
	@FXML
	public void repaintBoard() {
		painter.repaint(board.getRegions());
		painter.repaintCouncils(board.getRegions(), board.getKingCouncil(), councillors, kingCouncil);
		painter.repaintTile(permitTileSlot, this.board.getRegions());
	}

	/**
	 * This method is invoked to request a connection edit to the server
	 * 
	 * @param c1
	 *            the initial letter of the first city
	 * @param c2
	 *            the initial letter of the second city
	 * @param choice
	 *            this should be "ADD" or "REMOVE", depending on the choice of
	 *            the creator
	 */
	public void setCities(char c1, char c2, String choice) {
		this.city1 = c1;
		this.city2 = c2;

		editConnection(choice);
	}

	public void setConnector(ServerSideConnectorInt connector) {
		this.connector = connector;
	}

	/**
	 * This method requests the call of the setCities in order to remove a link,
	 * retrieving the city names from the corresponding SingleLink object, that
	 * associates a line to the source and destination city
	 * 
	 * @param line
	 *            the line that connects the two cities
	 */
	public void removeLink(Line line) {
		List<SingleLink> links = painter.getLinksBetweenCities();
		for (SingleLink link : links) {
			if (link.getLine() == line) {
				setCities(link.getCity1().getName().charAt(0), link.getCity2().getName().charAt(0), "REMOVE");
			}
		}
	}

	/**
	 * This method is invoked by the repaint() in order to add the existing
	 * connections between the cities
	 * 
	 * @param firstLink
	 *            the pane corresponding to the first city
	 * @param secondLink
	 *            the pane corresponding to the second city
	 * @param city1
	 *            the first City object
	 * @param city2
	 *            the second City object
	 */
	public void createLink(Pane firstLink, Pane secondLink, City city1, City city2) {

		Platform.runLater(() -> {
			painter.createLine(firstLink, secondLink, city1, city2);
		});
	}

	@Override
	@FXML
	public void performNewAction() {
		super.playSound("audio/buttonPressed.mp3");
		URL resource = null;
		FXMLLoader loader = new FXMLLoader();
		String pathTo = "PerformActionDialog.fxml";
		try {
			resource = new File("src/main/java/client/view/gui/configurator/" + pathTo).toURI().toURL();
			loader.setLocation(resource);
			AnchorPane page = loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Perform Action");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(stage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			ActionController actionController = loader.getController();
			actionController.setStage(dialogStage);
			actionController.setConnector(connector);
			actionController.setPlayer(playerStatus);
			actionController.setBoard(board);
			dialogStage.showAndWait();
		} catch (MalformedURLException e) {
			serverOutput.appendText(e.getMessage());
		} catch (IOException e) {
			serverOutput.appendText(e.getMessage());
		}
	}

	@FXML
	public void handlePlayStatus() {
		super.playSound("audio/buttonPressed.mp3");
		try {
			connector.sendToServer(new Packet("FINISHMAPCONFIG"));
			citiesListener.setGameState();
			repaintBoard();
		} catch (RemoteException e) {
			serverOutput.appendText(e.getMessage());
		}
	}

	@FXML
	public void chatMessage(ActionEvent event) {
		super.playSound("audio/buttonPressed.mp3");
		URL resource = null;
		FXMLLoader loader = new FXMLLoader();
		String pathTo = "chatMessage.fxml";
		try {
			resource = new File("src/main/java/client/view/gui/configurator/" + pathTo).toURI().toURL();

			loader.setLocation(resource);
			Parent page = loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Chat");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(stage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			ChatMessage chatMessage = loader.getController();
			chatMessage.setStage(dialogStage);
			chatMessage.setConnector(connector);
			dialogStage.showAndWait();
		} catch (MalformedURLException e) {
			serverOutput.appendText(e.getMessage());

		} catch (IOException e) {
			serverOutput.appendText(e.getMessage());

		}
	}

	@FXML
	public void handleMapConfigurationStatus() {
		citiesListener.setFirstLinkState();
		repaintBoard();
	}

	@FXML
	public void showPlayerCards(ActionEvent event) {
		super.playSound("audio/buttonPressed.mp3");
		URL resource = null;
		FXMLLoader loader = new FXMLLoader();
		String pathTo = "playerCards.fxml";
		try {
			resource = new File("src/main/java/client/view/gui/configurator/" + pathTo).toURI().toURL();

			loader.setLocation(resource);
			Parent page = loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Your Cards");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(stage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			CardController cardController = loader.getController();
			cardController.setStage(dialogStage);
			cardController.setConnector(connector);
			cardController.setPlayer(playerStatus);
			cardController.setScrollPane();
			cardController.setUnusedPermitTile();
			cardController.setUsedPermitTile();
			dialogStage.showAndWait();
		} catch (MalformedURLException e) {
			serverOutput.appendText(e.getMessage());

		} catch (IOException e) {
			serverOutput.appendText(e.getMessage());

		}
	}

	public void showCityInfo(City city) {
		Label name = new Label("City Name");
		Label bonus = new Label("Reward Token");
		Label emporiums = new Label("Emporiums");
		name.getStyleClass().add("mainLabel");
		bonus.getStyleClass().add("mainLabel");
		emporiums.getStyleClass().add("mainLabel");
		String cityName = city.getName();
		Tile rewardToken = city.winBonus();
		ArrayList<String> cityBonuses = rewardToken.getBonus();
		ArrayList<Emporium> cityEmporiums = city.getEmporiums();
		VBox nameBox = new VBox();
		VBox emporiumBox = new VBox();
		VBox bonusBox = new VBox();
		nameBox.getChildren().add(name);
		Label cityNameLabel = new Label(cityName);
		cityNameLabel.getStyleClass().add("simpleLabel");
		nameBox.getChildren().add(cityNameLabel);
		bonusBox.getChildren().add(bonus);
		for (String cityBonus : cityBonuses) {
			Label bonusLabel = new Label(cityBonus);
			bonusLabel.getStyleClass().add("simpleLabel");
			bonusBox.getChildren().add(bonusLabel);
		}
		emporiumBox.getChildren().add(emporiums);
		for (Emporium emporium : cityEmporiums) {
			Label emporiumLabel = new Label(emporium.getOwner().getNickName());
			emporiumLabel.getStyleClass().add("simpleLabel");
			emporiumBox.getChildren().add(emporiumLabel);
		}
		buttonsPane.getChildren().clear();
		buttonsPane.getChildren().add(nameBox);
		buttonsPane.getChildren().add(emporiumBox);
		buttonsPane.getChildren().add(bonusBox);
	}

	public void showButtonPane() {
		Platform.runLater(() -> {
			Button play = new Button("Start Playing");
			play.setOnAction(event -> {
				handlePlayStatus();
			});
			Button market = new Button("Market");
			market.setOnAction(event -> {
				showMarket();
			});
			Button mapConfiguration = new Button("Map Configuration");
			mapConfiguration.setOnAction(event -> {
				handleMapConfigurationStatus();
			});
			Button action = new Button("Perform Action");
			action.setOnAction(event -> {
				performNewAction();
			});
			Button cards = new Button("Your Cards");
			cards.setOnAction(event -> {
				showPlayerCards(event);
			});
			Button passTurn = new Button("Pass Turn");
			passTurn.setOnAction(event -> {
				passPlayerTurn();
			});
			this.buttonsPane.getChildren().clear();
			action.getStyleClass().add("menuButton");
			market.getStyleClass().add("menuButton");
			mapConfiguration.getStyleClass().add("menuButton");
			play.getStyleClass().add("menuButton");
			cards.getStyleClass().add("menuButton");
			passTurn.getStyleClass().add("menuButton");
			this.buttonsPane.getChildren().add(action);
			this.buttonsPane.getChildren().add(market);
			this.buttonsPane.getChildren().add(mapConfiguration);
			this.buttonsPane.getChildren().add(play);
			this.buttonsPane.getChildren().add(cards);
			this.buttonsPane.getChildren().add(passTurn);
		});
	}

	private void showMarket() {
		super.playSound("audio/buttonPressed.mp3");
		URL resource = null;
		FXMLLoader loader = new FXMLLoader();
		String pathTo = "market.fxml";
		try {
			resource = new File("src/main/java/client/view/gui/configurator/" + pathTo).toURI().toURL();
			loader.setLocation(resource);
			Parent page = loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Market");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(stage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			MarketController marketController = loader.getController();
			marketController.setStage(dialogStage);
			marketController.setConnector(connector);
			marketController.setPlayer(playerStatus);
			marketController.setMarket(this.itemsOnSale);
			dialogStage.showAndWait();
		} catch (MalformedURLException e) {
			serverOutput.appendText(e.getMessage());

		} catch (IOException e) {
			serverOutput.appendText(e.getMessage());
		}
	}

	private void passPlayerTurn() {
		try {
			connector.sendToServer(new Packet("PASSTURN"));
		} catch (RemoteException e) {
			serverOutput.appendText(e.getMessage());
		}
	}
}
