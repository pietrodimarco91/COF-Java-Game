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

    Pane firstLink;

    Pane secondLink;

    BoardController boardController;

    City firstCity,secondCity;

    public CitiesListener(BoardController boardController) {

        this.boardController = boardController;
        linkStateFirstSelect=new LinkStateFirstSelect();
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
        boardController.checkLink(firstLink,secondLink, firstCity, secondCity);
    }



    public void removeLink(Pane linePanes, Line line) {
    	boardController.removeLink(line);
       //linePanes.getChildren().remove(linePanes.getChildren().indexOf(line));
    }
}
