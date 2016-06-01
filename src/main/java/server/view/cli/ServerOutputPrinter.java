package server.view.cli;

import java.io.PrintStream;

/**
 * This abstract class will be used to print on the console all the needed and regular informations
 * @author Riccardo
 *
 */
public abstract class ServerOutputPrinter {
	
	private static PrintStream printer=new PrintStream(System.out,true);
	
	/**
	 * @param toPrint the string to print on the console
	 */
	public static void printLine(String toPrint) {
		printer.println(toPrint);
	}
}
