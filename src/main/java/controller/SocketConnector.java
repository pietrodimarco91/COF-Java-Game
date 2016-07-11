package controller;

import client.controller.ClientGUIController;
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

/**
 * This class is used by the Server in case of socket connection, it listens the socket of the specific client
 * receiving requests and responding to them with specific packets
 */

public class SocketConnector extends Thread implements ClientSideConnectorInt, ServerSideConnectorInt {

	private static final Logger logger = Logger.getLogger(SocketConnector.class.getName());
	private ObjectInputStream inputObjectFromClient;
	private ObjectOutputStream outputObjectToClient;
	private MatchHandler matchHandler;
	private int playerId;

	/**
	 * Sets up the connection with the specific client
	 */
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



	/**
	 * It listens the client requests and send to the Server the packet received
	 */
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
				this.matchHandler.setPlayerOffline(playerId);

				break;

			} catch (IOException e) {
				ServerOutputPrinter
						.printLine("[SERVER] Client with nickname '" + matchHandler.getPlayers().get(playerId).getNickName()
								+ "' and ID " + playerId + " disconnected!");
				PubSub.notifyAllClientsExceptOne(playerId,matchHandler.getPlayers(),
						"Client with nickname '" + matchHandler.getPlayers().get(playerId).getNickName() + "' and ID "
								+ playerId + " disconnected!");
				this.matchHandler.setPlayerOffline(playerId);
				break;
			} catch (ClassNotFoundException e) {
				ServerOutputPrinter
						.printLine("[SERVER] Client with nickname '" + matchHandler.getPlayers().get(playerId).getNickName()
								+ "' and ID " + playerId + " disconnected!");
				PubSub.notifyAllClientsExceptOne(playerId,matchHandler.getPlayers(),
						"Client with nickname '" + matchHandler.getPlayers().get(playerId).getNickName() + "' and ID "
								+ playerId + " disconnected!");
				this.matchHandler.setPlayerOffline(playerId);
				break;
			}
		}
	}

	/**
	 * Sets up the matchHandler
	 * @param matchHandler
     */
	@Override
	public void setMatchHandler(MatchHandler matchHandler) {
		this.matchHandler = matchHandler;
	}

	/**
	 * Sets up the playerId
	 * @param id
     */
	@Override
	public void setPlayerId(int id) {
		this.playerId = id;
	}

	/**
	 * It send to the match handler the right request from the client decoding the packet
	 * @param packet
	 * @throws RemoteException
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
			else if(packet.getMessageString().equals("PASSTURN"))
				matchHandler.passTurn(playerId);
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

	/**
	 * It send to the client the packet, client side it will be decoded and interpreted
	 * @param packet
	 * @throws RemoteException
     */
	@Override
	public void sendToClient(Packet packet) throws RemoteException {
		try {
			outputObjectToClient.reset();
			outputObjectToClient.writeObject(packet);
			outputObjectToClient.flush();
		} catch (IOException e) {
			ServerOutputPrinter.printLine(e.getMessage());
		}
	}

	@Override
	public void setGUIController(ClientGUIController controller) {

	}

}
