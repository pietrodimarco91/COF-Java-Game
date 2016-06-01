package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerSideRMIInt extends Remote {

    ServerSideRMIConnector connect(ConnectorInt a)throws RemoteException;
}
