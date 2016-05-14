package model;

/**
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
