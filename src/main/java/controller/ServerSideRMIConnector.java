package controller;

import client.actions.Action;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by pietro on 01/06/16.
 */
public class ServerSideRMIConnector extends UnicastRemoteObject implements ConnectorInt {

    ClientSideRMIConnectorInt clientSideRMIConnectorInt;
    private boolean yourTurn;
    private Action pendingAction;
    private boolean actionSent;
    private boolean matchStarted;

    public ServerSideRMIConnector(ClientSideRMIConnectorInt clientSideRMIConnectorInt) throws RemoteException {
        super();
        yourTurn=false;
        matchStarted=false;
        actionSent=false;
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
    public void sendActionToServer(Action action) throws RemoteException {
            if (yourTurn) {
                pendingAction = action;
                actionSent=true;
            }
    }


    @Override
    public void setTurn(boolean value) throws RemoteException {
        yourTurn=value;
    }

    @Override
    public Action getAction() throws RemoteException {
        while(!actionSent){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        actionSent=false;
        return pendingAction;
    }



    @Override
    public void waitStart() {
        while(!matchStarted){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setMatchStarted() {
        matchStarted=true;
    }


}
