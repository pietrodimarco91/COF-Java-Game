package controller;

import model.Board;
import server.view.cli.ServerOutputPrinter;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by pietro on 14/06/16.
 */
public class GameInitializator extends Thread {

    private static final Logger logger = Logger.getLogger(GameInitializator.class.getName());
    private int gameStatus;
    private ArrayList<Player> players;
    private int minimumNumberOfPlayers;
    private int id;
    private Board board;
    private int[] configParameters;
    private int numberOfPlayers;

    public GameInitializator(int id, Board board, int[] configParameters, int numberOfPlayers, int gameStatus, ArrayList<Player> players, int minumumNumberOfPlayers) {
        this.id = id;
        this.board = board;
        this.configParameters = configParameters;
        this.numberOfPlayers = numberOfPlayers;
        this.gameStatus=gameStatus;
        this.players=players;
        this.minimumNumberOfPlayers=minumumNumberOfPlayers;
    }

    @Override
    public void run() {
        gameInitialization();
    }

    private void gameInitialization() {
        waitingForPlayers();
        countdown();
        setDefinitiveNumberOfPlayers();
        boardInitialization();
    }

    private void waitingForPlayers() {
        ServerOutputPrinter.printLine("[MATCH " + id + "] Currently waiting for players...");
        sendMessageToClient("[Match ID: " + id + "] Currently waiting for players...",players.get(0).getId());
        while (players.size() < this.minimumNumberOfPlayers) {
            // Match starts with at least two players
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "ERROR TRYING TO SLEEP!", e);
            }
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "ERROR TRYING TO SLEEP!", e);
        }
    }

    /**
     * NEEDS JAVADOC
     */
    public void countdown() {
        for (int i = 20; i > 0; i--) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.log(Level.SEVERE, "ERROR TRYING TO SLEEP!", e);
            }
            for (Player player : players) {
                try {
                    player.getConnector().sendToClient(new Packet("MATCH STARTING IN: " + i + "\n"));
                } catch (RemoteException e) {
                    logger.log(Level.FINEST, "Error: couldn't write to client\n", e);
                }
            }
        }
        gameStatus=2;
    }

    /**
     * NEEDS JAVADOC
     */
    public void setDefinitiveNumberOfPlayers() {
        configParameters[0] = this.players.size();
        this.numberOfPlayers=this.players.size();
    }

    /**
     * This method is invoked to initialize the board before a match starts. The
     * parameters are set by the first player that joins the match.
     */
    public void boardInitialization() {
        board = new Board(configParameters[0], configParameters[1], configParameters[2], configParameters[3],
                configParameters[4]);
    }

    public void sendMessageToClient(String s, int playerId) {
        String message="[SERVER] "+s;
        try {
            players.get(playerId).getConnector().sendToClient(new Packet(message));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
