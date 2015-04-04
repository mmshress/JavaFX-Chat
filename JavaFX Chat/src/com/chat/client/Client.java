package com.chat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Platform;
import javafx.collections.ObservableList;

public class Client {
	/* The Socket of the Client */
	private Socket clientSocket;
	private BufferedReader serverToClientReader;
	private PrintWriter clientToServerWriter;
	private ObservableList<String> chatLog;
	
	public Client(String hostName, int portNumber, String name) {
		try {
			/* Try to establish a connection to the server */
			clientSocket = new Socket(hostName, portNumber);
			/* Instantiate writers and readers to the socket */
			serverToClientReader = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			clientToServerWriter = new PrintWriter(
					clientSocket.getOutputStream());
			/* Send name data to the server */
			clientToServerWriter.println(name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void writeToServer(String input){
		clientToServerWriter.println(input);
	}
	public void run(){
		/* Infinite loop to update the chat log from the server*/
		try{
			
			while(true){
				final String inputFromServer = serverToClientReader.readLine();
				Platform.runLater(new Runnable(){
					public void run(){
						chatLog.add(inputFromServer);
					}
				});
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
}
