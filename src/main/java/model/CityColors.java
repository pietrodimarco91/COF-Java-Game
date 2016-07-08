package model;

import java.util.Random;

/**
 * 
 */
public enum CityColors {
	GOLD, SILVER, BRONZE, STEEL;

	/**
	 * Each color has a different probability: GOLD->40%,SILVER->30%,BRONZE->20%,STEEL->10%
	 * @return
	 */
	public static String random() {
		Random random = new Random();
		int n=(int) (random.nextFloat()*100);
		if(n>=0&&n<10)
			return String.valueOf(STEEL);
		else if(n>=10&&n<30)
			return String.valueOf(BRONZE);
		else if(n>=30&&n<60)
			return String.valueOf(SILVER);
		else
			return String.valueOf(GOLD);
	}
}