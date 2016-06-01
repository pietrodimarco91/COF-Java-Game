package controller.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import client.view.cli.ClientOutputPrinter;

public class SockeOutputThread extends Thread {

	private Scanner input;
	
	public SockeOutputThread(Socket socket) {
		 try {
			input = new Scanner(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true) {
			ClientOutputPrinter.printLine(input.nextLine());
		}
	}
}
