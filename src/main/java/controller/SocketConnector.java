package controller;

import server.view.cli.ServerOutputPrinter;

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
    MatchHandler matchHandler;


    public SocketConnector(Socket socket) {
        this.socket=socket;
        try {
            output=new PrintWriter(socket.getOutputStream(),true);
            input=new Scanner(socket.getInputStream());
            writeToClient("[SERVER] New Socket connection established");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while opening the output/input stream for 'socket'", e);
        }
       ServerOutputPrinter.printLine("[SERVER] New Socket connection established");
    }
    
    public Socket getSocket() {
    	return this.socket;
    }

    @Override
    public void writeToClient(String s) {
        output.println(s);
    }

    @Override
    public int receiveIntFromClient() {
        this.writeToClient("*#*");
        return Integer.parseInt(input.nextLine());
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
