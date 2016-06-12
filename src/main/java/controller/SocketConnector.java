package controller;
import server.view.cli.ServerOutputPrinter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * al server servirà solo inviare pacchetti al client mentre il client invocherà le sendToServer in caso di
 */

public class SocketConnector implements ClientSideConnectorInt, ServerSideConnectorInt {


	private static final Logger logger= Logger.getLogger( SocketConnector.class.getName() );
    private Socket socket;
    private ObjectInputStream inputObjectFromClient;
    private PrintWriter outputStringToClient;
    private Scanner inputStringFromClient;
    private MatchHandler matchHandler;


    public SocketConnector(Socket socket) {
        this.socket=socket;
        try {
            outputStringToClient=new PrintWriter(socket.getOutputStream());
            inputObjectFromClient=new ObjectInputStream(socket.getInputStream());
            inputStringFromClient=new Scanner(socket.getInputStream());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while opening the output/input stream for 'socket'", e);
        }
        ServerOutputPrinter.printLine("[SERVER] New Socket connection established");
        receivePacketFromClient();
    }

    private void receivePacketFromClient() {
        while (true) {
            try {
                sendToServer((Packet) inputObjectFromClient.readObject());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setMatchHandler(MatchHandler matchHandler) {
        this.matchHandler=matchHandler;
    }

    @Override
    public void sendToClient(Packet packet) throws RemoteException {

    }


    @Override
    public void sendToServer(Packet packet) throws RemoteException {

    }
}
