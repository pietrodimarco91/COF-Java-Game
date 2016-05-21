package controller;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class initializes the game engine and the connection among the Clients.
 */
public class Server {

	/**
	 * The IP Address of the Server, that means the address that all Clients
	 * must know to connect to the game.
	 */
	private static final String ip="localhost";
	/**
	 * The port of the specified IP Address (connection parameter).
	 */
	private static final int port=80;
	private ConnectorHandler connectorHandler;
	private ExecutorService thread;
	/**
	 * The Server stores an array of currently on-going matches through their MatchHandlers
	 */
	private ArrayList<MatchHandler> matches;


	/**
	 * Default constructor
	 */
	public Server() {
		this.connectorHandler =new ConnectorHandler(port);
		thread= Executors.newCachedThreadPool();
		this.matches=new ArrayList<MatchHandler>();
		this.waitConnection();
	}

	/**
	 * NEED IMPLEMENTATION!
	 *@param connectionHandle.getConnector() return a specific connector type (RMI,Socket)
	 */

	private void waitConnection() {
		while(true){
			thread.submit(new UserHandler(connectorHandler.getConnector(), matches));
		}
	}
	

}