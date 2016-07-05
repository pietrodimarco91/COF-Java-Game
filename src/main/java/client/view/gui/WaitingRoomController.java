package client.view.gui;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import client.controller.ClientGUIController;
import client.controller.ClientSideConnector;
import client.controller.SocketInputOutputThread;
import controller.ClientSideConnectorInt;
import controller.Packet;
import controller.ServerSideConnectorInt;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
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
	private TextArea serverOutput;

	@FXML
	private VBox playersInGame;

	private Stage waitingRoomStage;

	private ServerSideConnectorInt connector;

	@FXML
	private TextField configChose;

	public void setStage(Stage stage) {
		waitingRoomStage = stage;
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
			dialogStage.initModality(Modality.NONE);
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
			serverOutput.appendText(packet.getUpdate().getHeader() + "\n");
			break;
		}
	}
}
