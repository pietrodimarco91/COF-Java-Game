package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by pietro on 01/06/16.
 */
public class ServerSideRMIConnector extends UnicastRemoteObject implements Connector{

    ClientSideRMIInt clientSideRMIInt;

    protected ServerSideRMIConnector(ClientSideRMIInt a) throws RemoteException {
        this.clientSideRMIInt=a;
    }

    @Override
    public void writeToClient(String s) throws RemoteException {

    }

    @Override
    public int receiveIntFromClient() throws RemoteException {
        return 0;
    }

    @Override
    public String receiveStringFromClient() throws RemoteException {
        return null;
    }
}
