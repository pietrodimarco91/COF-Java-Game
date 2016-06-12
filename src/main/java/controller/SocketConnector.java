package controller;

import client.actions.Action;
import server.view.cli.ServerOutputPrinter;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SocketConnector implements ConnectorInt {


	private static final Logger logger= Logger.getLogger( SocketConnector.class.getName() );
    private Socket socket;
    private ObjectInputStream inputObjectFromClient;
    private PrintWriter outputStringToClient;
    private Scanner inputStringFromClient;
    private Action pendingAction;
    private boolean actionSent;
    private boolean yourTurn;
    private boolean configSent;
    private ArrayList<Integer> pendingConfig;


    public SocketConnector(Socket socket) {
        this.socket=socket;
        actionSent=false;
        configSent=false;
        yourTurn=false;
        try {
            outputStringToClient=new PrintWriter(socket.getOutputStream());
            inputObjectFromClient=new ObjectInputStream(socket.getInputStream());
            inputStringFromClient=new Scanner(socket.getInputStream());
            writeToClient("[SERVER] New Socket connection established");
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error while opening the output/input stream for 'socket'", e);
        }
        ServerOutputPrinter.printLine("[SERVER] New Socket connection established");
        receiveObjectFromClient();

    }

    private void receiveObjectFromClient() {
        Object received =new Object();
        while(true) {
            try {
                received = inputObjectFromClient.readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if (received instanceof Action && yourTurn) {
                pendingAction = (Action) received;
                actionSent=true;
            }
            if (received instanceof ArrayList) {
                pendingConfig = (ArrayList<Integer>) received;
                configSent=true;
            }
        }
    }


    @Override
    public void writeToClient(String s) {
        outputStringToClient.println(s);
    }

    @Override
    public int receiveIntFromClient() {
        this.writeToClient("*#*");
        return Integer.parseInt(inputStringFromClient.nextLine());
    }


    @Override
    public String receiveStringFromClient() {
        return inputStringFromClient.nextLine();
    }

    @Override
    public void setTurn(boolean value) {
        yourTurn=value;
    }

    @Override
    public Action getAction() {
        while(!actionSent){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        actionSent=false;
        return pendingAction;
    }


    @Override
    public void waitStart() {
        //THIS METHOD IS ONLY USED BY RMI
    }

    @Override
    public void setMatchStarted() {
        writeToClient("START");
    }

    @Override
    public boolean checkCreator() {
        //THIS METHOD IS ONLY USED BY RMI
        return false;
    }


    @Override
    public void writeToServer(String s) throws RemoteException {
        //THIS METHOD IS ONLY USED BY RMI
    }

    @Override
    public int receiveIntFromServer() throws RemoteException {
        //THIS METHOD IS ONLY USED BY RMI
        return 0;
    }

    @Override
    public void sendActionToServer(Action action) throws RemoteException {
        //THIS METHOD IS ONLY USED BY RMI
    }

    @Override
    public void sendConfigurationToServer(ArrayList<Integer> config) throws RemoteException {
        //THIS METHOD IS ONLY USED BY RMI
    }


    @Override
    public void setCreator(boolean b) {
        if(b){
            writeToClient("CREATOR");
        }else
        writeToClient("NOT CREATOR");
    }

    @Override
    public ArrayList<Integer> getBoardConfiguration() {
        while(!configSent){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return pendingConfig;
    }
}
