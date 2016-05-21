package controller;

/**
 * This Class handles the interactions with the Client making the difference between RMI and Socket connections.
 */
public class ConnectorHandler implements Runnable{

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

    @Override
    public void run() {
        
    }

    /**
     *NEED TO BE IMPLEMENTED
     */
    public ConnectorHandler getConnector() {
        return null;
    }
    /**
     *NEED TO BE IMPLEMENTED
     */
    public void writeToClient(String s) {

    }
    /**
     *NEED TO BE IMPLEMENTED
     */
    public int reciveFromClient() {
        return 0;
    }
    /**
     *NEED TO BE IMPLEMENTED
     */
    public String getUserId() {
        return "null";
    }
}
