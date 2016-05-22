package controller;

/**
 * This Class handles the interactions with the Client making the difference between RMI and Socket connections.
 */
public class ConnectorHandler{

    /**
     *This attribute performs the interactions with the Client.
     */
    Connector connector;
    /**
     *The port where Server are listening to the requests from the Clients.
     */
    int port;

    
    public ConnectorHandler(int port) {
        this.port=port;
    }

    /**
     *NEED TO BE IMPLEMENTED
     */
    public Connector getConnector() {
        //Test
        return null;
    }

}
