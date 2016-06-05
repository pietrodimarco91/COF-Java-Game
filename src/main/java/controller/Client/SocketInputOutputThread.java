package controller.Client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import client.actions.EngageAssistantAction;

public class SocketInputOutputThread extends Thread {

	private Scanner input;
	private Scanner inputFromServer;
	private ObjectOutputStream outputToServer;
	//private PrintWriter outputToServer;
	private String received;

	public SocketInputOutputThread(Socket socket) {
		try {
			inputFromServer = new Scanner(socket.getInputStream());
			outputToServer = new ObjectOutputStream(socket.getOutputStream());
			//outputToServer = new PrintWriter(socket.getOutputStream(), true);
			input = new Scanner(System.in);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		while (true) {
			received = inputFromServer.nextLine();
			if (received.equals("*#*")) {
				try {
					outputToServer.writeObject(new EngageAssistantAction("QUICK"));
					outputToServer.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				// outputToServer.println(input.nextLine());
			} else
				System.out.println(received);
		}
	}
}
