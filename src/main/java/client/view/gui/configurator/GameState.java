package client.view.gui.configurator;

import javafx.application.Platform;
import javafx.scene.layout.Pane;
import model.City;

/**
 * Created by pietro on 09/07/16.
 */
public class GameState implements LinkState {

    private final BoardController boardController;

    public GameState(BoardController boardController) {
        this.boardController=boardController;
    }

    @Override
    public void select(CitiesListener citiesListener, Pane pane, City city) {
        boardController.showCityInfo(city);
    }

    @Override
    public void cityEntered(City city, BoardController boardController) {
        Platform.runLater(()->{
            boardController.showCityInfo(city);
        });
    }

    @Override
    public void cityExited(City city, BoardController boardController) {
        boardController.showButtonPane();
    }
}
