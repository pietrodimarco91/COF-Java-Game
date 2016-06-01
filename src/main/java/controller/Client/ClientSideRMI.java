package controller.Client;

import controller.ClientSideRMIInt;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * Created by pietro on 29/05/16.
 */
public class ClientSideRMI extends UnicastRemoteObject implements ClientSideRMIInt {

	protected ClientSideRMI() throws RemoteException {
	}

	@Override
	public void writeToClient(String s) throws RemoteException {
		System.out.println(s);
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
