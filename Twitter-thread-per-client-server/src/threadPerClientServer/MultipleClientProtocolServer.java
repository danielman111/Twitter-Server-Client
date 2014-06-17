package threadPerClientServer;

import java.io.*;
import java.net.*;
import java.util.Vector;
import threadPerClientServer.protocol.MessageProtocolFactory;
import threadPerClientServer.protocol.ServerProtocolFactory;
import threadPerClientServer.tokenizer.StompTokenizerFactory;
import threadPerClientServer.tokenizer.TokenizerFactory;
import userDatabase.UserDatabase;
import utilities.BooleanHolder;
import utilities.STOMP.StompFrame;
import utilities.Statistics;



public class MultipleClientProtocolServer<T> implements Runnable {

	private ServerSocket _serverSocket;
	private int _listenPort;
	private TokenizerFactory<T> _tokenizerFactory;
	private ServerProtocolFactory<T> _protocolFactory;
	private BooleanHolder _serverStop;
	private Vector<ConnectionHandler<T>> _allConnections;

	
	public MultipleClientProtocolServer(int port, TokenizerFactory<T> t, ServerProtocolFactory<T> p) {
		_serverSocket= null;
		_listenPort= port;
		_protocolFactory= p;
		_tokenizerFactory= t;
		_serverStop= new BooleanHolder();
		_allConnections= new Vector<ConnectionHandler<T>>();
	}

	

	public void run(){
		try {
			_serverSocket = new ServerSocket(_listenPort);
			System.out.println("Listening...");
		} catch (IOException e) {
			System.out.println("Cannot listen on port " + _listenPort);
		}
		UserDatabase.getUsers();
		Statistics statistics= Statistics.getStatisticsObject();
		while (!_serverStop.getValue()){
			try {
				ConnectionHandler<T> newConnection = new ConnectionHandler<T>(_tokenizerFactory.create(), _serverSocket.accept(), _protocolFactory.create(), _serverStop, this);
				_allConnections.add(newConnection);
				new Thread(newConnection).start();
			} catch (IOException e){
				if (!_serverStop.getValue())
					System.out.println("Failed to accept on port " + _listenPort);
				else
					System.out.println("Server recieved shutdown command");
			}
		}
		statistics.shutDown();
	}
	
	
	// Closes the connection
	public void close() {
		if (!_serverStop.getValue()){
			_serverStop.setTrue();
			for (ConnectionHandler<T> currentConnection : _allConnections) {
				currentConnection.stopServer();
			}
			try {
				_serverSocket.close();
			} catch (IOException e) {}
		}
	}

	
	public static void main(String[] args) throws IOException {
		// Get port
		int port = Integer.decode(args[0]).intValue();
		MultipleClientProtocolServer<StompFrame> server = new MultipleClientProtocolServer<StompFrame>(port, new StompTokenizerFactory(), new MessageProtocolFactory());
		Thread serverThread = new Thread(server);
		serverThread.start();
		try {
			serverThread.join();
		} catch (InterruptedException e){
			System.out.println("Server stopped");
		}
	}
	

}
