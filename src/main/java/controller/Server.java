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
	/**
	 * Matches Ids
	 */
	private static int id=0;
	/**
	 *This attribute handles every interaction with the user.
	 */
	private ConnectorHandler connectorHandler;
	/**
	 *These threads are used by Server to handles the different connections coming from the Clients
	 */
	private ExecutorService thread;
	/**
	 * The Server stores an array of currently on-going matches through their MatchHandlers
	 */
	private ArrayList<MatchHandler> matches;

	private Connector connector;


	public Server() {
		this.connectorHandler =new ConnectorHandler(port);
		thread= Executors.newCachedThreadPool();
		this.matches=new ArrayList<MatchHandler>();
		this.waitConnection();
	}

	public static int getId() {
		return id++;
	}

	/**
	 * This method wait the connection of the Clients and then the different Threads handle the different clients.
	 */
	private void waitConnection() {
		Object lock = null;
		RMIWaitConnectionThread waitRmiConnection=new RMIWaitConnectionThread(lock);
		SocketWaitConnectionThread waitSocketConnection=new SocketWaitConnectionThread(lock,port);
		while(true){
			waitRmiConnection.start();
			waitSocketConnection.start();
			try {
				this.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(!waitRmiConnection.isAlive()){
				connector=waitRmiConnection.getConnector();
				waitRmiConnection=new RMIWaitConnectionThread(lock);
			}
			if(!waitSocketConnection.isAlive()){
				connector=waitSocketConnection.getConnector();
				waitSocketConnection= new SocketWaitConnectionThread(lock, port);
			}
			thread.submit(new ClientHandler(connector, matches));
		}
	}
	

}