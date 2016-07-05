package client.view.gui;

import client.controller.ClientGUIController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class LoginController extends ClientGUIController {

	private Stage welcomeStage;

	@FXML
	private Button play;
	@FXML
	private TextField nickName;

	@FXML
	void play(ActionEvent event) {
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