package controller.Client;

import client.view.cli.ClientOutputPrinter;
import controller.ClientSideConnectorInt;
import controller.MatchHandler;
import controller.Packet;
import controller.ServerSideConnectorInt;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.RemoteException;

public class SocketInputOutputThread extends Thread implements ClientSideConnectorInt, ServerSideConnectorInt {

	private ObjectInputStream inputObjectFromServer;
	private ObjectOutputStream outputObjectToServer;


	public SocketInputOutputThread(Socket socket) {
		try {
			outputObjectToServer=new ObjectOutputStream(socket.getOutputStream());
			inputObjectFromServer=new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	//*****CLIENT SIDE METHOD******//

	@Override
	public void run() {
			while (true) {
				try {
					sendToClient((Packet) inputObjectFromServer.readObject());
				} catch(SocketException e) {
					ClientOutputPrinter.printLine("Critical error: Server went down and the connection has been closed.");
					break;
				} catch (IOException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
	}


	@Override
	public void sendToServer(Packet packet) throws RemoteException {
		try {
			outputObjectToServer.writeObject(packet);
			outputObjectToServer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void setPlayerId(int id) {

	}

	public void sendToClient(Packet packet) throws RemoteException {
		switch (packet.getHeader()) {
			case "MESSAGESTRING":
				ClientOutputPrinter.printLine(packet.getMessageString());
				break;
			default:
		}

	}

	//*****SERVER SIDE METHOD******//

	@Override
	public void setMatchHandler(MatchHandler matchHandler) {

	}
}
