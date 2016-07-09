package client.view.gui.configurator;

import java.util.ArrayList;

import client.controller.ClientGUIController;
import controller.Player;
import controller.ServerSideConnectorInt;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.PoliticCard;

public class ChoosePoliticCardsController extends ClientGUIController{
	
	private Stage stage;
	private Player player;
	private ServerSideConnectorInt connector;
	private String css;
	private ArrayList<PoliticCard> politicCards;
	@FXML
	private GridPane politicCardsScrollPane;
}
