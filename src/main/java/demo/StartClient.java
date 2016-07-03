package demo;

import client.controller.Client;

/**
 * Created by pietro on 29/05/16.
 */
public class StartClient {

	private static Client client;

	public static void main(String[] args) {
		client = new Client();
		client.start();
	}
}
