package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import server.view.cli.ServerOutputPrinter;

/**
 * This class allows a Client to initialize the RMI Connection, and is used to
 * give the Client a reference of the ServerSideConnector, which is a Remote
 * Object
 */
public class RMIConnection extends UnicastRemoteObject implements RMIConnectionInt {

	ArrayList<MatchHandler> matches;


	/**
	 * These threads are used by Server to handle the different connections
	 * coming from the Clients
	 */
	private transient ExecutorService thread;

	public RMIConnection(ArrayList<MatchHandler> matches, ExecutorService thread) throws RemoteException {
		this.matches = matches;
		this.thread = thread;
	}

	/**
	 * The Client needs the serverSideRMIConnector because otherwise he wouldn't
	 * be able to consult the Server
	 */
	@Override
	public ServerSideConnectorInt connect(ClientSideConnectorInt a, String nickName) throws RemoteException {
		a.sendToClient(new Packet("[SERVER] RMI Connection correctly established with Server engine!"));
		ServerOutputPrinter.printLine("[SERVER] RMI Connection correctly established from a new Client");
		ServerSideConnectorInt serverSideConnector=new ServerSideConnector();
		thread.submit(new ClientHandler(a,serverSideConnector, matches, nickName));
		return serverSideConnector;
	}
}
