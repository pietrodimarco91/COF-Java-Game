package controller;

import client.actions.Action;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Non possiamo fare a meno di questa classe in quanto se si usasse soltanto la
 * ClientSide interface il Server non riuscirebbe ad ascoltare!!
 */
public interface ServerSideConnectorInt extends Remote {

	public void sendToServer(Packet packet) throws RemoteException;

	void setMatchHandler(MatchHandler matchHandler);

	void setPlayerId(int id);
}
