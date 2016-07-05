package client.view.gui;

import client.controller.ClientGUIController;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
	private TextField nickName;
	@FXML
	private RadioButton socketCheckBox;
	@FXML
	private RadioButton rmiCheckBox;

	private int connectionType = 0; // 1 socket and 2 RMI

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

		String playerName = nickName.getText();
		String errorMessage = "";
		if (checkCorrectNickName(playerName) && connectionType != 0) {
			// Stage connectionStage =
		} else {
			if (connectionType == 0) {
				errorMessage += "Please select a connection type!\n";
			}
			if (!checkCorrectNickName(playerName)) {
				errorMessage += "Please use a nickname of at least 4 characters and without spaces!";
			}
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(welcomeStage);
			alert.setTitle("Ops...");
			alert.setHeaderText("Error!");
			alert.setContentText(errorMessage);
			alert.showAndWait();
			nickName.setText("");
		}
	}

	@FXML
	void selectConnectionType(ActionEvent event) {
		URL resource = null;
		String pathTo = "audio/checkBoxClick.mp3";
		try {
			resource = new File("src/main/java/client/view/gui/" + pathTo).toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		ToggleGroup group = new ToggleGroup();
		socketCheckBox.setToggleGroup(group);
		rmiCheckBox.setToggleGroup(group);
		if (socketCheckBox.isSelected()){
			playSound(resource.toString());
			connectionType = 1;
		}
		else if (rmiCheckBox.isSelected()){
			playSound(resource.toString());
			
			connectionType = 2;
		}

	}

	public void setStage(Stage stage) {
		this.welcomeStage = stage;
	}

	private void playSound(String soundPath) {

		final Media media = new Media(soundPath);
		final MediaPlayer mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();

	}

}