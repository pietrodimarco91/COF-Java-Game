package client.configurator;

import javafx.scene.layout.Pane;

/**
 * Created by pietro on 04/07/16.
 */
public class LinkStateFirstSelect implements LinkState {

    @Override
    public void select(CitiesListener citiesListener, Pane pane) {
        pane.getStyleClass().add("citySelected");
        citiesListener.setFirstLink(pane);
        citiesListener.setSecondLinkState();
    }
}
