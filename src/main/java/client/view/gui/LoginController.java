package client.view.gui;

import client.controller.ClientGUIController;

import java.io.File;
import java.net.ConnectException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
		URL resource = null;
		String pathTo = "audio/buttonPressed.mp3";
		try {
			resource = new File("src/main/java/client/view/gui/" + pathTo).toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		playSound(resource.toString());

		nickname = inputNickName.getText();
		String errorMessage = "";
		if (checkCorrectNickName(nickname) && connectionType != 0) {
			connect();
			FXMLLoader loader=new FXMLLoader();
			pathTo="WaitingRoom.fxml";
			Parent parentConnectionStage;
			try {
				resource = new File("src/main/java/client/view/gui/"+pathTo).toURI().toURL();
				loader.setLocation(resource);
				parentConnectionStage = loader.load();
				Stage waitingRoomStage=welcomeStage;
				waitingRoomStage.setScene(new Scene(parentConnectionStage));
				waitingRoomStage.show();
				WaitingRoomController waitingRoomController=loader.getController();
				waitingRoomController.setStage(waitingRoomStage);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
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
	}

	@FXML
	void selectConnectionType(ActionEvent event) {
		ToggleGroup group = new ToggleGroup();
		socketCheckBox.setToggleGroup(group);
		rmiCheckBox.setToggleGroup(group);
		if (socketCheckBox.isSelected())
			connectionType = 2;
		else if (rmiCheckBox.isSelected())
			connectionType = 1;
	}

	public void setStage(Stage stage) {
		this.welcomeStage = stage;
	}

	private void playSound(String soundPath) {
		final Media media = new Media(soundPath);
		final MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
	}
	
	@Override
	public void connect() {
		if(connectionType==1) {
			this.startRMIConnection(nickname);
		} else if(connectionType==2) {
			this.startSocketConnection(nickname);
		}
	}
}