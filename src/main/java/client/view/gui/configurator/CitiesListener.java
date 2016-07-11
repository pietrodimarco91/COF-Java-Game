package client.view.gui.configurator;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import model.City;

/**
 * Created by pietro on 05/07/16.
 */
public class CitiesListener {

    LinkState linkState;

    LinkState linkStateFirstSelect;

    LinkState linkStateSecondSelect;

    LinkState gameState;

    Pane firstLink;

    Pane secondLink;

    BoardController boardController;

    City firstCity,secondCity;

    public CitiesListener(BoardController boardController) {

        this.boardController = boardController;
        linkStateFirstSelect=new LinkStateFirstSelect();
        gameState = new GameState(boardController);

        linkStateSecondSelect=new LinkStateSecondSelect();
        linkState=linkStateFirstSelect;

    }

    public void cityClicked(Pane pane, City city) {
        linkState.select(this,pane,city);
    }


    public void setSecondLinkState() {
        linkState=linkStateSecondSelect;
    }

    public void setFirstLinkState() {
        linkState=linkStateFirstSelect;
    }

    public void setFirstLink(Pane firstLink, City city) {
        firstCity=city;
        this.firstLink = firstLink;
    }

    public void setSecondLink(Pane secondLink, City city) {
        this.secondCity=city;
        this.secondLink = secondLink;
        firstLink.getStyleClass().remove("citySelected");
        this.secondLink.getStyleClass().remove("citySelected");
        boardController.setCities(firstCity.getName().charAt(0),secondCity.getName().charAt(0),"ADD");
    }
    
    public void setAutomaticSecondLink(Pane secondLink, City city) {
    	this.secondCity=city;
        this.secondLink = secondLink;
        firstLink.getStyleClass().remove("citySelected");
        this.secondLink.getStyleClass().remove("citySelected");
        boardController.createLink(firstLink,secondLink, firstCity, secondCity);
    }

    public void removeLink(Line line) {
    	boardController.removeLink(line);
    }

    public void setGameState() {
        this.linkState=gameState;
    }

    public void cityEntered(City city) {
        linkState.cityEntered(city,boardController);
    }

    public void cityExited(City city) {
        linkState.cityExited(city,boardController);
    }
}