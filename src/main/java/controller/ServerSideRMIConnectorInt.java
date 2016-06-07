package controller;

import client.actions.Action;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Non possiamo fare a meno di questa classe in quanto se si usasse soltanto la
 * ClientSide interface il Server non riuscirebbe ad ascoltare!!
 */
public interface ServerSideRMIConnectorInt extends Remote {

	void writeToServer(String s) throws RemoteException;

	int receiveIntFromServer() throws RemoteException;

	void sendActionToServer(Action action) throws RemoteException;// To add UML Scheme

	void setTurn(boolean value) throws RemoteException;// To add UML Scheme

	Action getAction() throws RemoteException;

	void waitStart();

	void setMatchStarted();
}
