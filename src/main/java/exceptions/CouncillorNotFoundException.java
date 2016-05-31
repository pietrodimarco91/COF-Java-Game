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
		return "Errore: il consigliere cercato non è disponibile nel pool dei consiglieri... Si consiglia di cambiare colore";
	}
}
