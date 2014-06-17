package utilities.FollowerIdCollection;

import java.util.Vector;

public class FollowerIdCollection {

	private Vector<FollowerIdNode> _followers;
	
	
	public FollowerIdCollection(){
		_followers= new Vector<FollowerIdNode>();
	}
	
	
	public synchronized void addFollower(String username, String id){
		_followers.add(new FollowerIdNode(username, id));
	}
	
	
	public synchronized boolean removeFollower(String id){
		for (int i= 0; i < _followers.size(); i++) {
			if (_followers.elementAt(i).getId().equals(id)){
				_followers.remove(i);
				return true;
			}
		}
		return false;
	}
	
	
	public String findFollowerNameById(String id){
		String followerFound= "";
		for (int i= 0; i < _followers.size(); i++) {
			if (_followers.elementAt(i).getId().equals(id)){
				followerFound= _followers.elementAt(i).getUsername();
				break;
			}
		}
		return followerFound;
	}
	
	
	public String findFollowerIdByName(String username){
		String followerFound= "";
		for (int i= 0; i < _followers.size(); i++) {
			if (_followers.elementAt(i).getUsername().equals(username)){
				followerFound= _followers.elementAt(i).getId();
				break;
			}
		}
		return followerFound;
	}
	
	
	public int getNumberOfFollowers(){
		return _followers.size();
	}
	
	
	public String getFollowerByLocation(int location){
		return _followers.elementAt(location).getUsername();
	}
	

}
