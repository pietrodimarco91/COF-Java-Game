package exceptions;

public class MapSetupException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void printError() {
		System.out.println("Error: couldn't setup the map...");
	}
}
