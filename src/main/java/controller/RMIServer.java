package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

/**
 * This class is used in case of RMIServer Connection, it handles the real interaction with the user.
 */
public class RMIServer extends UnicastRemoteObject implements RMIServerInt {

    ArrayList<MatchHandler> matches;

    /**
     *These threads are used by Server to handle the different connections coming from the Clients
     */
    private ExecutorService thread;


    public RMIServer(ArrayList<MatchHandler> matches, ExecutorService thread) throws RemoteException {
        this.matches=matches;
        this.thread=thread;

    }
    @Override
    public void connect(ConnectorInt a) throws RemoteException {
        a.writeToClient("Connection RMI established");
        thread.submit(new ClientHandler(a,matches));
    }
}
