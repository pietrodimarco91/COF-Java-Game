package client.controller;

import java.rmi.RemoteException;

import client.view.gui.Login;
import javafx.application.Application;

public class ClientGUIController extends ClientController {

	@Override
	public void boardConfiguration() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mapConfiguration() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void performNewAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestBoardStatus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sellItemOnMarket() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void buyItemOnMarket() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void requestPlayerStatus() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void chat() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void initialConfiguration() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void play() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connect() throws RemoteException {
		// TODO Auto-generated method stub
	}

	@Override
	public void countDistance() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void editConnection(String choice) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void newConfiguration() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void welcome(String[] args) {
		Application.launch(Login.class, args);
	}
}
