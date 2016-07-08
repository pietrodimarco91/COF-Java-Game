package client.view.gui.configurator;


import client.controller.ClientGUIController;
import client.view.gui.LoaderResources;
import controller.Packet;
import controller.ServerSideConnectorInt;
import controller.UpdateState;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import model.Board;

import java.net.URL;
import java.rmi.RemoteException;
import java.util.ResourceBundle;

public class BoardController extends ClientGUIController {

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

    @FXML
    private StackPane stackPane;

    @FXML
    private TextArea text;

    @FXML
    private Button button;

    String css;

    private Painter painter;

    private CitiesListener citiesListener;
    private Stage stage;
    private Board board;
    private char city1,city2;
    private ServerSideConnectorInt connector;

    public void initialize() {
        grid.setPickOnBounds(false);
        css= LoaderResources.loadPath("configurator/style.css");
        citiesListener=new CitiesListener(this);
        painter =new Painter(stackPane,grid1,grid2,grid3,linesPane,citiesListener);
        button.setOnMouseClicked(event -> {
            handleTest();
        });
    }

    //Controllo se i collegamenti vanno benese si manda al server e ridisegno
    public void checkLink(Pane firstLink, Pane secondLink) {
        Platform.runLater(()->{
            painter.createLine(firstLink,secondLink);
        });
    }


    @Override
    public void sendPacketToGUIController(Packet packet) {
        switch (packet.getHeader()) {
            case "MESSAGESTRING":
                text.appendText(packet.getMessageString());
                break;
            case "UPDATE":
                handleUpdate(packet.getUpdate());
                break;
            case "CHAT":
                break;
        }
    }
    
    public void handleUpdate(UpdateState update) {
    	switch(update.getHeader()) {
    	case "BOARD":
    		setBoard(update);
            repaintCall();
    		break;
    	case "PLAYER_UPDATE":
    		//repaintPlayerStatus();
    		break;
    	}
    }

    @FXML
    public void handleTest() {
        try {
            connector.sendToServer(new Packet("S","T","ADD"));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void editConnection(String choice){
        try {
            connector.sendToServer(new Packet(String.valueOf(city1),String.valueOf(city2),choice));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setBoard(UpdateState update) {
        this.board = update.getBoard();

    }

    public void repaintCall(){
        painter.repaint(board.getRegions());
    }

    public void setCities(char c, char c1,String choice) {
        this.city1=c;
        this.city2=c1;
        //editConnection(choice);
    }

    public void setConnector(ServerSideConnectorInt connector) {
        this.connector = connector;
    }
}
