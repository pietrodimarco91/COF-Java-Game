package client.configurator;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class MapConfigController{

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="reset"
    private Button reset; // Value injected by FXMLLoader

    @FXML // fx:id="grid"
    private GridPane grid1; // Value injected by FXMLLoader

    @FXML // fx:id="grid"
    private GridPane grid2;

    @FXML // fx:id="grid"
    private GridPane grid3;

    @FXML
    private Pane linesPane;

    @FXML
    private GridPane grid;

    String css;

    private Painter painter;

    private CitiesListener citiesListener;


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        css=this.getClass().getResource("style.css").toExternalForm();
        citiesListener=new CitiesListener(this);
        painter =new Painter(grid1,grid2,grid3,linesPane,citiesListener);
        grid.setOnMouseClicked(event -> {
            citiesListener.linkClicked(event,linesPane);
        });
        //aggiungere cities e collegamenti
        painter.repaint();
    }



    //Controllo se i collegamenti vanno benese si manda al server e ridisegno
    public void checkLink(Pane firstLink, Pane secondLink) {

        painter.createLine(firstLink,secondLink);
    }
    }
