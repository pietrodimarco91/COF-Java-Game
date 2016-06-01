package controller.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketInputThread extends Thread {
	
	private PrintWriter output;
	
	private Scanner input;
	
	public SocketInputThread(Socket socket) {
		try {
			output=new PrintWriter(socket.getOutputStream(),true);
			input=new Scanner(System.in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true) {
			output.println(input.nextLine());
		}
	}
}
