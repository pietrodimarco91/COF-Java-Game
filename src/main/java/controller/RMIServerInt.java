package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerInt extends Remote {

    public void connect (ConnectorInt a)throws RemoteException;
}
