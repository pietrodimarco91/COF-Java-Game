package client.view.gui;

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

public class LoginController {

	private Stage stage;

	@FXML // fx:id="plat"
	private Button play;
	@FXML // fx:id="testo"
	private TextField nickName;

	@FXML
	void play(ActionEvent event) {
		final URL resource = getClass().getResource("buttonPressed.mp3");
        final Media media = new Media(resource.toString());
        final MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
		String playerName = nickName.getText();
		if (checkCorrectNickName(playerName))
			;
		else {
			// Show the error message.
			Alert alert = new Alert(AlertType.ERROR);
			alert.initOwner(stage);
			alert.setTitle("Ops...");
			alert.setHeaderText("Error!");
			alert.setContentText("Please use a nickname of at least 4 characters and without spaces!");
			alert.showAndWait();
			nickName.setText("");
		}
	}

	public boolean checkCorrectNickName(String nickName) {
		if (nickName.contains(" ") || nickName.length() < 4)
			return false;
		else
			return true;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

}