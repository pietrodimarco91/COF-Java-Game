package client.view.gui.configurator;

import java.util.ArrayList;
import java.util.List;

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
import model.City;
import model.PermitTile;
import model.PoliticCard;
import model.Tile;

public class CardController extends ClientGUIController {
	private Player player;
	private Stage stage;
	private ServerSideConnectorInt connector;
	private Board board;
	private String css;
	private ArrayList<PoliticCard> politicCards;
	private ArrayList<Tile> unusedPermitTile;

	@FXML
	private GridPane politicCardsScrollPane;
	@FXML
	private GridPane unusedPermitTileScrollPane;
	@FXML
	private GridPane usedPermitTileScrollPane;

	public void setScrollPane() {
		this.politicCards = this.player.getPoliticCards();
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
				} else
					stop = true;

			}
		}

	}

	public void setUnusedPermitTile() {
		this.unusedPermitTile = this.player.getUnusedPermitTile();
		css = LoaderResources.loadPath("/configurator/style.css");
		int countCards = 0;
		int idCard;
		List<City>cardCity;
		ArrayList<String>cardBonus;
		boolean stop = false;
		
		//da eliminare
		ArrayList<String> daCancellare=new ArrayList<String>();
		daCancellare.add("M");
		daCancellare.add("N");
		daCancellare.add("P");
		
		ArrayList<String> dabonus=new ArrayList<String>();
		dabonus.add("BONUSPRIM");
		dabonus.add("BONUSPRIM2");
		dabonus.add("BONUSPRIM3");
		

		for (int i = 0; i < 10 && !stop; i++) {
			for (int j = 0; j < 4 && !stop; j++) {
				
					/*PermitTile tempTile=(PermitTile)this.unusedPermitTile.get(countCards);
					idCard=tempTile.getId();
					cardCity=tempTile.getCities();
					cardBonus=tempTile.getBonus();*/
					Pane pane = new Pane();
					pane.getStylesheets().add(css);
					pane.getStyleClass().add("permitTile");
					Label id = new Label("ID: 3");
					id.getStylesheets().add(css);
					id.getStyleClass().add("id");
					unusedPermitTileScrollPane.add(pane, j, i);
					unusedPermitTileScrollPane.add(id, j, i);
					String prova="\n\n\nCity:";
					for(int k=0;k<daCancellare.size();k++){//da cancellare
						prova+=daCancellare.get(k)+",";
						
					}
					Label cityName = new Label(prova);
					cityName.getStylesheets().add(css);
					cityName.getStyleClass().add("cityPermitTile");
					unusedPermitTileScrollPane.add(cityName, j, i);
					
					String bonus="\n\n\n\nBonus:\n";
					for(int k=0;k<dabonus.size();k++){//da cancellare
						bonus+=dabonus.get(k)+"\n";
						
					}
					Label cityBonus =new Label(bonus);
					cityBonus.getStylesheets().add(css);
					cityBonus.getStyleClass().add("bonus");
					unusedPermitTileScrollPane.add(cityBonus, j, i);
					
					
					countCards++;

				
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
