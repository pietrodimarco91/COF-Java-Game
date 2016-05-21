package controller;

/**
 * This class is used in case of Socket Connection, it handles the real interaction with the user.
 */
public class SocketConnector extends Connector{

    @Override
    public void writeToClient(String s) {

    }

    @Override
    public int reciveFromClient() {
        return 0;
    }

    @Override
    public String getUserId() {
        return null;
    }
}
