package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Non possiamo fare a meno di questa classe in quanto se si usasse soltanto la
 * ClientSide interface il Server non riuscirebbe ad ascoltare!!
 */
public interface ServerSideRMIConnectorInt extends Remote {

	void writeToServer(String s) throws RemoteException;

	int receiveIntFromServer() throws RemoteException;

	String receiveStringFromServer() throws RemoteException;// To add UML Scheme
}
