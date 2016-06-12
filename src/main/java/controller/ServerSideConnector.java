package controller;

import client.actions.Action;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by pietro on 01/06/16.
 */
public class ServerSideConnector extends UnicastRemoteObject implements ServerSideConnectorInt {

    private boolean yourTurn;
    private Action pendingAction;
    private boolean actionSent;
    private boolean matchStarted;
    private boolean youAreCreator;
    private boolean creatorHasBeenSet;
    private ArrayList<Integer> pendingConfig;
    private boolean configSent;
    private MatchHandler matchHandler;

    public ServerSideConnector() throws RemoteException {
        super();
    }


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


    /**
     *IN THIS CASE IT'S USED BY CLIENTS
     */
    @Override
    public void sendToServer(Packet packet) throws RemoteException {
        switch (packet.getHeader()) {
            case "CONFIGOBJECT":
                break;
            case "BOARDSTATUS":
                matchHandler.getBoardStatus();
                break;
            case "ACTION":
                matchHandler.
                break;
            case "ADDLINK":
                break;
            case "REMOVELINK":
                break;
            case "MESSAGESTRING":
                break;
            case "CONFIGID":
                break;
            case "MARKET":
                break;

        }
    }

    @Override
    public void setMatchHandler(MatchHandler matchHandler) {
        this.matchHandler=matchHandler;
    }
}
