package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIConnectionInt extends Remote {

    ServerSideConnectorInt connect(ClientSideConnectorInt a, String nickName)throws RemoteException;
    
}
