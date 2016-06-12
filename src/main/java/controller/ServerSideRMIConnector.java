package controller;

import client.actions.Action;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by pietro on 01/06/16.
 */
public class ServerSideRMIConnector extends UnicastRemoteObject implements ConnectorInt {

    ClientSideRMIConnectorInt clientSideRMIConnectorInt;
    private boolean yourTurn;
    private Action pendingAction;
    private boolean actionSent;
    private boolean matchStarted;
    private boolean youAreCreator;
    private boolean creatorHasBeenSet;
    private ArrayList<Integer> pendingConfig;
    private boolean configSent;

    public ServerSideRMIConnector(ClientSideRMIConnectorInt clientSideRMIConnectorInt) throws RemoteException {
        super();
        yourTurn=false;
        configSent=false;
        matchStarted=false;
        creatorHasBeenSet=false;
        actionSent=false;
        youAreCreator=false;
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
    public void setTurn(boolean value){
        yourTurn=value;
    }

    @Override
    public Action getAction() {
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

    @Override
    public boolean checkCreator() throws RemoteException{
        while(!creatorHasBeenSet){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return youAreCreator;
    }


    @Override
    public void setCreator(boolean b) {
        creatorHasBeenSet=true;
        youAreCreator=b;
    }


    @Override
    public void sendConfigurationToServer(ArrayList<Integer> config) throws RemoteException {
        pendingConfig = config;
        configSent=true;

    }

    @Override
    public ArrayList<Integer> getBoardConfiguration() {
        while(!configSent){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return pendingConfig;
    }


}
