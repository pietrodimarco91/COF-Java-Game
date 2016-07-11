package client.view.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import client.view.cli.ClientOutputPrinter;

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
			ClientOutputPrinter.printLine(e.getMessage());
		} catch (IOException e) {
			ClientOutputPrinter.printLine(e.getMessage());
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
			ClientOutputPrinter.printLine(e.getMessage());
		} catch (IOException e) {
			ClientOutputPrinter.printLine(e.getMessage());
		}

		return loader;
	}

	public static String loadPath(String s) {
		URL resource = null;
		try {
			resource = new File("src/main/java/client/view/gui/" + s).toURI().toURL();
		} catch (MalformedURLException e) {
			ClientOutputPrinter.printLine(e.getMessage());
		}
		if (resource != null)
			return resource.toString();
		return null;
	}
}
