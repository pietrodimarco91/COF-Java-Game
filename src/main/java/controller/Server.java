package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.RemoteException;
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
	SocketConnector socketConnector;
	RMIServer rmiServer;
	/**
	 *These threads are used by Server to handle the different connections coming from the Clients
	 */
	private ExecutorService thread;
	/**
	 * The Server stores an array of currently on-going matches through their MatchHandlers
	 */
	private ArrayList<MatchHandler> matches;
	private ServerSocket welcomeSocket;


	public Server() {
		thread= Executors.newCachedThreadPool();
		this.matches=new ArrayList<MatchHandler>();
		try {
			rmiServer=new RMIServer(matches);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		this.waitConnection();
		try {
			this.welcomeSocket=new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	/**
	 * This method wait the connection of the Clients and then the different Threads handle the different clients.
	 */
	private void waitConnection() {
		while(true){
			try {
				socketConnector=new SocketConnector(welcomeSocket.accept());
			} catch (IOException e) {
				e.printStackTrace();
			}
			thread.submit(new ClientHandler(socketConnector, matches));
		}
	}



}