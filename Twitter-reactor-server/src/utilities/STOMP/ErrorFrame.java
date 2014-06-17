package utilities.STOMP;

public class ErrorFrame extends StompFrame{

	
	protected ErrorFrame(String wholeMessage) {
		super(wholeMessage);
	}
	
	
	public static ErrorFrame factory(String wholeMessage, String error) {
		String message= formatIntoErrorMessage(wholeMessage, error);
		int endlineIndex= message.indexOf(StompFrame.endlineChar);
		String headlessMessage= message.substring(endlineIndex + StompFrame.lenghtOfEndlineChar);
		return new ErrorFrame(headlessMessage);
	}
	
	
	public static String formatIntoErrorMessage(String wholeMessage, String error){
		StringBuilder sb= new StringBuilder("ERROR");
		sb.append(StompFrame.endlineChar);
		sb.append("message:").append(error);
		sb.append(StompFrame.endlineChar).append(StompFrame.endlineChar).append(StompFrame.endlineChar);
		sb.append("The message recieved is:").append(StompFrame.endlineChar);
		int indexOfNullChar= error.indexOf(StompFrame.nullChar);
		if (indexOfNullChar > 0)
			wholeMessage= wholeMessage.substring(0, indexOfNullChar);
		sb.append(wholeMessage).append(StompFrame.endlineChar).append(StompFrame.nullChar);
		return sb.toString();
	}

	
	public String getSTOMPType() {
		return "ERROR";
	}
	
	
	protected String getMalformedMessage() {
		return new String(super._body);
	}
	
	
}