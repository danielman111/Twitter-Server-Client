package threadPerClientServer;

import java.net.*;
import java.io.*;

import threadPerClientServer.protocol.ServerProtocol;
import threadPerClientServer.tokenizer.Tokenizer;
import utilities.BooleanHolder;


public class ConnectionHandler<T> implements Runnable {

	private BufferedReader _in;
	private PrintWriter _out;
	private Socket _clientSocket;
	private ServerProtocol<T> _protocol;
	private Tokenizer<T> _tokenizer;
	private BooleanHolder _serverStop;
	private boolean _isAlive;
	private MultipleClientProtocolServer<T> _server;


	public ConnectionHandler(Tokenizer<T> tokenizer, Socket acceptedSocket, ServerProtocol<T> p, BooleanHolder serverStop, MultipleClientProtocolServer<T> server) {
		_in = null;
		_out = null;
		_isAlive= true;
		_clientSocket = acceptedSocket;
		_protocol = p;
		_tokenizer= tokenizer;
		_serverStop= serverStop;
		_server= server;
		System.out.println("Accepted connection from client!");
		System.out.println("The client is from: " + acceptedSocket.getInetAddress() + ":" + acceptedSocket.getPort());
	}

	
	public void run() {
		try {
			initialize();
		} catch (IOException e) {
			System.out.println("Error in initializing I/O");
		}
		try {
			process();
		} catch (IOException e) {
			System.out.println("Error in I/O");
		} 
		System.out.println("Connection closed - bye bye...");
		close();
	}


	public void process() throws IOException {
		T incomingMessage= _tokenizer.getMessage(_in);
		while ((!_serverStop.getValue()) && (!_protocol.isEnd(incomingMessage))){
			String response = _protocol.processMessage(incomingMessage);
			if (response != null){
				addToOutBuffer(response);
			}
			incomingMessage= _tokenizer.getMessage(_in);
		}
		String response;
		if (_serverStop.getValue())
			response= _tokenizer.serverClosed();
		else
			response= _protocol.processMessage(incomingMessage);
		if (response != null){
			addToOutBuffer(response);
		}
	}


	public void initialize() throws IOException {
		// Initialize I/O
		_in = new BufferedReader(new InputStreamReader(_clientSocket.getInputStream(),"UTF-8"));
		_out = new PrintWriter(new OutputStreamWriter(_clientSocket.getOutputStream(),"UTF-8"), true);
		System.out.println("I/O initialized");
		_protocol.setConnectionHandler(this);
	}

	
	// Closes the connection
	public void close() {
		if (_isAlive){
			try {
				_clientSocket.close();
				if (_in != null){
					_clientSocket.shutdownInput();
					_in.close();
				}
				if (_out != null){
					_clientSocket.shutdownOutput();
					_out.close();
				}
			} catch (IOException e){
				if (!_serverStop.getValue())
					System.out.println("Exception in closing I/O");
			}
		}
	}
	
	
	public synchronized void stopServer(){
		String answer= _tokenizer.serverClosed();
		addToOutBuffer(answer);
		close();
		_isAlive= false;
		_server.close();
	}
	
	
	public synchronized void addToOutBuffer(String message){
		System.out.println("Sending the following message: ");
		System.out.println(message);
		_out.println(message);
	}

}


