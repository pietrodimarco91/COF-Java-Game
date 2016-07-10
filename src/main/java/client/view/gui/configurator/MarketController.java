package client.view.gui.configurator;
import client.view.gui.LoaderResources;
import controller.Player;
import controller.ServerSideConnectorInt;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pietro on 10/07/16.
 */
public class MarketController {

    @FXML
    private GridPane yourAssistant;

    @FXML
    private GridPane yourPolitic;

    @FXML
    private GridPane yourPermit;

    @FXML
    private GridPane marketAssistant;

    @FXML
    private GridPane marketPolitic;

    @FXML
    private GridPane marketPermit;


    private Stage stage;
    private String css;
    private ServerSideConnectorInt connector;
    private Player player;
    private List<ItemOnSale> market;

    public void setStage(Stage stage) {
        css = LoaderResources.loadPath("/configurator/style.css");
        this.stage = stage;
    }

    public void setConnector(ServerSideConnectorInt connector) {
        this.connector = connector;
    }

    public void setPlayer(Player player) {
        this.player = player;
        setAssistant(player.getNumberOfAssistants());
        setPoliticCard(player.getPoliticCards());
        setPermitTile(player.getUnusedPermitTile());
    }

    public void setMarket(List<ItemOnSale> market) {
        this.market = market;
    }



    /*****FILLING GRIDS*****/

    public void setAssistant(int assistant) {
        int count=assistant;

        for (int i = 0; i < 10 && count>0; i++) {
            for (int j = 0; j < 4 && count > 0; j++) {
                Pane pane = new Pane();
                pane.getStylesheets().add(css);
                pane.getStyleClass().add("assistant");
                yourAssistant.add(pane, j, i);
                pane.setOnMouseClicked(event -> {
                    assistantClicked(pane);
                });
                count--;
            }
        }
    }


    public void setPoliticCard(ArrayList<PoliticCard> politicCards) {
        int countCards = 0;
        boolean stop = false;

        for (int i = 0; i < 10 && !stop; i++) {
            for (int j = 0; j < 4 && !stop; j++) {
                if (countCards < politicCards.size()) {
                    Pane pane = new Pane();
                    pane.getStylesheets().add(css);
                    String color=politicCards.get(countCards).getColorCard();
                    switch (color) {
                        case "PINK":
                            pane.getStyleClass().add("pinkCard");
                            yourPolitic.add(pane, j, i);
                            break;
                        case "PURPLE":
                            pane.getStyleClass().add("purpleCard");
                            yourPolitic.add(pane, j, i);
                            break;
                        case "BLACK":
                            pane.getStyleClass().add("blackCard");
                            yourPolitic.add(pane, j, i);
                            break;
                        case "BLUE":
                            pane.getStyleClass().add("blueCard");
                            yourPolitic.add(pane, j, i);
                            break;
                        case "WHITE":
                            pane.getStyleClass().add("whiteCard");
                            yourPolitic.add(pane, j, i);
                            break;
                        case "ORANGE":
                            pane.getStyleClass().add("orangeCard");
                            yourPolitic.add(pane, j, i);
                            break;
                        case "MULTICOLOR":
                            pane.getStyleClass().add("multicolorCard");
                            yourPolitic.add(pane, j, i);
                            break;
                    }
                    pane.setOnMouseClicked(event -> {
                        politicClicked(pane,color);
                    });
                    countCards++;
                } else
                    stop = true;

            }
        }
    }



    public void setPermitTile(ArrayList<Tile> permitTile) {
        int countCards = 0;
        int idCard;
        List<City>cardCity;
        ArrayList<String>cardBonus;
        boolean stop = false;

        for (int i = 0; i < 10 && !stop; i++) {
            for (int j = 0; j < 4 && !stop; j++) {
                if(countCards<permitTile.size()){

                    PermitTile tempTile=(PermitTile)permitTile.get(countCards);
                    idCard=tempTile.getId();
                    cardCity=tempTile.getCities();
                    cardBonus=tempTile.getBonus();
                    Pane pane = new Pane();
                    pane.getStylesheets().add(css);
                    pane.getStyleClass().add("permitTile");
                    Label id = new Label("ID: "+idCard);
                    id.getStylesheets().add(css);
                    id.getStyleClass().add("id");
                    marketPermit.add(pane, j, i);
                    marketPermit.add(id, j, i);
                    String city="\n\n\nCity:";
                    for(int k=0;k<cardCity.size();k++){
                        city+=cardCity.get(k).getName().charAt(0)+",";
                    }
                    Label cityName = new Label(city);
                    cityName.getStylesheets().add(css);
                    cityName.getStyleClass().add("cityPermitTile");
                    marketPermit.add(cityName, j, i);

                    String bonus="\n\n\n\nBonus:\n";
                    for(int k=0;k<cardBonus.size();k++){
                        bonus+=cardBonus.get(k)+"\n";
                    }
                    Label cityBonus =new Label(bonus);
                    cityBonus.getStylesheets().add(css);
                    cityBonus.getStyleClass().add("bonus");
                    marketPermit.add(cityBonus, j, i);
                    String finalBonus = bonus;
                    pane.setOnMouseClicked(event -> {
                        permitTileClicked(pane, finalBonus);
                    });
                    countCards++;

                }else
                    stop=false;
            }
        }
    }




    /******EVENTS HANDLER******/

    private void assistantClicked(Pane pane) {
    }

    private void politicClicked(Pane pane, String color) {
    }

    private void permitTileClicked(Pane pane, String cardBonus) {
    }



}
