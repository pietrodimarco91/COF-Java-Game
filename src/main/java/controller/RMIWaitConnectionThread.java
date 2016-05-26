package controller;

/**
 * Created by pietro on 25/05/16.
 */
public class RMIWaitConnectionThread extends Thread {

    Connector connector;

    public RMIWaitConnectionThread(Object lock) {

    }

    @Override
    public void run() {
    }

    public Connector getConnector() {
        return connector;
    }
}
