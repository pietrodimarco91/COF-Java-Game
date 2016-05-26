package map;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import exceptions.ConfigAlreadyExistingException;
import model.ConfigFileManager;
import model.ConfigObject;

public class TestConfigFile {

	@Test
	public void test() {
		ConfigFileManager fileManager = new ConfigFileManager();
		try {
			fileManager.createConfiguration(5, 2, 2, 2, 2);
			fileManager.createConfiguration(7, 2, 2, 2, 2);
			fileManager.createConfiguration(7, 2, 2, 2, 2);
			fileManager.createConfiguration(6, 2, 2, 2, 2);
		} catch (ConfigAlreadyExistingException e) {
			e.printError();
		}
		assertEquals(2, fileManager.getConfigurations().size());

		try {
			fileManager.createConfiguration(5, 5, 5, 5, 5);
		} catch (ConfigAlreadyExistingException e) {
			e.printError();
		}
		assertEquals(3, fileManager.getConfigurations().size());
		try {
			fileManager.createConfiguration(8, 2, 2, 2, 2);
			fileManager.createConfiguration(4, 2, 2, 2, 2);
			fileManager.createConfiguration(3, 2, 2, 2, 2);
			fileManager.createConfiguration(5, 2, 2, 2, 2);
		} catch (ConfigAlreadyExistingException e) {
			e.printError();
		}
		assertEquals(6, fileManager.getConfigurations().size());
		try {
			fileManager.createConfiguration(8, 2, 2, 2, 2);
			fileManager.createConfiguration(4, 2, 2, 2, 2);
			fileManager.createConfiguration(3, 2, 2, 2, 2);
			fileManager.createConfiguration(5, 2, 2, 2, 2);
		} catch (ConfigAlreadyExistingException e) {
			e.printError();
		}
		assertEquals(6, fileManager.getConfigurations().size());
		ArrayList<ConfigObject> configs =fileManager.getConfigurations();
		for(ConfigObject config:configs) {
			System.out.println(config.toString());
		}
		fileManager.closeFile();
	}
}
