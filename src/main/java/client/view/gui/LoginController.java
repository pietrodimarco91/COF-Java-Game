package client.view.gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;

public class LoginController {

	private Stage stage;

	@FXML // fx:id="plat"
	private Button play;
	@FXML // fx:id="testo"
	private TextField nickName;

	@FXML
	void play(ActionEvent event) {
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