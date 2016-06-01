package controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by pietro on 01/06/16.
 */
public class ServerSideRMIConnector implements Connector, Remote {

    ClientSideRMIConnectorInt clientSideRMIConnectorInt;

    public ServerSideRMIConnector(ClientSideRMIConnectorInt clientSideRMIConnectorInt) {
        this.clientSideRMIConnectorInt=clientSideRMIConnectorInt;
    }

    @Override
    public void writeToClient(String s) throws RemoteException {
        clientSideRMIConnectorInt.writeToClient(s);

    }

    @Override
    public int receiveIntFromClient() throws RemoteException {

        return 0;
    }

    @Override
    public String receiveStringFromClient() throws RemoteException {
        //Sarà un metodo invocato dal client!bisogna gestirla internamente controllando lo stato del Server!
        return null;
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
}
