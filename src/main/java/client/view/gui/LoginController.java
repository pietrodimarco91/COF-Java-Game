package client.view.gui;

import client.controller.ClientGUIController;
import client.controller.ClientSideConnector;
import client.controller.SocketInputOutputThread;
import client.view.cli.ClientOutputPrinter;
import controller.ServerSideConnectorInt;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class LoginController extends ClientGUIController {

	private Stage welcomeStage;

	@FXML
	private Button play;
	@FXML
	private TextField inputNickName;
	@FXML
	private RadioButton socketCheckBox;
	@FXML
	private RadioButton rmiCheckBox;

	private int connectionType = 0; // 1 socket and 2 RMI

	private String nickname;

	@FXML
	void play(ActionEvent event) {
		super.playSound("audio/buttonPressed.mp3");

		nickname = inputNickName.getText();
		if (checkCorrectNickName(nickname) && connectionType != 0) {
			connect();
			createWaitingRoomStage();
		} else {
			showErrorMessage();
		}
	}

	public void createWaitingRoomStage() {
		URL resource = null;
		FXMLLoader loader = new FXMLLoader();
		String pathTo = "WaitingRoom.fxml";
		Parent parentConnectionStage;
		try {
			resource = new File("src/main/java/client/view/gui/" + pathTo).toURI().toURL();
			loader.setLocation(resource);
			parentConnectionStage = loader.load();
			Stage waitingRoomStage = welcomeStage;
			waitingRoomStage.setScene(new Scene(parentConnectionStage));
			waitingRoomStage.setTitle("Match Room");
			waitingRoomStage.show();
			WaitingRoomController waitingRoomController = loader.getController();
			waitingRoomController.setStage(waitingRoomStage);
			waitingRoomController.setNickName(nickname);
			Platform.runLater(() -> {
				ClientSideConnector clientSideConnector = super.getClientSideConnector();
				SocketInputOutputThread socketThread = super.getSocketThread();
				ServerSideConnectorInt serverSideConnector = super.getServerConnector();
				if (clientSideConnector != null) {
					clientSideConnector.setGUIController(waitingRoomController);
					waitingRoomController.setConnector(serverSideConnector);
					waitingRoomController.setClientConnector(clientSideConnector);
				}
				if (socketThread != null) {
					socketThread.setGUIController(waitingRoomController);
					waitingRoomController.setConnector(serverSideConnector);
					waitingRoomController.setClientConnector(socketThread);
				}
			});
		} catch (MalformedURLException e) {
			ClientOutputPrinter.printLine(e.getMessage());
		} catch (IOException e) {
			ClientOutputPrinter.printLine(e.getMessage());
		}
	}

	public void showErrorMessage() {
		String errorMessage = "";
		if (connectionType == 0) {
			errorMessage += "Please select a connection type!\n";
		}
		if (!checkCorrectNickName(nickname)) {
			errorMessage += "Please use a nickname of at least 4 characters and without spaces!";
		}
		// Show the error message.
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(welcomeStage);
		alert.setTitle("Ops...");
		alert.setHeaderText("Error!");
		alert.setContentText(errorMessage);
		alert.showAndWait();
		inputNickName.setText("");
	}

	@FXML
	public void selectConnectionType(ActionEvent event) {
		ToggleGroup group = new ToggleGroup();
		socketCheckBox.setToggleGroup(group);
		rmiCheckBox.setToggleGroup(group);

		if (socketCheckBox.isSelected()) {
			super.playSound("audio/checkBoxClick.mp3");
			connectionType = 2;
		} else if (rmiCheckBox.isSelected()) {
			super.playSound("audio/checkBoxClick.mp3");
			connectionType = 1;
		}
	}

	public void setStage(Stage stage) {
		this.welcomeStage = stage;
	}


	@Override
	public void connect() {
		if (connectionType == 1) {
			this.startRMIConnection(nickname);
		} else if (connectionType == 2) {
			this.startSocketConnection(nickname);
		}
	}
}