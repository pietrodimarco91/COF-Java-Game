package client.view.gui.configurator;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.City;

/**
 * Created by pietro on 04/07/16.
 */
public class LinkStateSecondSelect implements LinkState {

    @Override
    public void select(CitiesListener citiesListener, Pane pane, City city) {
        citiesListener.setSecondLink(pane,city);
        citiesListener.setFirstLinkState();
    }

    @Override
    public void cityEntered(City city, BoardController boardController) {

    }

    @Override
    public void cityExited(City city, BoardController boardController) {

    }
}
