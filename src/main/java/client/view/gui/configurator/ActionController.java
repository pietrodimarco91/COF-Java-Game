package client.view.gui.configurator;

import client.controller.ClientGUIController;
import controller.ServerSideConnectorInt;
import javafx.fxml.FXML;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ActionController extends ClientGUIController {
	
	private Stage stage;
	
	@FXML
	private RadioButton mainAction;

	@FXML
	private RadioButton quickAction;

	@FXML
	private RadioButton electCouncillor;
	
	@FXML
	private RadioButton buyPermitTile;
	
	@FXML
	private RadioButton buildEmporiumWithPermitTile;
	
	@FXML
	private RadioButton buildEmporiumWithKingsHelp;
	
	@FXML
	private RadioButton switchPermitTile;
	
	@FXML
	private RadioButton engageAssistant;
	
	@FXML
	private RadioButton performAdditionalMainAction;
	
	@FXML
	private RadioButton sendAssistantToElectCouncillor;

	public void setStage(Stage dialogStage) {
		// TODO Auto-generated method stub
		
	}

	public void setConnector(ServerSideConnectorInt connector) {
		// TODO Auto-generated method stub
		
	}
	

}
