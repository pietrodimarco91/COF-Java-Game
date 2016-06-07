package controller.Client;

import client.actions.Action;
import client.view.cli.ClientOutputPrinter;
import controller.ServerSideRMIConnectorInt;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

public class SocketInputOutputThread extends Thread implements ServerSideRMIConnectorInt{

	private Scanner input;
	private Scanner inputStringFromServer;
	private ObjectOutputStream outputObjectToServer;
	private PrintWriter outputStringToServer;
	private String received;
	private boolean waitStart;

	public SocketInputOutputThread(Socket socket) {
		try {
			waitStart=false;
			inputStringFromServer = new Scanner(socket.getInputStream());
			outputObjectToServer = new ObjectOutputStream(socket.getOutputStream());
			outputStringToServer = new PrintWriter(socket.getOutputStream(), true);
			input = new Scanner(System.in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			received = inputStringFromServer.nextLine();
			if (received.equals("*#*")) {
					outputStringToServer.println(input.nextLine());
			} else
				ClientOutputPrinter.printLine(received);
			if(received.equals("START"))
				waitStart=true;
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

	//ARE NECESSARY ONLY FOR THE SERVeR

	@Override
	public void setTurn(boolean value) throws RemoteException {
	}

	@Override
	public Action getAction() throws RemoteException {
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

	}
}
