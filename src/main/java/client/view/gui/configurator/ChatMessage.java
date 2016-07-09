package client.view.gui.configurator;

import java.rmi.RemoteException;

import client.controller.ClientGUIController;
import controller.Packet;
import controller.ServerSideConnectorInt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ChatMessage extends ClientGUIController{
	private Stage stage;
	private ServerSideConnectorInt connector;
	@FXML
	private TextField replyMessage;
	
	
	
	
	@FXML
	public void replyMessage(ActionEvent event){
		try {
			this.connector.sendToServer(new Packet(replyMessage.getText(), "***"));
			replyMessage.setText("");
		} catch (RemoteException e) {
		}
		
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setConnector(ServerSideConnectorInt connector) {
		this.connector = connector;
	}
	
	

}
