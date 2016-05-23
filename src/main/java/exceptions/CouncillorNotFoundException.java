package exceptions;

public class CouncillorNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void printError() {
		System.out.println(
				"Errore: il consigliere cercato non è disponibile nel pool dei consiglieri... Si consiglia di cambiare colore");
	}
}
