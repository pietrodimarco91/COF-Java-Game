package controller;

/**
 * Created by pietro on 25/05/16.
 */
public class RMIWaitConnectionThread extends Thread {

	Connector connector;
	Object lock;

	public RMIWaitConnectionThread(Object lock) {
		this.lock = lock;
	}

	@Override
	public void run() {
		synchronized (lock) {
			lock.notify();
		}
	}

	public Connector getConnector() {
		return connector;
	}
}
