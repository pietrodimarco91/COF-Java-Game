package client.view.gui;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.List;

import client.controller.ClientGUIController;
import client.view.gui.configurator.BoardController;
import controller.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * Created by pietro on 05/07/16.
 */
public class WaitingRoomController extends ClientGUIController {

	@FXML
	private Button createConfig;

	@FXML
	private Button requestConfig;

	@FXML
	private TextField chatMessage;

	@FXML
	private TextArea chat;

	@FXML
	private Button sendMessage;

	@FXML
	private Button mute;

	@FXML
	private TextArea serverOutput;

	@FXML
	private VBox playersInGame;

	@FXML
	private Label title;

	private Stage waitingRoomStage;

	private ServerSideConnectorInt connector;

	private ClientSideConnectorInt clientConnector;

	@FXML
	private TextField configChose;

	private String nickName;

	private boolean muteCheck = false;

	private MediaPlayer mediaPlayer;

	private String css;

	@FXML
	public void initialize() {
		super.playSound("audio/surroundMusic.mp3");
	}

	@FXML
	public void mute(ActionEvent event) {
		if (!muteCheck) {
			mute.getStyleClass().remove("audioButtonNotMute");
			mute.getStyleClass().add("audioButtonMute");
			muteCheck = true;
			super.muteSound(muteCheck);
		} else {
			mute.getStyleClass().remove("audioButtonMute");
			mute.getStyleClass().add("audioButtonNotMute");
			muteCheck = false;
			super.muteSound(muteCheck);
		}

	}

	public void setStage(Stage stage) {
		waitingRoomStage = stage;
		css = LoaderResources.loadPath("/configurator/style.css");
		stage.setOnCloseRequest(event->{
			super.disconnect();
		});
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
		title.setText("Welcome " + nickName + ", you are waiting for other players...");
	}

	public void setConnector(ServerSideConnectorInt connector) {
		this.connector = connector;
	}

	@FXML
	public void resetText() {
		configChose.setText("");
	}

	@FXML
	public void handleSelectConfiguration() {
		String string = this.configChose.getText();
		try {
			int configId = Integer.parseInt(string);
			connector.sendToServer(new Packet(new Integer(configId)));
		} catch (NumberFormatException e) {
			showAlert("Expected integer!");
		} catch (RemoteException e) {
			serverOutput.appendText(e.getMessage());
		}
	}

	@FXML
	public void newConfiguration() {
		URL resource = null;
		FXMLLoader loader = new FXMLLoader();
		String pathTo = "NewConfigDialog.fxml";
		try {
			resource = new File("src/main/java/client/view/gui/" + pathTo).toURI().toURL();
			loader.setLocation(resource);
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("New Configuration");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(waitingRoomStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			NewConfigController newConfigController = loader.getController();
			newConfigController.setStage(dialogStage);
			newConfigController.setConnector(connector);
			dialogStage.showAndWait();
		} catch (MalformedURLException e) {
			serverOutput.appendText(e.getMessage());
		} catch (IOException e) {
			serverOutput.appendText(e.getMessage());
		}
	}

	@FXML
	public void handleChatMessage() {
		try {
			connector.sendToServer(new Packet(chatMessage.getText(), "***"));
			chatMessage.setText("");
		} catch (RemoteException e) {
			serverOutput.appendText(e.getMessage());
		}
	}

	@FXML
	public void handleEnterPressed(KeyEvent keyEvent) {
		if (keyEvent.getCode() == KeyCode.ENTER)
			handleChatMessage();
	}

	public void showAlert(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(waitingRoomStage);
		alert.setTitle("Ops...");
		alert.setHeaderText("Error!");
		alert.setContentText(message);
		alert.showAndWait();
		configChose.setText("");
	}

	@FXML
	@Override
	public void boardConfiguration() {
		try {
			connector.sendToServer(new Packet("REQUESTCONFIG"));
		} catch (RemoteException e) {
			serverOutput.appendText(e.getMessage());
		}
	}

	@Override
	public void sendPacketToGUIController(Packet packet) {
		switch (packet.getHeader()) {
		case "MESSAGESTRING":
			serverOutput.appendText(packet.getMessageString() + "\n");
			break;
		case "UPDATE":
			handleUpdateState(packet.getUpdate());
			break;
		case "CHAT":
			chat.appendText(packet.getMessageString() + "\n");
			super.playSound("audio/messageIn.mp3");
			break;
		}
	}

	public void handleUpdateState(UpdateState update) {
		switch (update.getHeader()) {
		case "BOARD":

			URL resource = null;
			FXMLLoader loader = new FXMLLoader();
			Parent parentConnectionStage = null;
			try {
				resource = new File("src/main/java/client/view/gui/configurator/mapConfig.fxml").toURI().toURL();
				loader.setLocation(resource);
				parentConnectionStage = loader.load();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Stage confStage = this.waitingRoomStage;

			confStage.setTitle("Match Room");
			Scene scene = new Scene(parentConnectionStage);
			BoardController boardController = loader.getController();
			boardController.setStage(confStage);
			boardController.setBoard(update);
			super.stopSound();
			super.playSound("audio/soundtrackGaming.mp3");

			Platform.runLater(() -> {
				confStage.setScene(scene);
				confStage.show();
				boardController.setConnector(connector);
			});

			try {
				this.clientConnector.setGUIController(boardController);
			} catch (RemoteException e) {
				e.printStackTrace();
			}

			break;
		case "PLAYERS":
			refreshPlayersList(update);
			break;
		default:
		}
	}

	public void refreshPlayersList(UpdateState update) {
		Platform.runLater(() -> {
			Label titleLabel = (Label) playersInGame.getChildren().get(0);
			playersInGame.getChildren().clear();
			List<Player> players = update.getPlayers();
			Label playerNickname;
			playersInGame.getChildren().add(titleLabel);
			for (Player player : players) {
				playerNickname = new Label(player.getNickName());
				playerNickname.getStylesheets().add(css);
				playerNickname.getStyleClass().add("nicknameLabel");
				playersInGame.getChildren().add(playerNickname);
			}
		});
	}

	public void setClientConnector(ClientSideConnectorInt connector) {
		this.clientConnector = connector;
	}
}
