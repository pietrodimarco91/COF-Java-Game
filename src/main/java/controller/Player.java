package controller;

import java.util.*;

/**
 * 
 */
public class Player {

   

    /**
     * 
     */
    private String userName;

    /**
     * 
     */
    private String password;

    /**
     * 
     */
    private int matchesWon;

    /**
     * 
     */
    private int rageQuits;
    /**
     * 
     */
    private Connector playerConnector;// To add UML scheme

	/**
	 * Default constructor
	 */
	public Player(Connector playerConnector) {
	this.playerConnector=playerConnector;
	}
    /**
     * @return
     */
    public String getUserName() {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    public String getPassword() {
        // TODO implement here
        return "";
    }

    /**
     * @return
     */
    public int getMatchesWon() {
        // TODO implement here
        return 0;
    }

    /**
     * @return
     */
    public int getRageQuits() {
        // TODO implement here
        return 0;
    }
    /**
     * @return
     */
    public Connector getConnector() { //Da aggiunfere UML
        // TODO implement here
        return this.playerConnector;
    }

}