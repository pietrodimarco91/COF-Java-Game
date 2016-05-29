package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by pietro on 21/05/16.
 */
public interface Connector extends Remote{

    void writeToClient(String s) throws RemoteException;

    int receiveIntFromClient() throws RemoteException;

    String receiveStringFromClient() throws RemoteException;//To add UML Scheme

}
