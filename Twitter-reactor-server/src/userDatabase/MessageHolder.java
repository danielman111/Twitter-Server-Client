package userDatabase;

public class MessageHolder {

	private final String _topic;
	private final String _id;
	private final String _message;
	
	
	public MessageHolder(String topic, String id, String message) {
		_topic= topic;
		_id= id;
		_message= message;
	}
	
	
	public String getTopic(){
		return new String(_topic);
	}
	
	
	public String getId(){
		return new String(_id);
	}


	public String getMessage(){
		return new String(_message);
	}
	

}
