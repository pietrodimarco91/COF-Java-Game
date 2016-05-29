package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.ConfigFileManager;

/**
 * Created by pietro on 25/05/16.
 */
public class SocketWaitConnectionThread extends Thread {

	private static final Logger logger = Logger.getLogger(SocketWaitConnectionThread.class.getName());
	private ServerSocket welcomeSocket;
	private Connector connector;
	private Object lock;

	public SocketWaitConnectionThread(Object lock, int port) {

		try {
			welcomeSocket = new ServerSocket(port);
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while createing ServerSocket 'welcomeSocket' on port " + port, e);
		}

	}

	@Override
	public void run() {
		try {
			connector = new SocketConnector(welcomeSocket.accept());
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while createing SocketConnector 'connector' after welcomeSocket.accept()",
					e);
		}
		synchronized (lock) {
			lock.notifyAll();
		}
	}

	public Connector getConnector() {
		return connector;
	}
}
