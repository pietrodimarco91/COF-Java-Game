package client.view.gui.configurator;

import java.rmi.RemoteException;

import client.controller.ClientGUIController;
import controller.Packet;
import controller.ServerSideConnectorInt;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ChatMessage extends ClientGUIController{
	private Stage stage;
	private ServerSideConnectorInt connector;
	@FXML
	private TextField replyMessage;
	
	
	
	
	@FXML
	public void replyMessage(){
		try {
			this.connector.sendToServer(new Packet(replyMessage.getText(), "***"));
			replyMessage.setText("");
			this.stage.close();
		} catch (RemoteException e) {
		}
		
	}
	@FXML
	public void handleEnterKey(KeyEvent event){
		if(event.getCode()==KeyCode.ENTER){
			replyMessage();
		}
	}
	
	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setConnector(ServerSideConnectorInt connector) {
		this.connector = connector;
	}
	
	

}
