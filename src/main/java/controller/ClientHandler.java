package controller;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

/**
 *This Class let every user to create a new match
 * or to join in a pending match that already exist.
 */
public class ClientHandler implements Runnable {

    private ArrayList<MatchHandler> matches;

    /**
     *This attribute handles every interaction with the user.
     */
    private Connector connector;

    public ClientHandler(Connector connector, ArrayList<MatchHandler> matches) {
        this.matches=matches;
        this.connector =connector;
    }

    /**
     *This is the run() method of the Thread.
     *It asks to the User if he wants to join in a pending match or to launch a new match.
     */
    @Override
    public void run() {
        try {
            connector.writeToClient("Do you want to:\n1)join into a match\n2)create a new match?");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        try {
            switch (connector.receiveIntFromClient()){
                case 1:
                    this.joinMatch();
                    break;
                case 2:
                    this.launchNewMatch();
                    break;
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    /**
     * This method let to launch a new match adding it to the matches.
     */
    public void launchNewMatch() {
        Date date = new Date();
        MatchHandler matchHandler=new MatchHandler(Server.getId(), date, connector);
        matches.add(matchHandler);
        matchHandler.start();
    }

    /**
     * This method is used to let the user to join into a match.
     * To do it control if there are pending && not full matches in this case it add the new player into the match.
     * If there are no available matches it launch a new match.
     */
    public void joinMatch() {
        Iterator<MatchHandler> iterator=matches.iterator();
        MatchHandler matchInList;
        Boolean joined=false;
        while(iterator.hasNext()){
            matchInList=iterator.next();
            if(matchInList.isPending() && !(matchInList.isFull())){
                matchInList.addPlayer(connector);
                joined=true;
                break;
            }
        }
        if (!joined){
            this.launchNewMatch();
        }
    }



}
