package protocol;

import reactor.ConnectionHandler;

/**
 * A protocol that describes the behabiour of the server.
 */
public interface ServerProtocol<T> {

    /**
     * processes a message
     * @param msg the message to process
     * @return the reply that should be sent to the client, or null if no reply needed
     */
    T processMessage(T msg);

    
	public void setConnectionHandler(ConnectionHandler<T> cH);
    
    
    /**
     * detetmine whether the given message is the termination message
     * @param msg the message to examine
     * @return true if the message is the termination message, false otherwise
     */
    boolean isEnd(T msg);
    
    
	public String formatIntoMessage(String destination, String id, String message);


	public void addToOutBuffer(String sentUsername, String id, String message);

}
