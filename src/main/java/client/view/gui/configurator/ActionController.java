package client.view.gui.configurator;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.ArrayList;

import client.actions.AdditionalMainAction;
import client.actions.BuyPermitTileAction;
import client.actions.ElectCouncillorAction;
import client.actions.EngageAssistantAction;
import client.actions.KingBuildEmporiumAction;
import client.actions.SendAssistantAction;
import client.actions.SimpleBuildEmporiumAction;
import client.actions.SwitchPermitTilesAction;
import client.controller.ClientGUIController;
import controller.Packet;
import controller.Player;
import controller.ServerSideConnectorInt;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ActionController extends ClientGUIController {

	@FXML
	private RadioButton electCouncillorHills;

	@FXML
	private RadioButton electCouncillorCoast;

	@FXML
	private RadioButton electCouncillorMountains;

	@FXML
	private RadioButton buyPermitTileHills;

	@FXML
	private RadioButton buyPermitTileCoast;

	@FXML
	private RadioButton buyPermitTileMountains;

	@FXML
	private RadioButton switchPermitTileHills;

	@FXML
	private RadioButton switchPermitTileCoast;

	@FXML
	private RadioButton switchPermitTileMountains;

	@FXML
	private RadioButton sendAssistantHills;

	@FXML
	private RadioButton sendAssistantCoast;

	@FXML
	private RadioButton sendAssistantMountains;

	@FXML
	private RadioButton buyPermitTileSlot1;

	@FXML
	private RadioButton buyPermitTileSlot2;

	@FXML
	private TextField mainCouncillorColor;
	
	@FXML
	private TextField quickCouncillorColor;

	@FXML
	private TextField permitTile;

	@FXML
	private TextField citySimpleBuildEmporium;
	
	@FXML
	private TextField cityKingBuildEmporium;

	private String regionName;

	private Stage stage;

	private ServerSideConnectorInt connector;

	private String councillorColor;

	private ArrayList<String> politicCardColors;

	private int slot;

	private Player player;

	private Stage dialogStage;

	@FXML
	public void handleSelectRegionElectCouncillor() {
		ToggleGroup group = new ToggleGroup();
		electCouncillorHills.setToggleGroup(group);
		electCouncillorCoast.setToggleGroup(group);
		electCouncillorMountains.setToggleGroup(group);
		if (electCouncillorHills.isSelected())
			this.regionName = "HILLS";
		else if (electCouncillorCoast.isSelected())
			this.regionName = "COAST";
		else if (electCouncillorMountains.isSelected())
			this.regionName = "MOUNTAINS";
	}

	@FXML
	public void handleSelectRegionBuyPermitTile() {
		ToggleGroup group = new ToggleGroup();
		buyPermitTileHills.setToggleGroup(group);
		buyPermitTileCoast.setToggleGroup(group);
		buyPermitTileMountains.setToggleGroup(group);
		if (buyPermitTileHills.isSelected())
			this.regionName = "HILLS";
		else if (buyPermitTileCoast.isSelected())
			this.regionName = "COAST";
		else if (buyPermitTileMountains.isSelected())
			this.regionName = "MOUNTAINS";
	}
	
	@FXML
	public void handleSelectRegionSwitchPermitTile() {
		ToggleGroup group = new ToggleGroup();
		switchPermitTileHills.setToggleGroup(group);
		switchPermitTileCoast.setToggleGroup(group);
		switchPermitTileMountains.setToggleGroup(group);
		if (switchPermitTileHills.isSelected())
			this.regionName = "HILLS";
		else if (switchPermitTileCoast.isSelected())
			this.regionName = "COAST";
		else if (switchPermitTileMountains.isSelected())
			this.regionName = "MOUNTAINS";
	}
	
	@FXML
	public void handleSelectRegionSendAssistant() {
		ToggleGroup group = new ToggleGroup();
		sendAssistantCoast.setToggleGroup(group);
		sendAssistantHills.setToggleGroup(group);
		sendAssistantMountains.setToggleGroup(group);
		if (sendAssistantHills.isSelected())
			this.regionName = "HILLS";
		else if (sendAssistantHills.isSelected())
			this.regionName = "COAST";
		else if (sendAssistantMountains.isSelected())
			this.regionName = "MOUNTAINS";
	}

	@FXML
	public void handleSelectPermitTileSlot() {
		ToggleGroup group = new ToggleGroup();
		buyPermitTileSlot1.setToggleGroup(group);
		buyPermitTileSlot2.setToggleGroup(group);
		if (buyPermitTileSlot1.isSelected())
			this.slot = 1;
		else if (buyPermitTileSlot2.isSelected())
			this.slot = 2;
	}

	@FXML
	public void handleSelectCouncillorColor(ActionEvent event) {
		Button button = (Button) event.getSource();
		this.mainCouncillorColor.setText(button.getText());
		dialogStage.close();
	}

	@FXML
	public void handleChooseCouncillorColor() {
		URL resource = null;
		FXMLLoader loader = new FXMLLoader();
		String pathTo = "ChooseCouncillorColor.fxml";
		try {
			resource = new File("src/main/java/client/view/gui/configurator/" + pathTo).toURI().toURL();
			loader.setLocation(resource);
			loader.setController(this);
			AnchorPane page = (AnchorPane) loader.load();
			dialogStage = new Stage();
			dialogStage.setTitle("Choose Councillor Color");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(stage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			dialogStage.showAndWait();
		} catch (MalformedURLException e) {
			showAlert(e.getMessage());
		} catch (IOException e) {
			showAlert(e.getMessage());
		}
	}

	@FXML
	public void handleChoosePoliticCards() {
		URL resource = null;
		FXMLLoader loader = new FXMLLoader();
		String pathTo = "ChoosePoliticCards.fxml";
		try {
			resource = new File("src/main/java/client/view/gui/configurator/" + pathTo).toURI().toURL();
			loader.setLocation(resource);
			AnchorPane page = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Choose Politic Cards");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(stage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			dialogStage.showAndWait();
		} catch (MalformedURLException e) {
			showAlert(e.getMessage());
		} catch (IOException e) {
			showAlert(e.getMessage());
		}
	}

	@FXML
	public void handlePerformBuyPermitTileAction() {
		if(regionName==null || this.politicCardColors.size()==0 || this.player==null) {
			String error="";
			if(regionName==null)
				error+="Please specity a region!\n";
			if(politicCardColors.size()==0)
				error+="Please choose at least one Politic Card!\n";
			if(this.player==null)
				error+="Match isn't started yet!\n";
			showAlert(error);
			return;
		}
		try {
			this.connector.sendToServer(
					new Packet(new BuyPermitTileAction("main", this.regionName, this.politicCardColors, this.slot)));
			stage.close();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handlePerformBuildEmporiumKingAction() {
		if(this.cityKingBuildEmporium.getText()=="" || this.politicCardColors.size()==0) {
			String error="";
			if(this.cityKingBuildEmporium.getText()=="")
				error+="Please specity a city!\n";
			if(politicCardColors.size()==0)
				error+="Please choose at least one Politic Card!\n";
			showAlert(error);
			return;
		}
		try {
			this.connector.sendToServer(new Packet(
					new KingBuildEmporiumAction("main", this.cityKingBuildEmporium.getText(), this.politicCardColors)));
			stage.close();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handlePerformSimpleBuildEmporiumAction() {
		if(this.permitTile.getText()=="" || this.citySimpleBuildEmporium.getText()=="") {
			String error="";
			if(this.permitTile.getText()=="")
				error+="Please choose a Permit Tile!\n";
			if(this.citySimpleBuildEmporium.getText()=="")
				error+="Please choose the city where you want to build your Emporium!\n";
			showAlert(error);
			return;
		}
		try {
			this.connector.sendToServer(
					new Packet(new SimpleBuildEmporiumAction("main", Integer.parseInt(this.permitTile.getText()), this.citySimpleBuildEmporium.getText())));
			stage.close();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void handlePerformElectCouncillorAction() {
		if(this.regionName=="" || this.councillorColor=="") {
			String error="";
			if(this.regionName=="")
				error+="Please choose a Region!\n";
			if(this.councillorColor=="")
				error+="Please choose the color of the councillor to elect!\n";
			showAlert(error);
			return;
		}
		try {
			this.connector
					.sendToServer(new Packet(new ElectCouncillorAction("main", this.regionName, this.councillorColor)));
			stage.close();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleEngageAssistantAction() {
		try {
			this.connector.sendToServer(new Packet(new EngageAssistantAction("quick")));
			stage.close();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleSwitchPermitTilesAction() {
		if(this.regionName=="") {
			String error="Please choose a Region!\n";
			showAlert(error);
			return;
		}
		try {
			this.connector.sendToServer(new Packet(new SwitchPermitTilesAction("quick", this.regionName)));
			stage.close();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void handleSendAssistantAction() {
		if(this.regionName=="" || this.councillorColor=="") {
			String error="";
			if(this.regionName=="")
				error+="Please choose a Region!\n";
			if(this.councillorColor=="")
				error+="Please choose the color of the councillor to elect!\n";
			showAlert(error);
			return;
		}
		try {
			this.connector
					.sendToServer(new Packet(new SendAssistantAction("quick", this.regionName, this.councillorColor)));
			stage.close();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	public void handleAdditionalMainAction() {
		try {
			this.connector
					.sendToServer(new Packet(new AdditionalMainAction("quick")));
			stage.close();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void showAlert(String errorMessage) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.initOwner(stage);
		alert.setTitle("Ops...");
		alert.setHeaderText("Error!");
		alert.setContentText(errorMessage);
		alert.showAndWait();
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setConnector(ServerSideConnectorInt connector) {
		this.connector = connector;
	}
	
	public void setPlayer(Player player) {
		this.player=player;
	}
}
