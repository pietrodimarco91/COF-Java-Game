package controller.Client;

import java.rmi.Remote;
import java.rmi.RemoteException;


public interface RMIServerInt extends Remote {

     void connect (ConnectorInt a)throws RemoteException;
}
