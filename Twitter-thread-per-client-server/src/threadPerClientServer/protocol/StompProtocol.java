package threadPerClientServer.protocol;

import java.util.Vector;

import threadPerClientServer.ConnectionHandler;
import userDatabase.UserDatabase;
import utilities.STOMP.*;
import utilities.Statistics;

public class StompProtocol implements ServerProtocol<StompFrame> {

	private String _username;
	private UserDatabase _users;
	private ConnectionHandler<StompFrame> _cH;

	public StompProtocol() {
		_username= "";
		_cH= null;
		_users= UserDatabase.getUsers();
	}

	
	public void setConnectionHandler(ConnectionHandler<StompFrame> cH){
		_cH= cH;
	}
	
	
	public String processMessage(StompFrame msg) {
		String answer= "";
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
			answer= msg.getWholeSTOMPMessage(); 
		return answer;
	}

	
	private String doWork(ConnectFrame msg){
		if (!_username.equals(""))
			return SendError(msg, "you are already logged in");
		String username= msg.getUsername();
		String pass= msg.getPasscode();
		if (_users.verifyUserExistance(username)){
			if (_users.logClientIn(username, pass, this)){//_cH
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
			_users.logClientIn(username, pass, this);//_cH
			connectAndEmptyUserMessageQueue();
		}
		return null;
	}
	
	
	private String doWork(SendFrame msg){
		String loginCheck= checkLoggedIn(msg); 
		if (loginCheck != "")
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
		boolean didSend= _users.AddMessageToUser(destination, message);
		if (!didSend)
			return SendError(msg, "User " + destination + " does not exist");
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


	private String doWork(SubscribeFrame msg){
		String loginCheck= checkLoggedIn(msg); 
		if (!loginCheck.equals(""))
			return loginCheck;
		String destination= msg.getDestination();
		destination= destination.substring(destination.lastIndexOf('/') + 1, destination.length());
		String id= msg.getId();
		if (_users.followUser(_username, destination, id))
			return MessageFrame.formatIntoMessage("", "", "following " + destination);
		return SendError(msg, "cannot follow this user. either the user does not exist or you are already following this user");
	}
	
	
	private String doWork(UnsubscribeFrame msg){
		String loginCheck= checkLoggedIn(msg); 
		if (!loginCheck.equals(""))
			return loginCheck;
		String id= msg.getId();
		if (_users.unfollowUser(_username, id))
			return MessageFrame.formatIntoMessage("", "", "unfollowing " + id);;
		return SendError(msg, "cannot unfollow this user. either the user does not exist, you are not following this user, or you tried to unfollow yourself");
	}
	
	
	private String doWork(DisconnectFrame msg){
		String loginCheck= checkLoggedIn(msg);
		if (!loginCheck.equals(""))
			return loginCheck;
		_users.logClientOut(_username);
		_username= "";
		return ReceiptFrame.formatIntoReceiptMessage(msg.getReceiptId());
	}
	
	
	private String checkLoggedIn(StompFrame msg){
		if (_username.equals(""))
			return SendError(msg, "cannot preform action, you are not logged in");
		return "";
	}
	

	private String SendError(StompFrame msg, String error) {
		return ErrorFrame.formatIntoErrorMessage(msg.getWholeSTOMPMessage(), error);
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
	
	
	public boolean isEnd(StompFrame msg) {
		boolean isEnd= msg.isDisconnectFrame();
		return isEnd;
	}	
	
	
	private void connectAndEmptyUserMessageQueue(){
		_cH.addToOutBuffer(ConnectedFrame.formatIntoConnectedMessage());
		String message= _users.getMessageFromUser(_username);
		while (!message.equals("")){
			_cH.addToOutBuffer(message);
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


	@Override
	public String formatIntoMessage(String destination, String id, String message) {
		String formattedMessage= MessageFrame.formatIntoMessage(destination, id, message);
		return formattedMessage;
	}
	
	
	@Override
	public void addToOutBuffer(String destination, String id, String message) {
		String formattedMessage= formatIntoMessage(destination, id, message);
		_cH.addToOutBuffer(formattedMessage);
	}
	
}