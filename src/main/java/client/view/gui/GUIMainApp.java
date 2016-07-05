package client.view.gui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIMainApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		String pathTo="Login.fxml";
		URL resource=null;
		  try {
		   resource = new File("src/main/java/client/view/gui/"+pathTo).toURI().toURL();
		  } catch (MalformedURLException e) {
		   e.printStackTrace();
		  }
		loader.setLocation(resource);
		
		Parent root = loader.load();
		// Parent root = FXMLLoader.load( getClass().getResource("Login.fxml"));
		stage.setTitle("Login Area");
		stage.setScene(new Scene(root, 600, 500));
		stage.show();

		LoginController controller = loader.getController();
		controller.setStage(stage);
	}
}