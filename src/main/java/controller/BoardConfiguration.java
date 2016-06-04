package controller;

import exceptions.ConfigAlreadyExistingException;
import exceptions.InvalidInputException;
import exceptions.UnexistingConfigurationException;
import model.ConfigFileManager;
import model.ConfigObject;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by pietro on 03/06/16.
 */


public class BoardConfiguration {

    private Player creator;

    private static final Logger logger = Logger.getLogger(MatchHandler.class.getName());

    private ConfigFileManager configFileManager;

    private int[] configParameters;

    private int numberOfPlayers;

    public BoardConfiguration(Player creator, int[] configParameters, int numberOfPlayers) {
        this.configParameters=configParameters;
        this.numberOfPlayers=numberOfPlayers;
        this.creator=creator;
        this.configFileManager=new ConfigFileManager();
        boardConfiguration(creator);
    }


    public void boardConfiguration(Player player) {
        boolean correctAnswer = false;
        int choice = 0;
        ConfigObject config;
        ConnectorInt playerConnector = player.getConnector();
        try {
            playerConnector.writeToClient("BOARD CONFIGURATION:\n");
        } catch (RemoteException e) {
            logger.log(Level.INFO, "Error: couldn't write to client", e);
        }

        while (!correctAnswer) {
            try {
                playerConnector
                        .writeToClient("1) Create a new board configuration\n2) Choose an existing configuration\n");

            } catch (RemoteException e) {
                logger.log(Level.INFO, "Error: couldn't write to client", e);
            }
            try {
                choice = playerConnector.receiveIntFromClient();
            } catch (RemoteException e) {
                logger.log(Level.INFO, "Error: couldn't receive from client", e);
            }
            if (choice != 1 && choice != 2) {
                try {
                    playerConnector.writeToClient("ERROR: incorrect input. Please retry\n");
                } catch (RemoteException e) {
                    logger.log(Level.INFO, "Error: couldn't write to client", e);
                }
            } else
                correctAnswer = true;
        }

        if (choice == 1) {
            newConfiguration(playerConnector);
        } else {
            if (configFileManager.getConfigurations().size() > 0) {
                try {
                    playerConnector.writeToClient("These are the currently existing configurations:\n");
                    ArrayList<ConfigObject> configurations = configFileManager.getConfigurations();
                    for (ConfigObject configuration : configurations) {
                        playerConnector.writeToClient(configuration.toString());
                    }
                    int id = -1;
                    boolean correctID = false;
                    while (!correctID) {
                        playerConnector.writeToClient("Choose a configuration by typing its ID\n");
                        id = playerConnector.receiveIntFromClient();
                        try {
                            config = configFileManager.getConfiguration(id);
                            correctID = true;
                            saveConfig(config);
                            this.numberOfPlayers = config.getNumberOfPlayers();
                            playerConnector.writeToClient("You've chosen the Board Configuration number " + id
                                    + ": Now waiting for new players...");
                        } catch (UnexistingConfigurationException e) {
                            playerConnector.writeToClient(e.printError());
                        }
                    }
                } catch (RemoteException e) {
                    logger.log(Level.INFO, "Error: couldn't write to client", e);
                }
            } else {
                try {
                    playerConnector.writeToClient("There aren't any configurations yet! Please create a new one\n");
                    newConfiguration(playerConnector);

                } catch (RemoteException e) {
                    logger.log(Level.INFO, "Error: couldn't write to client", e);
                }
            }
        }
    }

    /**
     * NEEDS CODE QUALITY ADJUSTEMENTS
     *
     * @param playerConnector
     */
    public void newConfiguration(ConnectorInt playerConnector) {
        String parameters = "";
        int numberOfPlayers = 0, linksBetweenCities = 0, rewardTokenBonusNumber = 0, permitTileBonusNumber = 0,
                nobilityTrackBonusNumber = 0;
        boolean stop = false;
        try {
            playerConnector.writeToClient(
                    "NEW CONFIGURATION:\nInsert the configuration parameters in this order, and each number must be separated by a space");
            playerConnector.writeToClient(
                    "Maximum number of players, Reward Token bonus number, Permit Tiles bonus number, Nobility Track bonus number, Maximum number of outgoing connections from each City");
        } catch (RemoteException e) {
            logger.log(Level.INFO, "Error: couldn't write to client", e);
        }

        while (!stop) {
            try {
                parameters = playerConnector.receiveStringFromClient();
            } catch (RemoteException e) {
                logger.log(Level.INFO, "Error: couldn't receive from client", e);
            }
            int[] par = new int[5];
            int i = 0;
            StringTokenizer tokenizer = new StringTokenizer(parameters, " ");
            try {
                while (tokenizer.hasMoreTokens()) {
                    par[i] = Integer.parseInt(tokenizer.nextToken());
                    i++;
                }
                numberOfPlayers = par[0];
                rewardTokenBonusNumber = par[1];
                permitTileBonusNumber = par[2];
                nobilityTrackBonusNumber = par[3];
                linksBetweenCities = par[4];
            } catch (NumberFormatException e) {
                try {
                    playerConnector.writeToClient("Error: Expected integers values! Retry!");
                } catch (RemoteException e1) {
                    logger.log(Level.INFO, "Error: couldn't write to client", e1);
                }
            }
            try {
                parametersValidation(numberOfPlayers, rewardTokenBonusNumber, permitTileBonusNumber,
                        nobilityTrackBonusNumber, linksBetweenCities);
                configFileManager.createConfiguration(numberOfPlayers, rewardTokenBonusNumber, permitTileBonusNumber,
                        nobilityTrackBonusNumber, linksBetweenCities);
                stop = true;
            } catch (InvalidInputException e) {
                try {
                    playerConnector.writeToClient(e.printError());
                } catch (RemoteException e1) {
                    logger.log(Level.INFO, "Error: couldn't write to client", e1);
                }
            } catch (ConfigAlreadyExistingException e) {
                try {
                    playerConnector.writeToClient(e.printError());
                } catch (RemoteException e1) {
                    logger.log(Level.INFO, "Error: couldn't write to client", e1);
                }
            }
        }
        saveConfig(numberOfPlayers, rewardTokenBonusNumber, permitTileBonusNumber, nobilityTrackBonusNumber,
                linksBetweenCities);
        this.numberOfPlayers = numberOfPlayers;
        try {
            playerConnector
                    .writeToClient("Board correctly generated with selected parameters! Now we're about to start...");
        } catch (RemoteException e1) {
            logger.log(Level.INFO, "Error: couldn't write to client", e1);
        }
    }

    /**
     * Checks whether the specified parameters respect the rules or not.
     *
     * @param numberOfPlayers
     * @param rewardTokenBonusNumber
     * @param permitTileBonusNumber
     * @param nobilityTrackBonusNumber
     * @param linksBetweenCities
     * @throws InvalidInputException
     */
    public void parametersValidation(int numberOfPlayers, int rewardTokenBonusNumber, int permitTileBonusNumber,
                                     int nobilityTrackBonusNumber, int linksBetweenCities) throws InvalidInputException {
        if (numberOfPlayers < 2 || numberOfPlayers > 8)
            throw new InvalidInputException();
        if ((rewardTokenBonusNumber < 1 || rewardTokenBonusNumber > 3)
                || (permitTileBonusNumber < 1 || permitTileBonusNumber > 3)
                || (nobilityTrackBonusNumber < 1 || nobilityTrackBonusNumber > 3))
            throw new InvalidInputException();
        if (linksBetweenCities < 2 && linksBetweenCities > 4)
            throw new InvalidInputException();
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

    public void saveConfig(int numberOfPlayers, int rewardTokenBonusNumber, int permitTileBonusNumber,
                           int nobilityTrackBonusNumber, int linksBetweenCities) {
        configParameters[0] = numberOfPlayers;
        configParameters[1] = rewardTokenBonusNumber;
        configParameters[2] = permitTileBonusNumber;
        configParameters[3] = nobilityTrackBonusNumber;
        configParameters[4] = linksBetweenCities;
    }
}
