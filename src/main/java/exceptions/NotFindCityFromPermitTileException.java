package exceptions;

public class NotFindCityFromPermitTileException extends Exception{
	public String showError() {
		return "Invalid City Name! Please choose correct ID Permit Tile with correct City Name!";
	}

}
