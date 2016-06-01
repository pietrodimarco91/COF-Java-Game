package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIConnectionInt extends Remote {

    ServerSideRMIConnectorInt connect(ClientSideRMIConnectorInt a)throws RemoteException;
    
}
