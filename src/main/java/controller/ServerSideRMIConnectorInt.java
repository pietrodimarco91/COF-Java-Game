package controller;

import client.actions.Action;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Non possiamo fare a meno di questa classe in quanto se si usasse soltanto la
 * ClientSide interface il Server non riuscirebbe ad ascoltare!!
 */
public interface ServerSideRMIConnectorInt extends Remote {

	void writeToServer(String s) throws RemoteException;

	int receiveIntFromServer() throws RemoteException;

	void sendActionToServer(Action action) throws RemoteException;// To add UML Scheme

	void sendConfigurationToServer(ArrayList<Integer> config) throws RemoteException;// To add UML Scheme

	boolean checkCreator() throws RemoteException;

	void setTurn(boolean value);// To add UML Scheme

	Action getAction();

	void waitStart();

	void setMatchStarted();

	void setCreator(boolean b);

	ArrayList<Integer> getBoardConfiguration();
}
