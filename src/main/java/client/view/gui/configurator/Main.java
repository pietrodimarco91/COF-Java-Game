package client.view.gui.configurator;

import client.view.gui.LoaderResources;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root=LoaderResources.load("configurator/sample.fxml");
        primaryStage.setTitle("provaFX");
        primaryStage.setScene(new Scene(root,500,700));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
