package client.view.gui;

import javafx.scene.layout.Pane;
import model.City;

/**
 * Created by pietro on 07/07/16.
 */
public class MatchCityPane {

    private City city;

    private Pane connectedPane;

    public MatchCityPane(City city, Pane pane) {
        this.city=city;
        this.connectedPane=pane;
    }

    public Pane getPane(){
        return connectedPane;
    }

    public City getCity(){
        return city;
    }
}
