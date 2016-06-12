package controller;

import exceptions.UnexistingConfigurationException;
import model.ConfigFileManager;
import model.ConfigObject;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by pietro on 03/06/16.
 */


public class BoardConfiguration {

    private static final Logger logger = Logger.getLogger(MatchHandler.class.getName());
    private Player creator;
    private ConfigFileManager configFileManager;

    private int[] configParameters;

    private int numberOfPlayers;


    public BoardConfiguration(Player creator, int[] configParameters,int numberOfPlayers) {
        this.numberOfPlayers=numberOfPlayers;
        this.configParameters=configParameters;
        this.creator=creator;
        this.configFileManager=new ConfigFileManager();
        boardConfiguration(creator);
    }


    public void boardConfiguration(Player player) {
        boolean proceed=false;
        ArrayList<Integer> boardParameter;
        ConfigObject config;
        ConnectorInt playerConnector = player.getConnector();
        try {
            playerConnector.writeToClient("BOARD CONFIGURATION:\n");
            playerConnector.writeToClient("These are the currently existing configurations:\n");
            ArrayList<ConfigObject> configurations = configFileManager.getConfigurations();
            for (ConfigObject configuration : configurations) {
                playerConnector.writeToClient(configuration.toString());
            }
            while(!proceed) {
                boardParameter=playerConnector.getBoardConfiguration();
                if(boardParameter.size()==1){//returned the id
                    try {
                        config=configFileManager.getConfiguration(boardParameter.get(0));
                        playerConnector.writeToClient("You've chosen the Board Configuration number " + boardParameter.get(0)
                                + ": Now waiting for new players...");
                        saveConfig(config);
                        this.numberOfPlayers=config.getNumberOfPlayers();
                        proceed=true;
                    } catch (UnexistingConfigurationException e) {
                        playerConnector.writeToClient(e.printError());
                    }
                }else {
                    saveConfig(boardParameter);
                    this.numberOfPlayers=boardParameter.get(0);
                    proceed = true;
                }
            }
        } catch (RemoteException e) {
            logger.log(Level.INFO, "Error: couldn't write to client", e);
        }
    }

    /**
     * NEEDS JAVADOC
     *
     * @param config
     */
    public void saveConfig(ConfigObject config) {
        configParameters[0] = config.getNumberOfPlayers();
        configParameters[1] = config.getRewardTokenBonusNumber();
        configParameters[2] = config.getPermitTileBonusNumber();
        configParameters[3] = config.getNobilityTrackBonusNumber();
        configParameters[4] = config.getLinksBetweenCities();
    }
    public void saveConfig(ArrayList<Integer> parameters) {
        configParameters[0] = parameters.get(0);
        configParameters[1] = parameters.get(1);
        configParameters[2] = parameters.get(2);
        configParameters[3] = parameters.get(3);
        configParameters[4] = parameters.get(4);
    }
}
