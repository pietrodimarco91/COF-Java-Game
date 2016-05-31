package controller;

import java.rmi.NoSuchObjectException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;

/**
 * Created by pietro on 21/05/16.
 */
public interface ConnectorInt extends Remote {

	void writeToClient(String s) throws RemoteException;

	int receiveIntFromClient() throws RemoteException;

	String receiveStringFromClient() throws RemoteException;// To add UML Scheme
}
