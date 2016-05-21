package controller;

/**
 * Created by pietro on 21/05/16.
 */
public abstract class Connector {
    public abstract void writeToClient(String s);

    public abstract int reciveFromClient();

    public abstract String getUserId();
}
