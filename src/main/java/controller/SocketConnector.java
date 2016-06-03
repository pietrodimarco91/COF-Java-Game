package controller;

import client.actions.Action;
import server.view.cli.ServerOutputPrinter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketConnector implements ConnectorInt {

	private static final Logger logger= Logger.getLogger( SocketConnector.class.getName() );
	
    Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Action pendingAction;
    private Boolean yourTurn;


    public SocketConnector(Socket socket) {
        this.socket=socket;
        yourTurn=false;
        try {
            output=new ObjectOutputStream(socket.getOutputStream());
            input=new ObjectInputStream(socket.getInputStream());
            writeToClient("[SERVER] New Socket connection established");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while opening the output/input stream for 'socket'", e);
        }
        ServerOutputPrinter.printLine("[SERVER] New Socket connection established");
        reciveAction();

    }

    private void reciveAction() {
        Object received;
        while(true){
            try {
                received=input.readObject();
                if(received instanceof Action) {
                    if (checkAction(received)){
                        pendingAction = (Action) received;
                        ServerOutputPrinter.printLine(pendingAction.toString());
                    }

                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkAction(Object received) {
        //da fare controllando yourturn
        return true;
    }

    public Socket getSocket() {
    	return this.socket;
    }

    @Override
    public void writeToClient(String s) {

        try {
            output.writeObject(s);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int receiveIntFromClient() {
        this.writeToClient("*#*");
        try {
            return Integer.parseInt(input.readObject().toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @Override
    public String receiveStringFromClient() {
        try {
            return input.readObject().toString();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }



    @Override
    public void writeToServer(String s) throws RemoteException {
        //bisogna gestirla internamente controllando lo stato del Server!
    }

    @Override
    public int receiveIntFromServer() throws RemoteException {
        //bisogna gestirla internamente controllando lo stato del Server!
        return 0;
    }

    @Override
    public Action sendActionToServer(Action action) throws RemoteException {

        return pendingAction;
    }

    @Override
    public void sentTurn() throws RemoteException {
        yourTurn=true;
    }


}
