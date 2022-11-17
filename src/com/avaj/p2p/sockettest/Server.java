package com.avaj.p2p.sockettest;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String[] args) {
		System.out.println("Server");

		try {
			ServerSocket server = new ServerSocket(25570);

			while (true) {
				System.out.println("Waiting...");
				Socket client = server.accept();
				System.out.println("Client: " + client.getPort());
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
