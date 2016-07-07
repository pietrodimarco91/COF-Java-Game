package client.view.gui.configurator;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * Created by pietro on 07/07/16.
 */
public class RegionRunLater {

    Pane pane;
    int colIndex;
    int rowIndex;
    private GridPane region;

    public RegionRunLater(Pane pane, int colIndex, int rowIndex, GridPane region) {
        this.pane=pane;
        this.colIndex=colIndex;
        this.rowIndex=rowIndex;
        this.region = region;
    }

    public Pane getPane() {
        return pane;
    }

    public int getColIndex() {
        return colIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public GridPane getRegion() {
        return region;
    }
}
