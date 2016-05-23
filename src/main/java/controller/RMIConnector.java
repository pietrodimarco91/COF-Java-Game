package controller;

/**
 * This class is used in case of RMI Connection, it handles the real interaction with the user.
 */
public class RMIConnector extends Connector {

    public RMIConnector(int port) {

    }

    @Override
    public void writeToClient(String s) {

    }

    @Override
    public int receiveIntFromClient() {
        return 0;
    }

    @Override
    public String reciveStringFromClient() {
        return "null";
    }

}
