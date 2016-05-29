package controller.Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by pietro on 24/05/16.
 */
public interface RMIServerInt extends Remote {

    public void connect (ConnectorInt a)throws RemoteException;
}
