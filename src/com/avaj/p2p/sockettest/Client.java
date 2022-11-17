package com.avaj.p2p.sockettest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	public static void main(String[] args) {
		try {
			Scanner sc = new Scanner(System.in);
			while (true) {
				System.out.print("> ");
				String input = sc.nextLine();
				if (input.equals("q")) {
					break;
				}

				Socket socket = new Socket();
				socket.connect(new InetSocketAddress("127.0.0.1", Integer.parseInt(input)));
				System.out.println("Connected to 25570 port");
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
