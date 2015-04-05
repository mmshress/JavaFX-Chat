package com.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Client implements Runnable {
	/* The Socket of the Client */
	private Socket clientSocket;
	private BufferedReader serverToClientReader;
	private PrintWriter clientToServerWriter;
	private String name;
	public ObservableList<String> chatLog;

	public Client(String hostName, int portNumber, String name) throws UnknownHostException, IOException {
		
			/* Try to establish a connection to the server */
			clientSocket = new Socket(hostName, portNumber);
			/* Instantiate writers and readers to the socket */
			serverToClientReader = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			clientToServerWriter = new PrintWriter(
					clientSocket.getOutputStream(), true);
			chatLog = FXCollections.observableArrayList();
			/* Send name data to the server */
			this.name = name;
			clientToServerWriter.println(name);

		
	}

	public void writeToServer(String input) {
		clientToServerWriter.println(name + " : " + input);
	}

	public void run() {
		/* Infinite loop to update the chat log from the server */
		while (true) {
			try {

				final String inputFromServer = serverToClientReader.readLine();
				Platform.runLater(new Runnable() {
					public void run() {
						chatLog.add(inputFromServer);
					}
				});

			} catch (SocketException e) {
				Platform.runLater(new Runnable() {
					public void run() {
						chatLog.add("Error in server");
					}

				});
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
