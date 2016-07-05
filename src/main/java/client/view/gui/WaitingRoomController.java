package client.view.gui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import client.controller.ClientGUIController;
import client.controller.ClientSideConnector;
import client.controller.SocketInputOutputThread;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 * Created by pietro on 05/07/16.
 */
public class WaitingRoomController extends ClientGUIController {

	@FXML
	private Button createConfig;

	@FXML
	private Button requestConfig;

	@FXML
	private TextField chatMessage;

	@FXML
	private TextArea chat;

	@FXML
	private Button sendMessage;
	
	@FXML
	private Button mute;

	@FXML
	private TextArea serverOutput;

	@FXML
	private VBox playersInGame;
	@FXML
	private Label title ;

	private Stage waitingRoomStage;
	
	private String nickName;
	
	private boolean muteCheck=false;
	
	private MediaPlayer mediaPlayer;
	
	@FXML
	public void initialize() {
		URL resource = null;
		String pathTo = "audio/surroundMusic.mp3";
		try {
			resource = new File("src/main/java/client/view/gui/" + pathTo).toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		playSound(resource.toString());
		ClientSideConnector clientSideConnector = super.getClientSideConnector();
		SocketInputOutputThread socketThread = super.getSocketThread();
		//clientSideConnector.setGUIConsole(serverOutput);
		//socketThread.setGUIConsole(serverOutput);
	}
	
	@FXML
	public void mute(ActionEvent event){
		if(!muteCheck){
		mute.getStyleClass().remove("audioButtonNotMute");
		mute.getStyleClass().add("audioButtonMute");
		muteCheck=true;
		muteSound(muteCheck);
		}
		else if (muteCheck){
		mute.getStyleClass().remove("audioButtonMute");
		mute.getStyleClass().add("audioButtonNotMute");
		muteCheck=false;
		muteSound(muteCheck);
		}
		
	}

	public void setStage(Stage stage) {
		waitingRoomStage = stage;
	}
	
	public void setNickName(String nickName){
		this.nickName=nickName;
		title.setText("Welcolme, "+nickName+" you are waiting for other players...");
	}
	
	private void playSound(String soundPath) {
		final Media media = new Media(soundPath);
		this.mediaPlayer = new MediaPlayer(media);
		mediaPlayer.play();
	}

	private void muteSound(boolean mute){
		this.mediaPlayer.setMute(mute);
	}
}
