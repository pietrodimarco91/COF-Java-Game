package client.controller;

import client.view.cli.ClientOutputPrinter;
import controller.ClientSideConnectorInt;
import controller.Packet;
import javafx.scene.control.TextArea;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by pietro on 29/05/16.
 */
public class ClientSideConnector extends UnicastRemoteObject implements ClientSideConnectorInt {

	private TextArea guiConsole;

	public ClientSideConnector() throws RemoteException {

	}

	@Override
	public void sendToClient(Packet packet) throws RemoteException {
		switch (packet.getHeader()) {
		case "MESSAGESTRING":
			ClientOutputPrinter.printLine(packet.getMessageString());
			if (guiConsole != null) {
				this.guiConsole.appendText(packet.getMessageString());
			}
			break;
		case "UPDATE":
			ClientOutputPrinter.printLine("*** GUI UPDATE RECEIVED: " + packet.getUpdate().getHeader() + " ***");
			if (guiConsole != null) {
				this.guiConsole.appendText(packet.getMessageString());
			}
			break;
		default:
		}
	}

	public void setGUIConsole(TextArea console) {
		this.guiConsole = console;
	}
}
