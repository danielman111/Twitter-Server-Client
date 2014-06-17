package protocol;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.util.Vector;
import userDatabase.UserDatabase;
import utilities.STOMP.*;
import utilities.Statistics;
import reactor.ConnectionHandler;

/**
 * a simple implementation of the server protocol interface
 */
public class StompProtocol implements AsyncServerProtocol<StompFrame> {

	private boolean _shouldClose = false;
	private boolean _connectionTerminated = false;
	private String _username= "";
	private UserDatabase _users = UserDatabase.getUsers();
	private ConnectionHandler<StompFrame> _cH= null;

	
	public void setConnection(ConnectionHandler<StompFrame> cH){
		_cH= cH;
	}
	
	/**
	 * processes a message<BR>
	 * this simple interface prints the message to the screen, then composes a simple
	 * reply and sends it back to the client
	 *
	 * @param msg the message to process
	 * @return the reply that should be sent to the client, or null if no reply needed
	 */
	@Override
	public StompFrame processMessage(StompFrame msg) {        
		if (this._connectionTerminated) {
			return null;
		}
		StompFrame answer;
		if (msg.getSTOMPType().equals("CONNECT"))
			answer= doWork((ConnectFrame)msg);
		else if (msg.getSTOMPType().equals("SEND"))
			answer= doWork((SendFrame)msg);
		else if (msg.getSTOMPType().equals("SUBSCRIBE"))
			answer= doWork((SubscribeFrame)msg);
		else if (msg.getSTOMPType().equals("UNSUBSCRIBE"))
			answer= doWork((UnsubscribeFrame)msg);
		else if (msg.getSTOMPType().equals("DISCONNECT"))
			answer= doWork((DisconnectFrame)msg);
		else
			answer= msg; 
		return answer;
	}
	
	private StompFrame doWork(ConnectFrame msg){
		if (!_username.equals(""))
			return SendError(msg, "you are already logged in");
		String username= msg.getUsername();
		String pass= msg.getPasscode();
		if (_users.verifyUserExistance(username)){
			if (_users.logClientIn(username, pass, this)){
				_username= username;
				connectAndEmptyUserMessageQueue();
			}
			else {
				return SendError(msg, "incorrect login");
			}
		}
		else {
			_username= username;
			_users.registerUser(username, pass);
			_users.logClientIn(username, pass, this);
			connectAndEmptyUserMessageQueue();
		}
		return null;
	}
	
	
	private StompFrame doWork(SendFrame msg){
		StompFrame loginCheck= checkLoggedIn(msg);
		if (loginCheck != null)
			return loginCheck;
		long startTime= System.currentTimeMillis();
		String destination= msg.getDestination();
		destination= destination.substring(destination.lastIndexOf('/') + 1, destination.length());
		String message= msg.getMessage();
		if (destination.equals("server")){
			message= serverRecievedSendFrame(msg);
			if (message.equals("")){
				Statistics.getStatisticsObject().timeItTookToTweet(System.currentTimeMillis() - startTime);
				return null;
			}
		}
		Vector<String> otherUsersToMessage= findAtSignsInMessage(message);
		_users.AddMessageToUser(destination, message);
		if (!destination.equals("server"))
			_users.incTweetingCountOfUser(destination);
		for (int i= 0; i < otherUsersToMessage.size(); i++) {
			String otherUsername= otherUsersToMessage.get(i);
			_users.AddMessageToUser(otherUsername, message);
			_users.incMentionsCountOfUser(destination);
			_users.incMentionsByOthersCountOfUser(otherUsername);
		}
		Statistics.getStatisticsObject().timeItTookToTweet(System.currentTimeMillis() - startTime);
		return null;
	}


	private StompFrame doWork(SubscribeFrame msg){
		StompFrame loginCheck= checkLoggedIn(msg);
		if (loginCheck != null)
			return loginCheck;
		String destination= msg.getDestination();
		destination= destination.substring(destination.lastIndexOf('/') + 1, destination.length());
		String id= msg.getId();
		if (_users.followUser(_username, destination, id))
			return MessageFrame.factory("", "", "following " + destination);
		return SendError(msg, "cannot follow this user. either the user does not exist or you are already following this user");
	}
	
	
	private StompFrame doWork(UnsubscribeFrame msg){
		StompFrame loginCheck= checkLoggedIn(msg);
		if (loginCheck != null)
			return loginCheck;
		String id= msg.getId();
		if (_users.unfollowUser(_username, id))
			return MessageFrame.factory("", "", "unfollowing " + id);;
		return SendError(msg, "cannot unfollow this user. either the user does not exist, you are not following this user, or you tried to unfollow yourself");
	}
	
	
	private StompFrame doWork(DisconnectFrame msg){
		StompFrame loginCheck= checkLoggedIn(msg);
		if (loginCheck != null)
			return loginCheck;
		_users.logClientOut(_username);
		_username= "";
		connectionTerminated();
		return ReceiptFrame.factory(msg.getReceiptId());
	}
	
	
	private StompFrame checkLoggedIn(StompFrame msg){
		if (_username.equals(""))
			return SendError(msg, "cannot preform action, you are not logged in");
		return null;
	}
	

	private StompFrame SendError(StompFrame msg, String error) {
		return ErrorFrame.factory(msg.getWholeSTOMPMessage(), error);
	}

	
	private Vector<String> findAtSignsInMessage(String message) {
		Vector<String> userNames= new Vector<String>();
		char atSign= '@';
		char spaceSign= ' ';
		int locationOfAtSign= message.indexOf(atSign);
		String tempMessage= message;
		while (locationOfAtSign > -1){
			String messageAfterTheAtSign= tempMessage.substring(locationOfAtSign + 1);
			int locationOfNextSpace= messageAfterTheAtSign.indexOf(spaceSign);
			String userToMessage;
			if (locationOfNextSpace < 0)
				userToMessage= messageAfterTheAtSign;
			else
				userToMessage= messageAfterTheAtSign.substring(0, locationOfNextSpace);
			userNames.add(userToMessage);
			messageAfterTheAtSign= messageAfterTheAtSign.substring(locationOfNextSpace + 1);
			locationOfAtSign= messageAfterTheAtSign.indexOf(atSign);
			tempMessage= messageAfterTheAtSign;
		}
		return userNames;
	}	
	
	
	private void connectAndEmptyUserMessageQueue(){
		StringBuilder sb = new StringBuilder(ConnectedFrame.formatIntoConnectedMessage());
		ByteBuffer outData= null;
		try {
			outData = Charset.forName("UTF-8").newEncoder().encode(CharBuffer.wrap(sb));
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}
		_cH.addOutData(outData);
		String message= _users.getMessageFromUser(_username);
		while (!message.equals("")){
			sb = new StringBuilder(message);
			outData= null;
			try {
				outData = Charset.forName("UTF-8").newEncoder().encode(CharBuffer.wrap(sb));
			} catch (CharacterCodingException e) {
				e.printStackTrace();
			}
			_cH.addOutData(outData);
			message= _users.getMessageFromUser(_username);
		}
	}
	
	
	private String serverRecievedSendFrame(SendFrame msg){
		String reply= "";
		if (msg.getMessage().equals("stats\n"))
			Statistics.getStatisticsObject().getStats();
		else if (msg.getMessage().equals("stop\n"))
			_cH.stopServer();
		else if (msg.getMessage().equals("clients online\n"))
			reply= _users.getListOfAllUsers(true);
		else if (msg.getMessage().equals("clients\n"))
			reply= _users.getListOfAllUsers(false);
		return reply;
	}

	/**
	 * detetmine whether the given message is the termination message
	 *
	 * @param msg the message to examine
	 * @return true if termination is needed
	 */
	@Override
	public boolean isEnd(StompFrame msg) {
		boolean isEnd= msg.isDisconnectFrame();
		return isEnd;
	}
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setConnectionHandler(ConnectionHandler cH) {
		_cH= cH;
	}
	

	/**
	 * Is the protocol in a closing state?.
	 * When a protocol is in a closing state, it's handler should write out all pending data, 
	 * and close the connection.
	 * @return true if the protocol is in closing state.
	 */
	@Override
	public boolean shouldClose() {
		return this._shouldClose;
	}
	

	/**
	 * Indicate to the protocol that the client disconnected.
	 */
	@Override
	public void connectionTerminated() {
		this._connectionTerminated = true;
	}
	
	
	@Override
	public String formatIntoMessage(String destination, String id, String message) {
		String formattedMessage= MessageFrame.formatIntoMessage(destination, id, message);
		return formattedMessage;
	}
	
	
	@Override
	public void addToOutBuffer(String destination, String id, String message){
		MessageFrame formattedMessage= MessageFrame.factory(destination, id, message);
		_cH.addOutData(formattedMessage);
	}


}
