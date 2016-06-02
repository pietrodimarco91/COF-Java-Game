package controller;

import java.rmi.RemoteException;
import java.util.List;

import server.view.cli.ServerOutputPrinter;

public abstract class PubSub {
	
	public void notifyAllClients(List<Player> players,String message) {
		for(Player player : players) {
			try {
				player.getConnector().writeToClient(message);
			} catch (RemoteException e) {
				ServerOutputPrinter.printLine("Error: couldn't write to Client");
			}
		}
	}
}
