package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * This class is used in case of Socket Connection, it handles the real interaction with the user.
 */
public class SocketConnector extends Connector{

    Socket socket;
    PrintWriter output;
    Scanner input;


    public SocketConnector(Socket socket) {
        this.socket=socket;
        try {
            output=new PrintWriter(socket.getOutputStream());
            input=new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("accesso effettuato da :"+socket.getInetAddress());
    }

    @Override
    public void writeToClient(String s) {
        output.println(s);
        output.flush();
    }

    @Override
    public int receiveIntFromClient() {
        return 0;
    }


    @Override
    public String reciveStringFromClient() {
        return input.nextLine();
    }


}
