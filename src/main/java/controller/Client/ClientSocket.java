package controller.Client;

import java.io.IOException;
import java.net.Socket;

/**
 * Created by pietro on 30/05/16.
 */
public class ClientSocket {

    private Socket socket;
    private SockeOutputThread socketOutputThread;
    private SocketInputThread socketInputThread;
    private static final String address="localhost";
    private static final int port=2000;


    public ClientSocket(){
        try {
            socket=new Socket(address,port);
            socketOutputThread=new SockeOutputThread(socket);
            socketInputThread=new SocketInputThread(socket);
            socketOutputThread.start();
            socketInputThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
