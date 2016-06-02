package controller.Client;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by pietro on 30/05/16.
 */
public class ClientSocket {

    private static final String address="localhost";
    private static final int port=2000;
    private Socket socket;
    private SocketInputOutputThread socketInputOutputThread;


    public ClientSocket(){
        try {
            socket=new Socket(address,port);
            socketInputOutputThread =new SocketInputOutputThread(socket);
            socketInputOutputThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
