package client.view.gui;

import client.controller.ClientGUIController;
import client.controller.ClientSideConnector;
import client.controller.SocketInputOutputThread;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
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

	@FXML
	public void initialize() {
		ClientSideConnector clientSideConnector = super.getClientSideConnector();
		SocketInputOutputThread socketThread = super.getSocketThread();
		//clientSideConnector.setGUIConsole(serverOutput);
		//socketThread.setGUIConsole(serverOutput);
	}

	public void setStage(Stage stage) {
		waitingRoomStage = stage;
	}

}
