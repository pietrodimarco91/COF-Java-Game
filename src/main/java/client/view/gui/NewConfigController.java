package client.view.gui;

import java.rmi.RemoteException;

import controller.Packet;
import controller.ServerSideConnectorInt;
import filehandler.ConfigObject;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class NewConfigController {

	private Stage stage;

	@FXML
	private TextField numberOfPlayers;

	@FXML
	private TextField linksBetweenCities;

	@FXML
	private TextField rewardTokenBonusNumber;

	@FXML
	private TextField nobilityTrackBonusNumber;

	@FXML
	private TextField permitTileBonusNumber;

	private ServerSideConnectorInt connector;

	public void setStage(Stage dialogStage) {
		this.stage = dialogStage;
	}

	public void setConnector(ServerSideConnectorInt connector) {
		this.connector = connector;
	}

	@FXML
	public void handleOk() {
		try {
			connector.sendToServer(new Packet(new ConfigObject(Integer.parseInt(numberOfPlayers.getText()),
					Integer.parseInt(rewardTokenBonusNumber.getText()),
					Integer.parseInt(permitTileBonusNumber.getText()),
					Integer.parseInt(nobilityTrackBonusNumber.getText()),
					Integer.parseInt(linksBetweenCities.getText()))));
			stage.close();
		} catch (NumberFormatException e) {
			showAlert("Expected integers!");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void handleCancel() {
		stage.close();
	}

	public void showAlert(String message) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(stage);
		alert.setTitle("Ops...");
		alert.setHeaderText("Error!");
		alert.setContentText(message);
		alert.showAndWait();
		numberOfPlayers.setText("");
		rewardTokenBonusNumber.setText("");
		permitTileBonusNumber.setText("");
		nobilityTrackBonusNumber.setText("");
		linksBetweenCities.setText("");
	}

}
