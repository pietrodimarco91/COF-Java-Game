package exceptions;

/**
 * This exception is thrown when a Councillor of a specified color doesn't exist
 * inside the CouncillorsPool
 * 
 * @author Riccardo
 *
 */
public class CouncillorNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public String showError() {
		return "The councillor you are looking for isn't available in the Councillors Pool... try with a different color";
	}
}
