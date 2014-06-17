package utilities.STOMP;


public class MessageFrame extends StompFrame{

	public static volatile long _counter= 0;
	
	protected MessageFrame(String wholeMessage){
		super(wholeMessage);
	}
	
	
	public static MessageFrame factory(String topic, String subscription, String message) {
		String fullMessage= formatIntoMessage(topic, subscription, message);
		int endlineIndex= fullMessage.indexOf(StompFrame.endlineChar);
		String headlessMessage= fullMessage.substring(endlineIndex + StompFrame.lenghtOfEndlineChar);
		return new MessageFrame(headlessMessage);
	}

	
	public static String formatIntoMessage(String topic, String subscription, String message){
		StringBuilder sb= new StringBuilder("MESSAGE");
		sb.append(StompFrame.endlineChar);
		if (!topic.equals(""))
			sb.append("destination:/topic/").append(topic).append(StompFrame.endlineChar);
		if (!subscription.equals(""))
			sb.append("subscription:").append(subscription).append(StompFrame.endlineChar);
		sb.append("message-id:").append(MessageFrame.getCount()).append(StompFrame.endlineChar).append(StompFrame.endlineChar);
		if (!message.equals(""))
			sb.append(message).append(StompFrame.endlineChar).append(StompFrame.nullChar);
		else
			sb.append(StompFrame.nullChar);
		return sb.toString();
	}
	
	
	public String getSTOMPType(){
		return "MESSAGE";
	}
	
	
	public static synchronized long getCount(){
		_counter++;
		return _counter;
	}

}
