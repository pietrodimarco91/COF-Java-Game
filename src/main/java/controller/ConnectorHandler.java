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
    ConnectorRMIServer connectorRMIServer;

    
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
    public Connector getConnector() throws IOException {
        SocketConnector decision = new SocketConnector(welcomeSocket.accept());
        decision.writeToClient("Do you want to use:1)RMI\n2)Socket?");
        switch (decision.receiveIntFromClient()){
            case 1:
                return decision;
            case 2:
                return new RMIConnector();
        }
        return null;
    }

    private int choice() {
        return 0;
    }

}
