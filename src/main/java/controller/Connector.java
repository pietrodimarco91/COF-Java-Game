package controller;

/**
 * Created by pietro on 21/05/16.
 */
public abstract class Connector {
    public abstract void writeToClient(String s);

    public abstract int receiveIntFromClient();
    
    public abstract int receiveStringFromClient();//To add UML Scheme

    public abstract String getUserId();
}
