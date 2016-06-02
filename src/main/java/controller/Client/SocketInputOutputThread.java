package controller.Client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class SocketInputOutputThread extends Thread {

	private Scanner input;
	private Scanner inputFromServer;
	private PrintWriter outputToServer;
	private String recieved;
	
	public SocketInputOutputThread(Socket socket) {
		 try {
			 inputFromServer = new Scanner(socket.getInputStream());
			 outputToServer=new PrintWriter(socket.getOutputStream(),true);
			 input=new Scanner(System.in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		while(true) {
			recieved=inputFromServer.nextLine();
			if(recieved.equals("*#*"))
				outputToServer.println(input.nextLine());
			else
				System.out.println(recieved);
		}
	}
}
