package controller;

import server.view.cli.ServerOutputPrinter;

import java.rmi.RemoteException;
import java.util.List;

public abstract class PubSub {
	
	public void notifyAllClients(List<Player> players,String message) {
		for(Player player : players) {
			try {
				player.getConnector().sendToClient(new Packet(message));
			} catch (RemoteException e) {
				ServerOutputPrinter.printLine("Error: couldn't write to Client");
			}
		}
	}


}
