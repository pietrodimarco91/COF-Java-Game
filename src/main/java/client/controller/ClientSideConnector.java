package client.controller;

import client.view.cli.ClientOutputPrinter;
import controller.ClientSideConnectorInt;
import controller.Packet;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class represents the remote object used by the server with an RMI
 * connection in order to send packets to the Client.
 */
public class ClientSideConnector extends UnicastRemoteObject implements ClientSideConnectorInt {

	private transient ClientGUIController guiController;

	public ClientSideConnector() throws RemoteException {
	}

	@Override
	/**
	 * This method is invoked when the Server sends a packet to the client, and
	 * this must be interpreted to perform the different actions.
	 */
	public void sendToClient(Packet packet) throws RemoteException {
		switch (packet.getHeader()) {
		case "MESSAGESTRING":
			ClientOutputPrinter.printLine(packet.getMessageString());
			if (guiController != null) {
				guiController.sendPacketToGUIController(packet);
			}
			break;
		case "UPDATE":
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

	/**
	 * This method is invoked by the client gui controllers to dynamically let
	 * the connectors know which is the current GUI controller.
	 */
	public void setGUIController(ClientGUIController controller) {
		this.guiController = controller;
	}
}
