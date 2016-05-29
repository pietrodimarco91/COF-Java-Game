package controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

import model.ConfigFileManager;

/**
 * This Class handles the interactions with the Client making the difference between RMI and Socket connections.
 */
public class ConnectorHandler{

	private static final Logger logger= Logger.getLogger( ConnectorHandler.class.getName() );
    /**
     *The port where Server are listening to the requests from the Clients.
     */
    ServerSocket welcomeSocket;
    ConnectorRMIServer connectorRMIServer;

    
    public ConnectorHandler(int port) {
        try {
            welcomeSocket=new ServerSocket(port);
        } catch (IOException e) {
            logger.log(Level.SEVERE,"Error while creating ServerSocket 'welcomeSocket' on port"+port,e);
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
