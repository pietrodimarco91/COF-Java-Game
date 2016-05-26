package controller.Client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

/**
 * Created by pietro on 24/05/16.
 */
public class ConnectorRMIClient extends UnicastRemoteObject implements ConnectorRMIClientInt {

    protected ConnectorRMIClient() throws RemoteException {
    }

    @Override
    public void tell(String s) {
        System.out.println(s);

    }

    @Override
    public int receiveInt() {
        Scanner input = new Scanner(System.in);
        return input.nextInt();
    }

    @Override
    public String receiveString() {
        Scanner input = new Scanner(System.in);
        return input.nextLine();
    }
}
