package client.view.gui.configurator;

import java.util.ArrayList;

import client.controller.ClientGUIController;
import client.view.gui.LoaderResources;
import controller.Player;
import controller.ServerSideConnectorInt;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import model.Board;
import model.PoliticCard;

public class CardController extends ClientGUIController {
	private Player player;
	private Stage stage;
	private ServerSideConnectorInt connector;
	private Board board;
	private String css;
	private ArrayList<PoliticCard> politicCards;

	@FXML
	private GridPane politicCardsScrollPane;

	public void setScrollPane() {
		politicCards = this.player.getPoliticCards();
		css = LoaderResources.loadPath("/configurator/style.css");
		int countCards = 0;
		boolean stop = false;

		for (int i = 0; i < 10 && !stop; i++) {
			for (int j = 0; j < 4 && !stop; j++) {
				if (countCards < this.player.getPoliticCards().size()) {
					Pane pane = new Pane();
					pane.getStylesheets().add(css);
					switch (this.player.getPoliticCards().get(countCards).getColorCard()) {
					case "PINK":
						pane.getStyleClass().add("pinkCard");
						politicCardsScrollPane.add(pane, j, i);
						break;
					case "PURPLE":
						pane.getStyleClass().add("purpleCard");
						politicCardsScrollPane.add(pane, j, i);
						break;
					case "BLACK":
						pane.getStyleClass().add("blackCard");
						politicCardsScrollPane.add(pane, j, i);
						break;
					case "BLUE":
						pane.getStyleClass().add("blueCard");
						politicCardsScrollPane.add(pane, j, i);
						break;
					case "WHITE":
						pane.getStyleClass().add("whiteCard");
						politicCardsScrollPane.add(pane, j, i);
						break;
					case "ORANGE":
						pane.getStyleClass().add("orangeCard");
						politicCardsScrollPane.add(pane, j, i);
						break;
					case "MULTICOLOR":
						pane.getStyleClass().add("multicolorCard");
						politicCardsScrollPane.add(pane, j, i);
						break;
					}
					countCards++;
				}else
					stop=true;

			}
		}

	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setConnector(ServerSideConnectorInt connector) {
		this.connector = connector;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setBoard(Board board) {
		this.board = board;
	}
}
