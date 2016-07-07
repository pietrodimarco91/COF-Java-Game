package client.view.gui.configurator;

import javafx.scene.input.MouseEvent;
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

    MapConfigController mapConfigController;

    City firstCity,secondCity;

    public CitiesListener(MapConfigController mapConfigController) {

        this.mapConfigController=mapConfigController;
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
        System.out.println(city.getName());
        firstCity=city;
        this.firstLink = firstLink;
    }

    public void setSecondLink(Pane secondLink, City city) {
        System.out.println(city.getName());
        this.secondCity=city;
        this.secondLink = secondLink;
        firstLink.getStyleClass().remove("citySelected");
        this.secondLink.getStyleClass().remove("citySelected");
        mapConfigController.checkLink(firstLink,secondLink);
        mapConfigController.setCities(firstCity.getName().charAt(0),secondCity.getName().charAt(0),"ADD");
    }


    public void removeLink(Pane linePanes, Line line) {
       linePanes.getChildren().remove(linePanes.getChildren().indexOf(line));
    }
}
