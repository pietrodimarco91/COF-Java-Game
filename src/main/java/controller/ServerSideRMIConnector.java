package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by pietro on 01/06/16.
 */
public class ServerSideRMIConnector extends UnicastRemoteObject implements ConnectorInt {

    ClientSideRMIConnectorInt clientSideRMIConnectorInt;
    MatchHandler matchHandler;

    public ServerSideRMIConnector(ClientSideRMIConnectorInt clientSideRMIConnectorInt) throws RemoteException {
        super();
        this.clientSideRMIConnectorInt=clientSideRMIConnectorInt;
    }

    @Override
    public void writeToClient(String s) throws RemoteException {
        clientSideRMIConnectorInt.writeToClient(s);
    }

    @Override
    public int receiveIntFromClient() throws RemoteException {
    	return clientSideRMIConnectorInt.receiveIntFromClient();
    }

    @Override
    public String receiveStringFromClient() throws RemoteException {
        return clientSideRMIConnectorInt.receiveStringFromClient();
    }

    @Override
    public void writeToServer(String s) throws RemoteException {
        //Sarà un metodo invocato dal client!bisogna gestirla internamente controllando lo stato del Server!

    }

    @Override
    public int receiveIntFromServer() throws RemoteException {
        //Sarà un metodo invocato dal client!bisogna gestirla internamente controllando lo stato del Server!
        return 0;
    }

    @Override
    public String receiveStringFromServer() throws RemoteException {
        //Sarà un metodo invocato dal client!bisogna gestirla internamente controllando lo stato del Server!
        return null;
    }

    @Override
    public void setMatchHandler(MatchHandler matchHandler) {
        this.matchHandler=matchHandler;
    }
}
