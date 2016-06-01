package controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by pietro on 01/06/16.
 */
public class ServerSideRMIConnector extends UnicastRemoteObject implements ServerSideRMIInt{
    /**
     * Creates and exports a new UnicastRemoteObject object using an
     * anonymous port.
     * <p>
     * <p>The object is exported with a server socket
     * created using the {@link RMISocketFactory} class.
     *
     * @throws RemoteException if failed to export object
     * @since JDK1.1
     * @param a
     */
    protected ServerSideRMIConnector(ClientSideRMIInt a) throws RemoteException {
    }
}
