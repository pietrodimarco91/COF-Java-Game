package controller;

import client.actions.Action;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Created by pietro on 01/06/16.
 */
public class ServerSideConnector extends UnicastRemoteObject implements ServerSideConnectorInt {

    private MatchHandler matchHandler;
    private int playerId;

    public ServerSideConnector() throws RemoteException {
        super();
    }

    /**
     *IN THIS CASE IT'S USED BY CLIENTS
     */
    @Override
    public void sendToServer(Packet packet) throws RemoteException {
        switch (packet.getHeader()) {
            case "CONFIGOBJECT":
                matchHandler.setConfigObject(packet.getMessageString(),playerId);
                break;
            case "BOARDSTATUS":
                matchHandler.getBoardStatus(playerId);
                break;
            case "ACTION":
                matchHandler.evaluateAction(packet.getAction(),playerId);
                break;
            case "ADDLINK":
                matchHandler.addLink(packet.getMessageString(),playerId);
                break;
            case "REMOVELINK":
                matchHandler.removeLink(packet.getMessageString(),playerId);
                break;
            case "MESSAGESTRING":
                matchHandler.messageFromClient(packet.getMessageString(),playerId);
                break;
            case "CONFIGID":
                matchHandler.setExistingConf(packet.getConfigId(),playerId);
                break;
            case "MARKET":
                if(packet.getMarketEvent() instanceof MarketEventBuy)
                    matchHandler.buyEvent(packet.getMarketEvent(),playerId);
                else
                matchHandler.sellEvent(packet.getMarketEvent(),playerId);
                break;
            default:
        }
    }

    @Override
    public void setMatchHandler(MatchHandler matchHandler) {
        this.matchHandler=matchHandler;
    }

    @Override
    public void setPlayerId(int id) {
        this.playerId=id;
    }
}
