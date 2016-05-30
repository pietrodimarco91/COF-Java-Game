package controller;

import java.rmi.RemoteException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This Class let every user to create a new match or to join in a pending match
 * that already exist.
 */
public class ClientHandler implements Runnable {

	private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());

	private ArrayList<MatchHandler> matches;

	/**
	 * This attribute handles every interaction with the user.
	 */
	private ConnectorInt connectorInt;

	public ClientHandler(ConnectorInt connectorInt, ArrayList<MatchHandler> matches) {
		this.matches = matches;
		this.connectorInt = connectorInt;
	}

	/**
	 * This is the run() method of the Thread. It asks to the User if he wants
	 * to join in a pending match or to launch a new match.
	 */
	@Override
	public void run() {
		if (!joinMatch()) {
			launchNewMatch(new Date());
		}
	}

	/**
	 * This method let to launch a new match adding it to the matches.
	 * 
	 * @param date
	 */
	public synchronized void launchNewMatch(Date date) {
		int id = Server.getId();
		MatchHandler matchHandler = new MatchHandler(id, date, connectorInt);
		matches.add(matchHandler);
		matchHandler.start();
		DateFormat dateFormat = new SimpleDateFormat();
		try {
			connectorInt.writeToClient(
					"You launched a new match of Council Of Four on " + dateFormat.format(date) + " with ID " + id+"\n");
		} catch (RemoteException e) {
			logger.log(Level.INFO, "Error: could not write to client while launching a new match\n", e);
		}
	}

	/**
	 * This method is used to let the user to join into a match. To do it
	 * control if there are pending && not full matches in this case it add the
	 * new player into the match. If there are no available matches it launch a
	 * new match.
	 */
	public synchronized boolean joinMatch() {
		Iterator<MatchHandler> iterator = matches.iterator();
		MatchHandler matchInList;
		boolean joined = false;
		while (iterator.hasNext() && !joined) {
			matchInList = iterator.next();
			if (matchInList.isPending() && !(matchInList.isFull())) {
				matchInList.addPlayer(connectorInt);
				try {
					connectorInt.writeToClient(
							"You joined an already existing match still pending, with ID " + matchInList.getId()+"\n");
				} catch (RemoteException e) {
					logger.log(Level.INFO, "Error: could not write to client while trying to join a match\n", e);
				}
				joined = true;
			}
		}
		return joined;
	}

}
