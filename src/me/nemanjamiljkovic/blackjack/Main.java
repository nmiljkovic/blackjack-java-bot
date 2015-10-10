package me.nemanjamiljkovic.blackjack;

import java.net.Socket;

import me.nemanjamiljkovic.blackjack.Bot.Communicator;
import me.nemanjamiljkovic.blackjack.Bot.SimpleBot;

public class Main {
	public static void main(String[] args) {
		try {
			Socket socket = new Socket("localhost", 8000);
			
			Communicator.createAndRun(socket, new SimpleBot());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
