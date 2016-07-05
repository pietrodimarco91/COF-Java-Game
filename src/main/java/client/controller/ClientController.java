package client.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;
import client.view.cli.ClientOutputPrinter;
import controller.Packet;
import controller.RMIConnectionInt;
import controller.ServerSideConnectorInt;
import exceptions.InvalidInputException;

public abstract class ClientController {

	private static final String ADDRESS = "localhost";
	private static final int PORT = 2000;
	private ClientSideConnector clientSideConnector;
	private RMIConnectionInt rmiConnectionInt;
	private ServerSideConnectorInt packetSenderInt;
	private SocketInputOutputThread socketInputOutputThread;
	private static final Logger logger = Logger.getLogger(ClientController.class.getName());

	public ServerSideConnectorInt getServerConnector() {
		return packetSenderInt;
	}

	public void startSocketConnection(String nickName) {
		try {
			socketInputOutputThread = new SocketInputOutputThread(new Socket(ADDRESS, PORT));
			socketInputOutputThread.start();
			packetSenderInt = socketInputOutputThread;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				ClientOutputPrinter.printLine(e.getMessage());
				Thread.currentThread().interrupt();
			}
			packetSenderInt.sendToServer(new Packet(nickName));
		} catch (IOException e) {
			ClientOutputPrinter.printLine(e.getMessage());
		}
	}

	public void startRMIConnection(String nickName) {
		try {
			clientSideConnector = new ClientSideConnector();
			rmiConnectionInt = (RMIConnectionInt) Naming.lookup("rmi://localhost/registry");
			packetSenderInt = rmiConnectionInt.connect(clientSideConnector, nickName);
		} catch (NotBoundException e) {
			logger.log(Level.FINEST, "Error: the object you were looking for is not bounded", e);
		} catch (MalformedURLException e) {
			logger.log(Level.FINEST, "Error: the URL specified is invalid", e);
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: RemoteException was thrown", e);
		}
	}

	/**
	 * NEEDS IMPLEMENTATION
	 */
	public void disconnect() {
		try {
			if (clientSideConnector != null)
				UnicastRemoteObject.unexportObject(clientSideConnector, true);
			if (socketInputOutputThread != null)
				socketInputOutputThread.disconnect();
		} catch (NoSuchObjectException e) {
			ClientOutputPrinter.printLine(e.getMessage());
		} finally {
			System.exit(0);
		}
	}

	public boolean checkCorrectNickName(String nickName) {
		if (nickName.contains(" ") || nickName.length() < 4)
			return false;
		else
			return true;
	}

	/**
	 * Checks whether the specified parameters respect the rules or not.
	 *
	 * @param numberOfPlayers
	 * @param rewardTokenBonusNumber
	 * @param permitTileBonusNumber
	 * @param nobilityTrackBonusNumber
	 * @param linksBetweenCities
	 * @throws InvalidInputException
	 */
	public void parametersValidation(int numberOfPlayers, int rewardTokenBonusNumber, int permitTileBonusNumber,
			int nobilityTrackBonusNumber, int linksBetweenCities) throws InvalidInputException {
		if (numberOfPlayers < 2 || numberOfPlayers > 8)
			throw new InvalidInputException();
		if ((rewardTokenBonusNumber < 1 || rewardTokenBonusNumber > 3)
				|| (permitTileBonusNumber < 1 || permitTileBonusNumber > 3)
				|| (nobilityTrackBonusNumber < 1 || nobilityTrackBonusNumber > 3))
			throw new InvalidInputException();
		if (linksBetweenCities < 2 && linksBetweenCities > 4)
			throw new InvalidInputException();
	}
	
	public ClientSideConnector getClientSideConnector(){
		return clientSideConnector;
	}
	
	public SocketInputOutputThread getSocketThread(){
		return socketInputOutputThread;
	}

	public abstract void welcome(String[] args);

	public abstract void connect() throws RemoteException;

	public abstract void boardConfiguration();

	public abstract void mapConfiguration();

	public abstract void countDistance();

	public abstract void editConnection(String choice);

	public abstract void newConfiguration();

	public abstract void performNewAction();

	public abstract void requestBoardStatus();

	public abstract void sellItemOnMarket();

	public abstract void buyItemOnMarket();

	public abstract void requestPlayerStatus();

	public abstract void chat();

	public abstract void initialConfiguration();

	public abstract void play();

}
