package controller;


import java.io.IOException;
import java.net.ServerSocket;

/**
 * This Class handles the interactions with the Client making the difference between RMI and Socket connections.
 */
public class ConnectorHandler{

    /**
     *The port where Server are listening to the requests from the Clients.
     */
    ServerSocket welcomeSocket;

    
    public ConnectorHandler(int port) {
        try {
            welcomeSocket=new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *This method return a specific connector
     */
    public Connector getConnector(){

        System.out.println("in attesa di connessione...");
        //if Socket Connection

        try {
            return new SocketConnector(welcomeSocket.accept());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
