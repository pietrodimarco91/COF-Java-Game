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
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Board;
import model.City;
import model.ItemOnSale;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
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
	private GridPane councillors;

	@FXML
	private GridPane balcony;

	@FXML
	private GridPane indicatorPane;

	@FXML
	private GridPane kingCouncil;

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
		painter = new Painter(stackPane, grid1, grid2, grid3, linesPane, citiesListener);
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
			chat.appendText(packet.getMessageString() + "\n");
			break;
		}
	}

	@Override
	public void editConnection(String choice) {
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
			serverOutput.appendText(update.getMessage() + "\n");
			break;
		case "MARKET":
			setItemsOnSale(update);
			break;
		}
	}

	private void repaintPlayerStatus(Player player) {
		painter.repaintPlayerStatus(player, indicatorPane);

	}

	public void setStage(Stage stage) {
		this.stage = stage;
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
	public void repaintBoard() {
		painter.repaint(board.getRegions());
		painter.repaintCouncils(board.getRegions(), board.getKingCouncil(), councillors, kingCouncil);
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
		URL resource = null;
		FXMLLoader loader = new FXMLLoader();
		String pathTo = "PerformActionDialog.fxml";
		try {
			resource = new File("src/main/java/client/view/gui/configurator/" + pathTo).toURI().toURL();
			loader.setLocation(resource);
			AnchorPane page = (AnchorPane) loader.load();
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
			dialogStage.showAndWait();
		} catch (MalformedURLException e) {
			serverOutput.appendText(e.getMessage());
		} catch (IOException e) {
			serverOutput.appendText(e.getMessage());
		}
	}

	@FXML
	public void handlePlayStatus() {
		try {
			connector.sendToServer(new Packet("FINISHMAPCONFIG"));
		} catch (RemoteException e) {
			serverOutput.appendText(e.getMessage());
		}
	}

	@FXML
	public void handleChatMessage() {
		// must show the chat message input field
	}

	public void showPlayerCards(ActionEvent event) {
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
			dialogStage.show();
		} catch (MalformedURLException e) {
			serverOutput.appendText(e.getMessage());
		} catch (IOException e) {
			serverOutput.appendText(e.getMessage());
		}
	}
}
