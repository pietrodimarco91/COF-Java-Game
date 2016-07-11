package client.view.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by pietro on 06/07/16.
 */
public final class LoaderResources {

	public static Parent load(String ResourceLocalPathFromGui) {
		FXMLLoader loader = new FXMLLoader();
		URL resourceURL;
		Parent returned = null;

		try {
			resourceURL = new File("src/main/java/client/view/gui/" + ResourceLocalPathFromGui).toURI().toURL();
			loader.setLocation(resourceURL);
			returned = loader.load();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returned;
	}

	public static FXMLLoader getLoader(String ResourceLocalPathFromGui) {
		FXMLLoader loader = new FXMLLoader();
		URL resourceURL;
		try {
			resourceURL = new File("src/main/java/client/view/gui/" + ResourceLocalPathFromGui).toURI().toURL();
			loader.setLocation(resourceURL);
			loader.load();
			return loader;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return loader;
	}

	public static String loadPath(String s) {
		URL resource = null;
		try {
			resource = new File("src/main/java/client/view/gui/" + s).toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		if (resource != null)
			return resource.toString();
		return null;
	}
}
