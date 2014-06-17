package threadPerClientServer.protocol;

import threadPerClientServer.ConnectionHandler;

public interface ServerProtocol<T> {


	public String processMessage(T msg);

	public void setConnectionHandler(ConnectionHandler<T> cH);
	
	public boolean isEnd(T msg);

	public void addToOutBuffer(String destination, String id, String message);

	public String formatIntoMessage(String destination, String id, String message);

}