package userDatabase;

import java.util.LinkedList;
import threadPerClientServer.protocol.ServerProtocol;
import utilities.FollowerIdCollection.FollowerIdCollection;


public class User {

	
	private final String _username;
	private final String _password;
	private FollowerIdCollection _usersFollowingMe;
	private FollowerIdCollection _usersIFollow;
	private LinkedList<MessageHolder> _messages;
	@SuppressWarnings("rawtypes")
	private ServerProtocol _protocol;
	private int _numOfTweets;
	private int _numOfMentionsByOthers;
	private int _numOfMentions;
	
	
	public User(String username, String passcode){
		_username= username;
		_password= passcode;
		_usersFollowingMe= new FollowerIdCollection();
		_usersIFollow= new FollowerIdCollection();
		if (!username.equals("server"))
			_usersFollowingMe.addFollower(_username, "0");
		_messages= new LinkedList<MessageHolder>();
		_protocol= null;
		_numOfTweets= 0;
		_numOfMentionsByOthers= 0;
		_numOfMentions= 0;
	}
	
	
	protected String getUsername(){
		return _username;
	}
	
	
	protected boolean checkPassword(String passcode){
		return (_password.equals(passcode));
	}
	
	@SuppressWarnings("rawtypes")
	protected synchronized void setProtocol(ServerProtocol protocol){
		_protocol= protocol;
	}
	
	
	protected synchronized void closeConnection(){
		_protocol= null;
	}
	
	
	
	protected boolean checkIfCanFollowUser(String user, String id){
		return (_usersIFollow.findFollowerIdByName(user).equals(""));
	}
	
	
	protected synchronized void followUser(String user, String id){
		_usersIFollow.addFollower(user, id);
	}
	
	
	protected synchronized boolean unfollowUser(String user, String id){
		if ((user.equals("")) || ((user.equals(_username))))
				return false;
		_usersIFollow.removeFollower(id);
		return true;
	}
	
	
	protected boolean checkIfCanAddFollowingUser(String user, String id){
		return (_usersFollowingMe.findFollowerIdByName(user).equals(""));
	}
	
	
	protected synchronized void addFollowingUser(String user, String id){
		_usersFollowingMe.addFollower(user, id);
	}
	
	
	protected boolean checkIfCanRemoveFollowingUser(String user, String id){
		return ((!_usersFollowingMe.findFollowerNameById(id).equals("")) && (!_usersFollowingMe.findFollowerIdByName(user).equals("")));
	}
	
	
	protected synchronized boolean removeFollowingUser(String user, String id){
		return _usersFollowingMe.removeFollower(id);
	}
	
	
	protected String getUserById(String id){
		return _usersIFollow.findFollowerNameById(id);
	}
	
	
	protected synchronized void addMessage(String message, String sentUsername){
		String id= _usersIFollow.findFollowerIdByName(sentUsername);
		if (_protocol != null){
			_protocol.addToOutBuffer(sentUsername, id, message);
		}
		else
			_messages.add(new MessageHolder(sentUsername, id, message));
	}
	
	
	protected synchronized String getMessage(){
		try {
			MessageHolder message= _messages.remove();
			return (_protocol.formatIntoMessage(message.getTopic(), message.getId(), message.getMessage()));
		} catch (Exception e){
			return "";
		}
	}
	
	
	protected FollowerIdCollection getFollowersForThisUser(){
		return _usersFollowingMe;
	}
	
	
	protected boolean isLoggedIn(){
		if (_username.equals("server"))
			return true;
		return (_protocol != null);
	}
	
	
	protected synchronized void incTweetingCount(){
		_numOfTweets++;
	}
	
	
	protected synchronized void incMentionsCount(){
		_numOfMentions++;
	}
	
	
	protected synchronized void incMentionsByOthersCount(){
		_numOfMentionsByOthers++;
	}
	
	
	protected int getTweetingCount(){
		return _numOfTweets;
	}
	
	
	protected int getMentionsCount(){
		return _numOfMentions;
	}
	
	
	protected int getMentionsByOthersCount(){
		return _numOfMentionsByOthers;
	}
	
	
	protected int getFollowersCount(){
		return _usersFollowingMe.getNumberOfFollowers();
	}
	

}
