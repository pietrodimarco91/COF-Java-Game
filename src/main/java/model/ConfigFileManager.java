package model;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import exceptions.ConfigAlreadyExistingException;
import exceptions.UnexistingConfigurationException;

/**
 * This class is responsible for managing the configuration file, from which the
 * configurations of the board will be loaded and where they will be saved.
 * 
 * @author Riccardo
 *
 */
public class ConfigFileManager {

	private static final Logger logger = Logger.getLogger(ConfigFileManager.class.getName());
	/**
	 * The identifier of the configuration file
	 */
	private File file;

	/**
	 * The name of the file
	 */
	private final String filename = "config/board.config";

	/**
	 * The FileOutputStreamto to be used to open the file and correctly write in
	 * it.
	 */
	private FileOutputStream fos;

	/**
	 * The ObjectOutputStream to be used to serialize the ConfigObjects in the
	 * config fille
	 */
	private ObjectOutputStream outputStream;

	/**
	 * The current number of the configurations saved in the config file.
	 */
	private int numberOfConfigurations;

	public ConfigFileManager() {
		this.file = new File(filename);
		this.numberOfConfigurations = getCurrentNumberOfConfigurations();
		openFile();
	}

	/**
	 * This methods allows the player to create a new configuration and save it
	 * inside the config file
	 * 
	 * @param numberOfPlayers
	 * @param rewardTokenBonusNumber
	 * @param permitTileBonusNumber
	 * @param nobilityTrackBonusNumber
	 * @param linksBetweenCities
	 * @throws ConfigAlreadyExistingException
	 *             if the chosen configuration already exists inside the file
	 */
	public void createConfiguration(int numberOfPlayers, int rewardTokenBonusNumber, int permitTileBonusNumber,
			int nobilityTrackBonusNumber, int linksBetweenCities) throws ConfigAlreadyExistingException {
		if (configAlreadyExists(numberOfPlayers, rewardTokenBonusNumber, permitTileBonusNumber,
				nobilityTrackBonusNumber, linksBetweenCities)) {
			throw new ConfigAlreadyExistingException();
		}
		numberOfConfigurations++;
		try {
			outputStream.writeObject(new ConfigObject(numberOfConfigurations, numberOfPlayers, rewardTokenBonusNumber,
					permitTileBonusNumber, nobilityTrackBonusNumber, linksBetweenCities));
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error: cannot save the configuration in file ", e);

		}
	}

	/**
	 * This methods counts the already existing configurations in order to
	 * correctly initialize the corresponding parameter
	 * 
	 * @return the current number of existing configurations
	 */
	public int getCurrentNumberOfConfigurations() {
		if(!file.exists())
			return 0;
		ArrayList<ConfigObject> configurations = getConfigurations();
		if(configurations.isEmpty())
			return 0;
		ConfigObject lastConfig = configurations.get(configurations.size()-1);
		return lastConfig.getId();
	}

	/**
	 * This methods returns an ArrayList of ConfigObjects, representing the
	 * configurations already existing inside the config file
	 * 
	 * @return a list of the already existing configurations, that the player
	 *         can choose from
	 */
	public ArrayList<ConfigObject> getConfigurations() {
		ArrayList<ConfigObject> configurations = new ArrayList<ConfigObject>();

		try (FileInputStream fis = new FileInputStream(file);
				ObjectInputStream inputStream = new ObjectInputStream(fis)) {
			while (true) {
				configurations.add((ConfigObject) inputStream.readObject());
			}
		} catch (EOFException e) {
			logger.log(Level.FINEST, "Just read all configurations from " + filename, e);
			// just to break from the cycle and catch the end of file
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while reading the content of the file " + filename, e);
			closeFile();
			System.exit(0);
		} catch (ClassNotFoundException e) {
			logger.log(Level.SEVERE, "Error in handling the class type", e);
			closeFile();
			System.exit(0);
		}
		return configurations;
	}

	/**
	 * This method is used to check whether the desired configuration already
	 * exists inside the file or not.
	 * 
	 * @param numberOfPlayers
	 * @param rewardTokenBonusNumber
	 * @param permitTileBonusNumber
	 * @param nobilityTrackBonusNumber
	 * @param linksBetweenCities
	 * @return true if the specified configuration already exists, false
	 *         otherwise
	 */
	public boolean configAlreadyExists(int numberOfPlayers, int rewardTokenBonusNumber, int permitTileBonusNumber,
			int nobilityTrackBonusNumber, int linksBetweenCities) {
		ArrayList<ConfigObject> configs = getConfigurations();
		for (ConfigObject config : configs) {
			if (config.getLinksBetweenCities() == linksBetweenCities
					&& config.getNobilityTrackBonusNumber() == nobilityTrackBonusNumber
					&& config.getNumberOfPlayers() == numberOfPlayers
					&& config.getPermitTileBonusNumber() == permitTileBonusNumber
					&& config.getRewardTokenBonusNumber() == rewardTokenBonusNumber)
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * @throws UnexistingConfigurationException
	 */
	public ConfigObject getConfiguration(int id) throws UnexistingConfigurationException {
		ArrayList<ConfigObject> configurations = getConfigurations();
		for (ConfigObject configuration : configurations) {
			if (configuration.getId() == id)
				return configuration;
		}
		throw new UnexistingConfigurationException();
	}

	/**
	 * This method opens the necessary output streams to write inside the file
	 */
	public void openFile() {
		if (!file.exists()) {
			try {
				fos = new FileOutputStream(file);
				outputStream = new ObjectOutputStream(fos);
			} catch (FileNotFoundException e) {
				logger.log(Level.SEVERE, "Error while opening the file!", e);
				System.exit(0);
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error while reading the file!", e);
				System.exit(0);
			}
		} else {
			try {
				fos = new FileOutputStream(file, true);
				outputStream = new AppendableObjectOutputStream(fos);
			} catch (FileNotFoundException e) {
				logger.log(Level.SEVERE, "Error while opening the file!", e);
				System.exit(0);
			} catch (IOException e) {
				logger.log(Level.SEVERE, "Error while reading the file!", e);
				System.exit(0);
			}
		}

	}

	/**
	 * This method closes all the output streams.
	 */
	public void closeFile() {
		try {
			fos.close();
			outputStream.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, "Error while closing the file!", e);
			System.exit(0);
		}
	}

}
