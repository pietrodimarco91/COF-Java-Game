package model;

/**
 * Created by Gabriele Bressan on 13/05/16.
 * Enumaration about colors of the single Councillor
 */

public enum CouncillorColors {
	PINK, ORANGE, BLUE, PURPLE, BLACK, WHITE, MULTICOLOR;

	private static CouncillorColors councillorColors;

	public static String getRandomColor() {
		councillorColors = CouncillorColors.values()[(int) (Math.random() * CouncillorColors.values().length)];
		return councillorColors.toString();
	}

}
