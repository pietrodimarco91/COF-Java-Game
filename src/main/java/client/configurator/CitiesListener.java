package client.configurator;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

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

    public CitiesListener(MapConfigController mapConfigController) {
        this.mapConfigController=mapConfigController;
        linkStateFirstSelect=new LinkStateFirstSelect();
        linkStateSecondSelect=new LinkStateSecondSelect();
        linkState=linkStateFirstSelect;
    }

    public void cityClicked(Pane pane) {
        linkState.select(this,pane);
    }


    public void setSecondLinkState() {
        linkState=linkStateSecondSelect;
    }

    public void setFirstLinkState() {
        linkState=linkStateFirstSelect;
    }

    public void setFirstLink(Pane firstLink) {
        this.firstLink = firstLink;
    }

    public void setSecondLink(Pane secondLink) {
        this.secondLink = secondLink;
        firstLink.getStyleClass().remove("citySelected");
        this.secondLink.getStyleClass().remove("citySelected");
        mapConfigController.checkLink(firstLink,secondLink);
    }

    public void linkClicked(MouseEvent event, Pane linesPane) {
        //linesPane.getChildren().remove(linesPane.getChildren().indexOf(line));
    }

}
