package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by pietro on 21/05/16.
 */
public interface ClientSideConnectorInt extends Remote {

	public void sendToClient(Packet packet) throws RemoteException;
}
