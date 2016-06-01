package controller.Client;

import controller.RMIConnectionInt;
import controller.ServerSideRMIConnectorInt;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

import client.view.cli.ClientOutputPrinter;

/**
 * 
 */
public class Client {

	ClientSideRMIConnector clientSideRMIConnector;
	RMIConnectionInt rmiConnectionInt;
	ClientSocket clientSocket;
	ServerSideRMIConnectorInt serverSideConnectorInt;
	
	/**
	 * Default constructor
	 */
	public Client() {
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
			rmiConnectionInt = (RMIConnectionInt) Naming.lookup("rmi://127.0.0.1/registry");
			serverSideConnectorInt =rmiConnectionInt.connect(clientSideRMIConnector);
		} catch (NotBoundException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
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