package client.view.gui;

import client.controller.ClientGUIController;
import java.net.URL;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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
	void play(ActionEvent event) {
		final URL resource = getClass().getResource("audio/buttonPressed.mp3");
        final Media media = new Media(resource.toString());
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
		String playerName = nickName.getText();
		if (checkCorrectNickName(playerName)) {
			//Stage connectionStage = 
		} else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(welcomeStage);
			alert.setTitle("Ops...");
			alert.setHeaderText("Error!");
			alert.setContentText("Please use a nickname of at least 4 characters and without spaces!");
			alert.showAndWait();
			nickName.setText("");
		}
	}

	public void setStage(Stage stage) {
		this.welcomeStage = stage;
	}

}