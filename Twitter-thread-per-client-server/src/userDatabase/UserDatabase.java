package userDatabase;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;
import threadPerClientServer.protocol.ServerProtocol;
import utilities.IntegerHolder;
import utilities.FollowerIdCollection.FollowerIdCollection;



public class UserDatabase {

	
	private ConcurrentHashMap<String, User> _users;
	private static UserDatabase instance= null;
	
	
	protected UserDatabase(){
		_users= new ConcurrentHashMap<String, User>();
		User server= new User("server", "U83R 1337 H4x0R");
		_users.put("server", server);
	}
	
	
	public synchronized boolean registerUser(String clientName, String passcode){
		if (_users.get(clientName) != null)
			return false;
		_users.put(clientName, new User(clientName, passcode));
		return true;
	}
	
	
	public boolean verifyUserExistance(String clientName){
		return (_users.get(clientName) != null);
	}
	
	
	@SuppressWarnings("rawtypes")
	public synchronized boolean logClientIn(String clientName, String passcode, ServerProtocol protocol){
		User userFound= _users.get(clientName);
		if ((userFound != null) && (userFound.checkPassword(passcode)) && (!userFound.isLoggedIn())){
			_users.get(clientName).setProtocol(protocol);
			System.out.println("User " + clientName + " logged in");
			return true;
		}
		return false;
	}
	
	
	public synchronized void logClientOut(String clientName){
		_users.get(clientName).closeConnection();
		System.out.println("User " + clientName + " logged out");
	}
	
	
	public boolean followUser(String followingUser, String userToFollow, String id){
		User userFound= _users.get(userToFollow);
		if ((userFound != null) && (userFound.checkIfCanAddFollowingUser(followingUser, id)) && (_users.get(followingUser).checkIfCanFollowUser(userToFollow, id))){
			userFound.addFollowingUser(followingUser, id);
			_users.get(followingUser).followUser(userToFollow, id);
			return true;
		}
		return false;
	}
	

	public boolean unfollowUser(String followingUser, String id){
		String userToUnfollow= _users.get(followingUser).getUserById(id);
		User userFound= _users.get(userToUnfollow);
		if ((userFound != null) && (userFound.checkIfCanRemoveFollowingUser(followingUser, id))){
			return ((_users.get(followingUser).unfollowUser(userToUnfollow, id)) && (userFound.removeFollowingUser(followingUser, id)));
		}
		return false;
	}
	
	
	public boolean AddMessageToUser(String username, String message){
		User foundUser= _users.get(username);
		if (foundUser == null)
			return false;
		FollowerIdCollection followingUsers= foundUser.getFollowersForThisUser();
		for (int i= 0; i < followingUsers.getNumberOfFollowers(); i++) {
			String followingUser= followingUsers.getFollowerByLocation(i);
			User follower= _users.get(followingUser);
			follower.addMessage(message, username);
		}
		return true;
	}
	
	
	public String getMessageFromUser(String username){
		User foundUser= _users.get(username);
		return foundUser.getMessage();
	}
	
	
	public synchronized String getListOfAllUsers(boolean online){
		StringBuilder userList= new StringBuilder();
		for (User userFound : _users.values()){
			String username= userFound.getUsername();
	        if ((online) && (userFound.isLoggedIn())){
	        	if (!username.equals("server"))
		        	userList.append(username).append(" ");
	        }
	        else
	        	if (!username.equals("server"))
		        	userList.append(username).append(" ");
		}
		System.out.println(userList.toString());
		return userList.toString();
	}
	
	
	public static UserDatabase getUsers(){
		if (instance == null)
			instance= new UserDatabase();
		return instance;
	}
	
	
	public void incTweetingCountOfUser(String username){
		User userfound= _users.get(username);
		if (userfound != null)
			userfound.incTweetingCount();
	}
	
	
	public void incMentionsCountOfUser(String username){
		User userfound= _users.get(username);
		if (userfound != null)
			userfound.incMentionsCount();
	}
	
	
	public void incMentionsByOthersCountOfUser(String username){
		User userfound= _users.get(username);
		if (userfound != null)
			userfound.incMentionsByOthersCount();
	}
	
	
	public synchronized Vector<String> getTopStats(){
		Vector<String> finalAnswer= new Vector<String>();
		String userWithMostTweets= ""; 
		IntegerHolder mostTweets= new IntegerHolder(-1);
		String userWithMostMentions= ""; 
		IntegerHolder mostMentions= new IntegerHolder(-1);
		String userWithMostMentionsByOthers= "";
		IntegerHolder mostMentionsByOthers= new IntegerHolder(-1);
		String userWithMostFollowers= "";
		IntegerHolder mostFollowers= new IntegerHolder(-1);
		for (User userFound : _users.values()){
			String username= userFound.getUsername();
	        int tweets= userFound.getTweetingCount();
	        int mentions= userFound.getMentionsCount();
	        int mentionsByOthers= userFound.getMentionsByOthersCount();
	        int followers= userFound.getFollowersCount();
	        userWithMostTweets= helper(username, userWithMostTweets, mostTweets, tweets);
	        userWithMostMentions= helper(username, userWithMostMentions, mostMentions, mentions);
	        userWithMostMentionsByOthers= helper(username, userWithMostMentionsByOthers, mostMentionsByOthers, mentionsByOthers);
	        userWithMostFollowers= helper(username, userWithMostFollowers, mostFollowers, followers);
		}
	    finalAnswer.add(userWithMostFollowers);
	    finalAnswer.add("" + mostFollowers.getValue());
	    finalAnswer.add(userWithMostTweets);
	    finalAnswer.add("" + mostTweets.getValue());
	    finalAnswer.add(userWithMostMentions);
	    finalAnswer.add(userWithMostMentionsByOthers);
	    return finalAnswer;
	}
	
	
	private String helper(String username, String maxName, IntegerHolder max, int cur){
		if (!username.equals("server") && (cur > max.getValue())){
         	max.setValue(cur);
         	return username;
    	}
		return maxName;
	}
	
	
}
