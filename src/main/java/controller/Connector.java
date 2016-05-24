package controller;

import java.rmi.RemoteException;

/**
 * Created by pietro on 21/05/16.
 */
public interface Connector{

    void writeToClient(String s) throws RemoteException;

    int receiveIntFromClient() throws RemoteException;

    String receiveStringFromClient() throws RemoteException;//To add UML Scheme

}
