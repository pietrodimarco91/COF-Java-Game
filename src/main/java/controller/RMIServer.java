package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * This class is used in case of RMIServer Connection, it handles the real interaction with the user.
 */
public class RMIServer extends UnicastRemoteObject implements RMIServerInt {

    ArrayList<MatchHandler> matches;

    public RMIServer(ArrayList<MatchHandler> matches) throws RemoteException {
        this.matches=matches;

    }
    @Override
    public void connect(Connector a) throws RemoteException {
        new ClientHandler(a,matches);
    }
}
