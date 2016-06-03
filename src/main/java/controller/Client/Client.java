package controller.Client;

import controller.MatchHandler;
import controller.RMIConnectionInt;
import controller.ServerSideRMIConnectorInt;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

import client.view.cli.ClientOutputPrinter;

/**
 * 
 */
public class Client {

	private static final Logger logger = Logger.getLogger(Client.class.getName());
	ClientSideRMIConnector clientSideRMIConnector;
	RMIConnectionInt rmiConnectionInt;
	ClientSocket clientSocket;
	ServerSideRMIConnectorInt serverSideConnectorInt;
	
	/**
	 * Default constructor
	 */
	public Client() {
		logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
		Scanner input = new Scanner(System.in);
		ClientOutputPrinter.printLine("How do you want to connect?\n1)RMI\n2)Socket");
		switch (input.nextInt()){
			case 1:
				this.startRMIConnection();
				break;
			case 2:
				this.startSocketConnection();
				break;
		}
	}

	private void startSocketConnection() {
		clientSocket=new ClientSocket();
	}

	private void startRMIConnection() {
		try {
			clientSideRMIConnector=new ClientSideRMIConnector();
			rmiConnectionInt = (RMIConnectionInt) Naming.lookup("rmi://localhost/registry");
			serverSideConnectorInt =rmiConnectionInt.connect(clientSideRMIConnector);
		} catch (NotBoundException e) {
			logger.log(Level.FINEST, "Error: the object you were looking for is not bounded", e);
		} catch (MalformedURLException e) {
			logger.log(Level.FINEST, "Error: the URL specified is invalid", e);
		} catch (RemoteException e) {
			logger.log(Level.FINEST, "Error: RemoteException was thrown", e);
		}
	}

	/**
	 * @return
	 */
	public boolean hasBuiltLastEmporium() {
		// TODO implement here
		return false;
	}
	

	/**
	 * @return
	 */
	public void performActions() {
		// TODO implement here

	}

	/**
	 * @return
	 */
	public void mainActions() {
		// TODO implement here

	}

	/**
	 * @return
	 */
	public void quickActions() {
		// TODO implement here

	}


	/**
	 * @return
	 */
	public void buildEmporiumWithKingsHelp() {
		// TODO implement here

	}

	





}