package client.view.cli;

import java.io.PrintStream;

/**
 * This abstract class will be used to print on the console everything needed by the client
 * @author Riccardo
 *
 */
public abstract class ClientOutputPrinter {
	private static PrintStream printer=new PrintStream(System.out,true);
	
	/**
	 * @param toPrint the string to print on the console
	 */
	public static void printLine(String toPrint) {
		printer.println(toPrint);
	}
	
	/**
	 * 
	 * @param num the integer number to print on the console
	 */
	public static void printLine(int num) {
		printer.println(num);
	}
	
	/**
	 * This method will automatically invoke the toString() method of the specified object
	 * @param object the object on which the toString() method should be invoked
	 */
	public static void printObject(Object object) {
		printer.println(object);
	}	
}
