package controller.Client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by pietro on 29/05/16.
 */
public class Connector extends UnicastRemoteObject implements ConnectorInt{

    protected Connector() throws RemoteException {
    }

    @Override
    public void writeToClient(String s) throws RemoteException {
            System.out.println(s);
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
