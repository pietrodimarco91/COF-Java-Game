package controller;

import client.actions.Action;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * It is implemented by the SocketConnector and by the ServerSideConnector in case of RMI Connection,
 * it generates requests to the Server
 */
public interface ServerSideConnectorInt extends Remote {

	public void sendToServer(Packet packet) throws RemoteException;

	void setMatchHandler(MatchHandler matchHandler) throws RemoteException;

	void setPlayerId(int id) throws RemoteException;
}
