package client.controller;

import java.rmi.RemoteException;

import client.view.gui.GUIMainApp;
import client.view.gui.LoaderResources;
import controller.Packet;
import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class ClientGUIController extends ClientController {
	
	private Media media;
	private MediaPlayer mediaPlayer;
	
	@Override
	public void boardConfiguration() {}

	/**
	 * This will be used only by the CLI Controller, as the GUI needs to
	 * implement only the boardConfiguration() and newConfiguration()
	 */
	@Override
	public void mapConfiguration() {}

	@Override
	public void performNewAction() {}

	/**
	 * This will be used only by the CLI Controller, as the GUI always receives
	 * the board updates
	 */
	@Override
	public void requestBoardStatus() {}

	@Override
	public void sellItemOnMarket() {}

	@Override
	public void buyItemOnMarket() {}

	/**
	 * This will be used only by the CLI Controller, as the GUI always receives
	 * the player updates when needed
	 */
	@Override
	public void requestPlayerStatus() {}

	@Override
	public void chat() {}

	/**
	 * This will be used only by the CLI Controller, as the GUI implements
	 * different methods and this one isn't needed
	 */
	@Override
	public void initialConfiguration() {}

	/**
	 * This will be used only by the CLI Controller, as the GUI implements
	 * different methods and this one isn't needed
	 */
	@Override
	public void play() {}

	@Override
	public void connect() throws RemoteException {}

	/**
	 * This will be used only by the CLI Controller, as the GUI doesn't need it
	 */
	@Override
	public void countDistance() {}

	@Override
	public void editConnection(String choice) {}

	@Override
	public void newConfiguration() {}

	@Override
	public void welcome(String[] args) {
		Application.launch(GUIMainApp.class, args);
	}

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

	public void sendPacketToGUIController(Packet packet) {}
}
