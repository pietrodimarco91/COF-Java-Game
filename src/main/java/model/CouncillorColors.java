package model;

import java.util.ArrayList;

/**
 * Created by Gabriele Bressan on 13/05/16. Enumaration about colors of the
 * single Councillor
 */

public enum CouncillorColors {
	PINK, ORANGE, BLUE, PURPLE, BLACK, WHITE, MULTICOLOR;

	private static CouncillorColors councillorColors;

	public static String getRandomColor() {
		councillorColors = CouncillorColors.values()[(int) (Math.random() * CouncillorColors.values().length)];
		return councillorColors.toString();
	}

	/**
	 * This method is used to get the ArrayList of strings corresponding to the
	 * colors of the councillors EXCEPT the multicolor.
	 * 
	 * @return
	 */
	public static ArrayList<String> getCouncillorColors() {
		ArrayList<String> colorValues = new ArrayList<>();
		CouncillorColors[] values = CouncillorColors.values();
		for (int i = 0; i < values.length; i++) {
			if (!(values[i].equals(MULTICOLOR)))
				colorValues.add(String.valueOf(values[i]));
		}
		return colorValues;
	}
	
	public static ArrayList<String> getPoliticCardsColors() {
		ArrayList<String> colorValues = new ArrayList<>();
		CouncillorColors[] values = CouncillorColors.values();
		for (int i = 0; i < CouncillorColors.values().length; i++) {
				colorValues.add(String.valueOf(values[i]));
		}
		return colorValues;
	}


}
