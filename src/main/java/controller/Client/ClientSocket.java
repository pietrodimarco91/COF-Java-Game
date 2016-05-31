package controller.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Created by pietro on 30/05/16.
 */
public class ClientSocket {

    private PrintWriter output;
    private Scanner input;
    private Socket socket;
    private static final int port=2000;


    public ClientSocket(){
        try {
            socket=new Socket("127.0.0.1",port);
            output=new PrintWriter(socket.getOutputStream());
            input=new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
