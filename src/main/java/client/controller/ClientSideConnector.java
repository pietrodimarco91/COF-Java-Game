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

	private ClientGUIController guiController;

	public ClientSideConnector() throws RemoteException {

	}

	@Override
	public void sendToClient(Packet packet) throws RemoteException {
		switch (packet.getHeader()) {
		case "MESSAGESTRING":
			ClientOutputPrinter.printLine(packet.getMessageString());
			if (guiController != null) {
				guiController.sendPacketToGUIController(packet);
			}
			break;
		case "UPDATE":
			ClientOutputPrinter.printLine("*** GUI UPDATE RECEIVED: " + packet.getUpdate().getHeader() + " ***");
			if (guiController != null) {
				guiController.sendPacketToGUIController(packet);
			}
			break;
		case "CHAT":
			if (guiController != null) {
				guiController.sendPacketToGUIController(packet);
			}
			break;
		default:
		}
	}

	public void setGUIController(ClientGUIController controller) {
		this.guiController = controller;
	}
}
