package client.controller;

import client.view.cli.ClientOutputPrinter;
import controller.ClientSideConnectorInt;
import controller.Packet;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by pietro on 29/05/16.
 */
public class ClientSideConnector extends UnicastRemoteObject implements ClientSideConnectorInt {

	public ClientSideConnector() throws RemoteException {
		
	}

	@Override
	public void sendToClient(Packet packet) throws RemoteException {
		switch (packet.getHeader()) {
			case "MESSAGESTRING":
				ClientOutputPrinter.printLine(packet.getMessageString());
				break;
			case "UPDATE":
				ClientOutputPrinter.printLine("*** GUI UPDATE RECEIVED: "+ packet.getUpdate().getHeader()+" ***");
			default:
		}
	}
}
