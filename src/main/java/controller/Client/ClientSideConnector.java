package controller.Client;

import client.view.cli.ClientOutputPrinter;
import controller.ClientSideConnectorInt;
import controller.Packet;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * Created by pietro on 29/05/16.
 */
public class ClientSideConnector extends UnicastRemoteObject implements ClientSideConnectorInt {

	public ClientSideConnector() throws RemoteException {
		
	}

	@Override
	public void sendToClient(Packet packet) throws RemoteException {

	}
}
