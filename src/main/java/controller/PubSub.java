package controller;

import server.view.cli.ServerOutputPrinter;

import java.rmi.RemoteException;
import java.util.List;

import model.Board;

public abstract class PubSub {

	public static void notifyAllClients(List<Player> players, String message, Board board) {
		for (Player player : players) {
			try {
				if (!player.playerIsOffline()) {
					player.getConnector().sendToClient(new Packet(new UpdateState(board)));
					player.getConnector().sendToClient(new Packet("[GAME NOTIFY] " + message));
				}
			} catch (RemoteException e) {
				player.setPlayerOffline();
				ServerOutputPrinter.printLine("[SERVER] Client with nickname '" + player.getNickName() + "' and ID "
						+ player.getId() + " disconnected!");
				notifyAllClientsExceptOne(player.getId(), players, "Client with nickname '" + player.getNickName()
						+ "' and ID " + player.getId() + " disconnected!");
			}
		}
	}

	public static void notifyAllClientsExceptOne(int playerId, List<Player> players, String message) {
		for (Player player : players) {
			if (player.getId() != playerId) {
				try {
					if (!player.playerIsOffline())
						player.getConnector().sendToClient(new Packet("[GAME NOTIFY] " + message));
				} catch (RemoteException e) {
					player.setPlayerOffline();
					ServerOutputPrinter.printLine("[SERVER] Client with nickname '" + player.getNickName() + "' and ID "
							+ player.getId() + " disconnected!");
					notifyAllClientsExceptOne(player.getId(), players, "Client with nickname '" + player.getNickName()
							+ "' and ID " + player.getId() + " disconnected!");
				}
			}
		}
	}

	public static void chatMessage(int playerId, List<Player> players, String message) {
		for (Player player : players) {
			try {
				if (!player.playerIsOffline())
					player.getConnector()
							.sendToClient(new Packet("[CHAT] " + players.get(playerId).getNickName() + ": " + message, "***"));
			} catch (RemoteException e) {
				player.setPlayerOffline();
				ServerOutputPrinter.printLine("[SERVER] Client with nickname '" + player.getNickName() + "' and ID "
						+ player.getId() + " disconnected!");
				notifyAllClientsExceptOne(player.getId(), players, "Client with nickname '" + player.getNickName()
						+ "' and ID " + player.getId() + " disconnected!");
			}
		}
	}

}
