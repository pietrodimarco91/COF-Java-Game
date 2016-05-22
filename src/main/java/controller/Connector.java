package controller;

/**
 * Created by pietro on 21/05/16.
 */
public abstract class Connector {
    public abstract void writeToClient(String s);

    public abstract int reciveIntFromClient();
    
    public abstract int reciveStringFromClient();//To add UML Scheme

    public abstract String getUserId();
}
