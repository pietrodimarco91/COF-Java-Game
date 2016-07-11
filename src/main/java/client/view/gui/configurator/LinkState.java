package client.view.gui.configurator;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import model.City;

/**
 * Created by pietro on 04/07/16.
 */
public interface LinkState {
    void select(CitiesListener citiesListener, Pane pane, City city);

    void cityEntered(City city, BoardController boardController);

    void cityExited(City city, BoardController boardController);
}
