package exceptions;

public class CityNotFoundFromPermitTileException extends Exception{
	public String showError() {
		return "Invalid City Name! Please choose correct ID Permit Tile with correct City Name!";
	}

}
