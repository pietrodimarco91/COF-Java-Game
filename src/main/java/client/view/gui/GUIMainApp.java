package client.view.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUIMainApp extends Application {

	@Override
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(GUIMainApp.class.getResource("Login.fxml"));
		Parent root = loader.load();
		// Parent root = FXMLLoader.load( getClass().getResource("Login.fxml"));
		stage.setTitle("Login Area");
		stage.setScene(new Scene(root, 600, 500));
		stage.show();

		LoginController controller = loader.getController();
		controller.setStage(stage);
	}
}