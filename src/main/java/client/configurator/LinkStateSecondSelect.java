package client.configurator;

import javafx.scene.layout.Pane;

/**
 * Created by pietro on 04/07/16.
 */
public class LinkStateSecondSelect implements LinkState {

    @Override
    public void select(CitiesListener citiesListener, Pane pane) {
        citiesListener.setSecondLink(pane);
        citiesListener.setFirstLinkState();
    }
}
