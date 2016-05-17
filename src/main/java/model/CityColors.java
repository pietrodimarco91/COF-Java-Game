package model;

import java.util.Random;

/**
 * 
 */
public enum CityColors {
	GOLD, SILVER, BRONZE, STEEL;

	public static String random() {
		return String.valueOf(values()[new Random().nextInt(values().length)]);
	}
}