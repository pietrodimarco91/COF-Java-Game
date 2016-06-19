package controller;

import server.view.cli.ServerOutputPrinter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class SocketConnector extends Thread implements ClientSideConnectorInt, ServerSideConnectorInt {

	private static final Logger logger = Logger.getLogger(SocketConnector.class.getName());
	private ObjectInputStream inputObjectFromClient;
	private ObjectOutputStream outputObjectToClient;
	private MatchHandler matchHandler;
	private int playerId;

	public SocketConnector(Socket socket) {
		logger.addHandler(new StreamHandler(System.out, new SimpleFormatter()));
		try {
			outputObjectToClient = new ObjectOutputStream(socket.getOutputStream());
			inputObjectFromClient = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while opening the output/input stream for 'socket'", e);
		}
		ServerOutputPrinter.printLine("[SERVER] New Socket connection established with Client!");
	}

	@Override
	public void run() {
		while (true) {
			try {
				sendToServer((Packet) inputObjectFromClient.readObject());
			} catch (SocketException e) {
				ServerOutputPrinter
						.printLine("[SERVER] Client with nickname '" + matchHandler.getPlayers().get(playerId).getNickName()
								+ "' and ID " + playerId + " disconnected!");
				PubSub.notifyAllClientsExceptOne(playerId,matchHandler.getPlayers(),
						"Client with nickname '" + matchHandler.getPlayers().get(playerId).getNickName() + "' and ID "
								+ playerId + " disconnected!");
				break;
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	// *****SERVER SIDE METHOD******//
	@Override
	public void setMatchHandler(MatchHandler matchHandler) {
		this.matchHandler = matchHandler;
	}

	@Override
	public void setPlayerId(int id) {
		this.playerId = id;
	}
	
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
				matchHandler.setPlayerNickName(playerId, packet.getMessageString());
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
	
	// *****SERVER SIDE METHOD******//
	//in this case this method is used to receive something from client
	@Override
	public void sendToClient(Packet packet) throws RemoteException {
		try {
			outputObjectToClient.writeObject(packet);
			outputObjectToClient.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
