package client.controller;

import java.rmi.RemoteException;

import client.view.gui.GUIMainApp;
import client.view.gui.LoaderResources;
import controller.Packet;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * This class represents the superclass for all the controllers used with the
 * GUI: it extends the abstract class ClientController as well as the
 * ClientCLIController, as it needs to override the implementation of the
 * methods used to perform any action. As there are more specific controllers
 * for the GUI that extend this class, some methods will not be implemented
 * here, as they will be overriden by more specific classes that extend
 * ClientGUIController.
 * 
 * @author Riccardo
 *
 */
public class ClientGUIController extends ClientController {

	private Media media;
	private MediaPlayer mediaPlayer;

	@Override
	public void boardConfiguration() {
	}

	@Override
	public void performNewAction() {
	}

	@Override
	public void sellItemOnMarket() {
	}

	@Override
	public void buyItemOnMarket() {
	}

	@Override
	public void chat() {
	}

	@Override
	public void connect() throws RemoteException {
	}

	@Override
	public void editConnection(String choice) {
	}

	@Override
	public void newConfiguration() {
	}

	/**
	 * This method launches the main JavaFX application thread.
	 */
	@Override
	public void welcome(String[] args) {
		Application.launch(GUIMainApp.class, args);
	}

	/**
	 * This method is used to play sounds during clicks or events.
	 * @param soundPath the path to the source file to be played
	 */
	public void playSound(String soundPath) {
		soundPath = LoaderResources.loadPath(soundPath);
		this.media = new Media(soundPath);
		this.mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
	}

	public void muteSound(boolean mute) {
		this.mediaPlayer.setMute(mute);
	}

	public void stopSound() {
		this.mediaPlayer.stop();
	}

	public void sendPacketToGUIController(Packet packet) {
	}

	/**
	 * This will be used only by the CLI Controller, as the GUI always receives
	 * the player updates when needed
	 */
	@Override
	public void requestPlayerStatus() {
	}

	/**
	 * This will be used only by the CLI Controller, as the GUI implements
	 * different methods and this one isn't needed
	 */
	@Override
	public void initialConfiguration() {
	}

	/**
	 * This will be used only by the CLI Controller, as the GUI implements
	 * different methods and this one isn't needed
	 */
	@Override
	public void play() {
	}

	/**
	 * This will be used only by the CLI Controller, as the GUI doesn't need it
	 */
	@Override
	public void countDistance() {
	}

	/**
	 * This will be used only by the CLI Controller, as the GUI needs to
	 * implement only the boardConfiguration() and newConfiguration()
	 */
	@Override
	public void mapConfiguration() {
	}

	/**
	 * This will be used only by the CLI Controller, as the GUI always receives
	 * the board updates
	 */
	@Override
	public void requestBoardStatus() {
	}
}
