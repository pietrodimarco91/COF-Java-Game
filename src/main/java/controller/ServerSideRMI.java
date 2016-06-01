package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

/**
 * This class is used in case of ServerSideRMI Connection, it handles the real interaction with the user.
 */
public class ServerSideRMI extends UnicastRemoteObject implements ServerSideRMIInt {

    ArrayList<MatchHandler> matches;

    /**
     *These threads are used by Server to handle the different connections coming from the Clients
     */
    private ExecutorService thread;


    public ServerSideRMI(ArrayList<MatchHandler> matches, ExecutorService thread) throws RemoteException {
        this.matches=matches;
        this.thread=thread;

    }
    @Override
    public ServerSideRMIConnector connect(ConnectorInt a) throws RemoteException {
        a.writeToClient("Connection RMI established");
        ServerSideRMIConnector serverSideRMIConnector=new ServerSideRMIConnector(a);
        thread.submit(new ClientHandler(serverSideRMIConnector,matches));
        return serverSideRMIConnector;
    }
}
