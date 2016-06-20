package controller;

import server.view.cli.ServerOutputPrinter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
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
	private static final int port=2000;
	
	/**
	 * The boolean variable to stop the server
	 */
	private static boolean stopServer=false;
	
	/**
	 * Matches Ids
	 */
	private static int id=0;
	SocketConnector socketConnector;
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
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			RMIConnectionInt b=new RMIConnection(matches,thread);
			try {
				try {
					Naming.bind("rmi://localhost/registry", b);
				} catch (AlreadyBoundException e) {
					e.printStackTrace();
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			ServerOutputPrinter.printLine("[SERVER] Ready to receive RMI invocations.");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		try {
			this.welcomeSocket=new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		ServerOutputPrinter.printLine("[SERVER] Ready to receive Socket Connections.");
		this.waitSocketConnection();
	}

	public static int getId() {
		return id++;
	}

	/**
	 * This method is invoked when the server will have to shut down
	 */
	public static void stopServer() {
		stopServer=true;
	}
	
	/**
	 * This method wait the connection of the Clients and then the different Threads handle the different clients.
	 */
	private void waitSocketConnection() {
		while(!stopServer){
			try {
				socketConnector=new SocketConnector(welcomeSocket.accept());
				socketConnector.start();
				socketConnector.sendToClient(new Packet("[SERVER] Socket Connection correctly established with Server engine!"));
				thread.submit(new ClientHandler(socketConnector, socketConnector, matches, "tempNickName"));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}