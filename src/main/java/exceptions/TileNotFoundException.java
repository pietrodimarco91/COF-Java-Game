package exceptions;

public class TileNotFoundException extends Exception {
	
	public String showError() {
		return "Tile Not Found! The Tile you specified wasn't found!";
	}
}
