package client.view.gui.configurator;
import controller.Player;
import controller.ServerSideConnectorInt;
import javafx.stage.Stage;
import model.ItemOnSale;

import java.util.List;

/**
 * Created by pietro on 10/07/16.
 */
public class MarketController {

    private Stage stage;
    private ServerSideConnectorInt connector;
    private Player player;
    private List<ItemOnSale> market;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setConnector(ServerSideConnectorInt connector) {
        this.connector = connector;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setMarket(List<ItemOnSale> market) {
        this.market = market;
    }
}
