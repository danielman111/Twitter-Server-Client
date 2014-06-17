package utilities.FollowerIdCollection;

public class FollowerIdNode {

	private final String _id;
	private final String _username;
	
	
	public FollowerIdNode(String username, String id){
		_username= username;
		_id= id;
	}
	
	
	protected String getId(){
		return _id;
	}
	
	
	protected String getUsername(){
		return _username;
	}


}
