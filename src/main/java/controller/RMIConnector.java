package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This class is used in case of RMI Connection, it handles the real interaction with the user.
 */
public class RMIConnector extends UnicastRemoteObject implements Connector, ConnectorRMIServerInt {

    ConnectorRMIClientInt clientMethod;

    public RMIConnector() throws RemoteException {
    }

    @Override
    public void writeToClient(String s) throws RemoteException {
        clientMethod.tell(s);
    }

    @Override
    public int receiveIntFromClient() throws RemoteException {

        return clientMethod.receiveInt();
    }

    @Override
    public String receiveStringFromClient() throws RemoteException {
        return clientMethod.receiveString();
    }

}
