package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketConnector implements ConnectorInt {

	private static final Logger logger= Logger.getLogger( SocketConnector.class.getName() );
	
    Socket socket;
    PrintWriter output;
    Scanner input;


    public SocketConnector(Socket socket) {
        this.socket=socket;
        try {
            output=new PrintWriter(socket.getOutputStream());
            input=new Scanner(socket.getInputStream());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while opening the output/input stream for 'socket'", e);
        }
        System.out.println("Socket connection established");
    }

    @Override
    public void writeToClient(String s) {
        output.println(s);
        output.flush();
    }

    @Override
    public int receiveIntFromClient() {
        return input.nextInt();
    }


    @Override
    public String receiveStringFromClient() {
        return input.nextLine();
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
    public String receiveStringFromServer() throws RemoteException {
        //bisogna gestirla internamente controllando lo stato del Server!
        return null;
    }
}
