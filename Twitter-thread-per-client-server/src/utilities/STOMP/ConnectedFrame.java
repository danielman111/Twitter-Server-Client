package utilities.STOMP;

public class ConnectedFrame extends StompFrame{

	
	protected ConnectedFrame(String wholeMessage) {
		super(wholeMessage);
	}
	
	
	public static ConnectedFrame factory() {
		String message= formatIntoConnectedMessage();
		int endlineIndex= message.indexOf(StompFrame.endlineChar);
		String headlessMessage= message.substring(endlineIndex + StompFrame.lenghtOfEndlineChar);
		return new ConnectedFrame(headlessMessage);
	}
	
	
	public static String formatIntoConnectedMessage(){
		StringBuilder sb= new StringBuilder("CONNECTED");
		sb.append(StompFrame.endlineChar).append("version:1.2").append(StompFrame.endlineChar).append(StompFrame.nullChar);
		return sb.toString();
	}


	public String getSTOMPType() {
		return "CONNECTED";
	}

}
