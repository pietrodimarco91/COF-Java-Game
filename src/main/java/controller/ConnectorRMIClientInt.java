package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by pietro on 24/05/16.
 */
public interface ConnectorRMIClientInt extends Remote {

    void Tell(String s) throws RemoteException;

    int receiveInt() throws RemoteException;

    String receiveString() throws RemoteException;
}
