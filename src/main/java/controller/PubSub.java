package controller;

import server.view.cli.ServerOutputPrinter;

import java.rmi.RemoteException;
import java.util.List;

public abstract class PubSub {

	public static void notifyAllClients(List<Player> players, String message) {
		for (Player player : players) {
			try {
				player.getConnector().sendToClient(new Packet("[GAME NOTIFY] " + message));
			} catch (RemoteException e) {
				ServerOutputPrinter.printLine("Error: couldn't write to Client");
			}
		}
	}

	public static void notifyAllClientsExceptOne(int playerId, List<Player> players, String message) {
		for (Player player : players) {
			if (player.getId() != playerId) {
				try {
					player.getConnector().sendToClient(new Packet("[GAME NOTIFY] " + message));
				} catch (RemoteException e) {
					ServerOutputPrinter.printLine("Error: couldn't write to Client");
				}
			}
		}
	}
	
	public static void chatMessage(int playerId, List<Player> players, String message) {
		for (Player player : players) {
			if (player.getId() != playerId) {
				try {
					player.getConnector().sendToClient(new Packet("[CHAT] "+players.get(playerId).getNickName()+": " + message));
				} catch (RemoteException e) {
					ServerOutputPrinter.printLine("Error: couldn't write to Client");
				}
			}
		}
	}

}
