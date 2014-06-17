package utilities;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import userDatabase.UserDatabase;
import utilities.STOMP.StompFrame;


public class Statistics{

	private int _maxTweetsPer5Seconds;
	private Timer _clock;
	private int _numOfClockTicks;
	private int _totalTweetsPer5Seconds;
	private int _tweetsPer5Seconds;
	private int _totalTimeToPassATweet;
	private int _tweetCount;
	private boolean _stillRunning;
	private static Statistics instance= null;
	private static final int STATISTICS_INTERVAL= 5000;
	
	
	protected Statistics(){
		_maxTweetsPer5Seconds= 0;
		_totalTimeToPassATweet= 0;
		_tweetCount= 0;
		_numOfClockTicks= 0;
		_totalTweetsPer5Seconds= 0;
		_tweetsPer5Seconds= 0;
		_stillRunning= true;
		_clock= new Timer();
		_clock.schedule(new TimerTask() {
			@Override
			public void run() {
				_numOfClockTicks++;
				if (_tweetsPer5Seconds > _maxTweetsPer5Seconds)
					_maxTweetsPer5Seconds= _tweetsPer5Seconds;
				_totalTweetsPer5Seconds+= _tweetsPer5Seconds;
				_tweetsPer5Seconds= 0;
			}
		}, 0, STATISTICS_INTERVAL);
	}

	
	public static Statistics getStatisticsObject(){
		if (instance == null)
			instance= new Statistics();
		return instance;
	}

	
	public void shutDown(){
		_clock.cancel();
		_stillRunning= false;
	}

	
	public synchronized void timeItTookToTweet(long time){
		if (_stillRunning){
			_totalTimeToPassATweet+= time;
			_tweetCount++;
			_tweetsPer5Seconds++;
		}
	}
	

	public void getStats() {
		StringBuilder answer= new StringBuilder();
		UserDatabase users= UserDatabase.getUsers();
		Vector<String> usersTopStats= users.getTopStats();
		if (_numOfClockTicks == 0)
			_numOfClockTicks= 1;
		if (_tweetCount == 0)
			_tweetCount= 1;
		answer.append("Max number of tweets per 5 seconds: ").append(_maxTweetsPer5Seconds).append(StompFrame.endlineChar);
		answer.append("Average number of tweets per 5 seconds: ").append(_totalTweetsPer5Seconds/_numOfClockTicks).append(StompFrame.endlineChar);
		answer.append("Average time to tweet: ").append(_totalTimeToPassATweet/_tweetCount).append(StompFrame.endlineChar);
		answer.append("The most followed user is ").append(usersTopStats.elementAt(0))
		.append(", with ").append(usersTopStats.elementAt(1)).append(" followers").append(StompFrame.endlineChar);
		answer.append("The user with most tweets is ").append(usersTopStats.elementAt(2))
		.append(", with ").append(usersTopStats.elementAt(3)).append(" tweets").append(StompFrame.endlineChar);
		answer.append("The user with most mentions is ").append(usersTopStats.elementAt(4)).append(StompFrame.endlineChar);
		answer.append("The most mentioned user is ").append(usersTopStats.elementAt(5)).append(StompFrame.endlineChar);
		users.AddMessageToUser("server", answer.toString());
	}



}
