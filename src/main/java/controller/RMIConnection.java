package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

/**
 * This class allows a Client to initialize the RMI Connection, and is used to
 * give the Client a reference of the ServerSideRMIConnector, which is a Remote
 * Object
 */
public class RMIConnection extends UnicastRemoteObject implements RMIConnectionInt {

	ArrayList<MatchHandler> matches;

	/**
	 * These threads are used by Server to handle the different connections
	 * coming from the Clients
	 */
	private ExecutorService thread;

	public RMIConnection(ArrayList<MatchHandler> matches, ExecutorService thread) throws RemoteException {
		this.matches = matches;
		this.thread = thread;
	}

	/**
	 * The Client needs the serverSideRMIConnector because otherwise he wouldn't
	 * be able to consult the Server
	 */
	@Override
	public ServerSideRMIConnectorInt connect(ClientSideRMIConnectorInt a) throws RemoteException {
		a.writeToClient("[SERVER]: RMI Connection correctly established");
		ServerSideRMIConnector serverSideRMIConnector = new ServerSideRMIConnector(a);
		thread.submit(new ClientHandler(serverSideRMIConnector, matches));
		return serverSideRMIConnector;
	}
}
