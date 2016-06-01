package controller.Client;

import controller.ClientSideRMIConnectorInt;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import client.view.cli.ClientOutputPrinter;

/**
 * Created by pietro on 29/05/16.
 */
public class ClientSideRMIConnector extends UnicastRemoteObject implements ClientSideRMIConnectorInt {

	protected ClientSideRMIConnector() throws RemoteException {
		
	}

	@Override
	public void writeToClient(String s) throws RemoteException {
		ClientOutputPrinter.printLine(s);
	}

	@Override
	public int receiveIntFromClient() throws RemoteException {
		Scanner input = new Scanner(System.in);
		int choice = input.nextInt();
		return choice;
	}

	@Override
	public String receiveStringFromClient() throws RemoteException {
		String string = "";
		Scanner input = new Scanner(System.in);
		string = input.nextLine();
		return string;
	}
}
