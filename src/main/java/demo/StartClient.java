package demo;

import client.controller.Client;

public class StartClient {

	private static Client client;

	public static void main(String[] args) {
		client = new Client(args);
		client.start();
	}
}
