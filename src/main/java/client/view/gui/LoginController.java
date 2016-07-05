package client.view.gui;

import java.net.URL;

import javax.swing.JButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class LoginController {
	

    @FXML // fx:id="plat"
    private Button play;
    @FXML // fx:id="testo"
    private TextField nickName;
    @FXML
    void play( ActionEvent event ){
    	String playerName=nickName.getText();
    	

    	
    }
    

}