package controller;

import server.view.cli.ServerOutputPrinter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class SocketConnector extends Thread implements ClientSideConnectorInt, ServerSideConnectorInt {


	private static final Logger logger= Logger.getLogger( SocketConnector.class.getName() );
    private Socket socket;
    private ObjectInputStream inputObjectFromClient;
    private ObjectOutputStream outputObjectToClient;
    private MatchHandler matchHandler;
    private int playerId;


    public SocketConnector(Socket socket) {
        this.socket=socket;
        try {
            outputObjectToClient=new ObjectOutputStream(socket.getOutputStream());
            inputObjectFromClient=new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while opening the output/input stream for 'socket'", e);
        }
        ServerOutputPrinter.printLine("[SERVER] New Socket connection established");
    }


 

    @Override
    public void run() {
        while (true) {
            try {
                sendToServer((Packet) inputObjectFromClient.readObject());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
    //*****SERVER SIDE METHOD******//
    @Override
    public void setMatchHandler(MatchHandler matchHandler) {
        this.matchHandler=matchHandler;
    }

    @Override
    public void setPlayerId(int id) {
        this.playerId=id;
    }

    @Override
    public void sendToClient(Packet packet) throws RemoteException {
        try {
            outputObjectToClient.writeObject(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    //*****CLIENT SIDE METHOD******//


    @Override
    public void sendToServer(Packet packet) throws RemoteException {
        switch (packet.getHeader()) {
            case "CONFIGOBJECT":
                matchHandler.setConfigObject(packet.getConfigObject(),playerId);
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
}
