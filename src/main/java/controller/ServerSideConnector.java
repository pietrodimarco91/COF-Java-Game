package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by pietro on 01/06/16.
 */
public class ServerSideConnector extends UnicastRemoteObject implements ServerSideConnectorInt {

	private transient MatchHandler matchHandler;
	private int playerId;

	public ServerSideConnector() throws RemoteException {
		super();
	}

	/**
	 * IN THIS CASE IT'S USED BY CLIENTS
	 */
	@Override
	public void sendToServer(Packet packet) throws RemoteException {
		switch (packet.getHeader()) {
		case "CONFIGOBJECT":
			matchHandler.setConfigObject(packet.getConfigObject(), playerId);
			break;
		case "BOARDSTATUS":
			matchHandler.sendBoardStatus(playerId);
			break;
		case "ACTION":
			matchHandler.evaluateAction(packet.getAction(), playerId);
			break;
		case "ADDLINK":
			matchHandler.generateConnection(packet.getMessageString(), playerId);
			break;
		case "REMOVELINK":
			matchHandler.removeConnection(packet.getMessageString(), playerId);
			break;
		case "COUNTDISTANCE":
			matchHandler.countDistance(packet.getMessageString(), playerId);
			break;
		case "MESSAGESTRING":
			if (packet.getMessageString().equals("REQUESTCONFIG"))
				matchHandler.sendConfigurations(playerId);
			else if (packet.getMessageString().equals("REQUESTPLAYERSTATUS"))
				matchHandler.sendPlayerStatus(playerId);
			else if (packet.getMessageString().equals("FINISHMAPCONFIG"))
				matchHandler.checkIfGraphIsConnected(playerId);
			else
				matchHandler.messageFromClient(packet.getMessageString(), playerId);
			break;
		case "CHAT":
			matchHandler.chat(playerId,packet.getMessageString());
			break;
		case "CONFIGID":
			matchHandler.setExistingConf(packet.getConfigId(), playerId);
			break;
		case "MARKET":
			if (packet.getMarketEvent() instanceof MarketEventBuy)
				matchHandler.buyEvent(packet.getMarketEvent(), playerId);
			else
				matchHandler.sellEvent(packet.getMarketEvent(), playerId);
			break;
		default:
		}
	}

	@Override
	public void setMatchHandler(MatchHandler matchHandler) {
		this.matchHandler = matchHandler;
	}

	@Override
	public void setPlayerId(int id) {
		this.playerId = id;
	}
}
