package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIConnectionInt extends Remote {

    ServerSideRMIConnector connect(ClientSideRMIInt a)throws RemoteException;
}
