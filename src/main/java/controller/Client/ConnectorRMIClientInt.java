package controller.Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by pietro on 24/05/16.
 */
public interface ConnectorRMIClientInt extends Remote {

    public void Tell(String s) throws RemoteException;

    public int receiveInt() throws RemoteException;

    public String receiveString() throws RemoteException;
}
