package controller.Client;

import client.actions.Action;
import client.view.cli.ClientOutputPrinter;
import controller.ServerSideConnectorInt;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class SocketInputOutputThread extends Thread implements ServerSideConnectorInt {

	private Scanner input;
	private Scanner inputStringFromServer;
	private ObjectOutputStream outputObjectToServer;
	private PrintWriter outputStringToServer;
	private String received;
	private boolean waitStart;
	private boolean youAreCreator;
	private boolean creatorHasBeenSet;


	public SocketInputOutputThread(Socket socket) {
		try {
			input = new Scanner(System.in);
			waitStart=false;
			creatorHasBeenSet=false;
			youAreCreator=false;
			inputStringFromServer = new Scanner(socket.getInputStream());
			outputObjectToServer = new ObjectOutputStream(socket.getOutputStream());
			outputStringToServer = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			received = inputStringFromServer.nextLine();
			switch (received){
				case "START":
					waitStart=true;
					break;
				case "*#*":
					outputStringToServer.println(input.nextLine());
					break;
				case "CREATOR":
					creatorHasBeenSet=true;
					youAreCreator=true;
					break;
				case "NOT CREATOR":
					creatorHasBeenSet=true;
					break;
				default:
					ClientOutputPrinter.printLine(received);

			}
		}
	}


	@Override
	public void writeToServer(String s) throws RemoteException {

	}

	@Override
	public int receiveIntFromServer() throws RemoteException {
		return 0;
	}

	@Override
	public void sendActionToServer(Action action) throws RemoteException {
		try {
			outputObjectToServer.writeObject(action);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void sendConfigurationToServer(ArrayList<Integer> config) throws RemoteException {
		try {
			outputObjectToServer.writeObject(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Override
	public void setTurn(boolean value) {
		//ARE NECESSARY ONLY FOR THE SERVeR
	}

	@Override
	public Action getAction()  {
		//ARE NECESSARY ONLY FOR THE SERVeR
		return null;
	}

	@Override
	public void waitStart() {
		while (!waitStart){
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void setMatchStarted() {
		//ARE NECESSARY ONLY FOR THE SERVeR
	}

	@Override
	public void setCreator(boolean b) {
		//ARE NECESSARY ONLY FOR THE SERVeR
	}

	@Override
	public ArrayList<Integer> getBoardConfiguration() {
		//ARE NECESSARY ONLY FOR THE SERVeR
		return null;
	}
}
