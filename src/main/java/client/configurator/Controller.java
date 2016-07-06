package client.configurator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class Controller {

    @FXML
    private Button bottone;

    @FXML
    private TextField textField;



    @FXML
    public void checkLabel(ActionEvent actionEvent) throws IOException {
        Parent scene2Parent= FXMLLoader.load(getClass().getResource("mapConfig.fxml"));
        Scene scene2 =new Scene(scene2Parent);
        Stage app_stage=(Stage)((Node) actionEvent.getSource()).getScene().getWindow();

        app_stage.setScene(scene2);
        app_stage.show();
    }
}
