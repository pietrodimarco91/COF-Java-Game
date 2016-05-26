package controller;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by pietro on 25/05/16.
 */
public class SocketWaitConnectionThread extends Thread {

    private ServerSocket welcomeSocket;
    private Connector connector;
    private Object lock;


    public SocketWaitConnectionThread(Object lock, int port) {
        this.connector=connector;
        try {
            welcomeSocket=new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        try {
            connector = new SocketConnector(welcomeSocket.accept());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.notifyAll();
    }

    public Connector getConnector() {
        return connector;
    }
}
