package com.chat.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/* Thread Class for each incoming client */
public class ClientThread implements Runnable{
	
	/* The socket of the client */
	private Socket clientSocket;
	/* Server class from which thread was called */
	private Server baseServer;
	private BufferedReader incomingMessageReader;
	private PrintWriter outgoingMessageWriter;
	/* The name of the client */
	
	
	private String clientName;
	public ClientThread(Socket clientSocket, Server baseServer) {
		this.setClientSocket(clientSocket);
		this.baseServer = baseServer;
		try {
			/*
			 * Reader to get all incoming messages that the client passes to the
			 * server
			 */
			incomingMessageReader = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));
			/* Writer to write outgoing messages from the server to the client */
			outgoingMessageWriter = new PrintWriter(
					clientSocket.getOutputStream());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void run() {
		try {
			this.clientName = getClientNameFromNetwork();
			String inputToServer;
			while(true){
				inputToServer = incomingMessageReader.readLine();
				baseServer.writeToAllSockets(inputToServer);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void writeToServer(String input){
		outgoingMessageWriter.println(input);
	}
	public String getClientNameFromNetwork() throws IOException {
		/*
		 * Get the name of the client, which is the first data transaction the
		 * server-client make
		 */
		return incomingMessageReader.readLine();
	}
	public String getClientName(){
		return this.clientName;
	}
	public Socket getClientSocket() {
		return clientSocket;
	}
	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
}
