package utilities.STOMP;

import java.util.HashMap;

public class SendFrame extends StompFrame{

	
	protected SendFrame(String wholeMessage) {
		super(wholeMessage);
	}

	
	public static StompFrame factory(String wholeMessage){
		HashMap<String, String> headers=parseHeaders(wholeMessage);
		if (headers == null)
			return ErrorFrame.factory(wholeMessage, "cannot read headers");
		if (headers.get("destination") == null)
			return ErrorFrame.factory(wholeMessage, "invalid message: no destination entered");
		return new SendFrame(wholeMessage);
	}
	
	
	public String getSTOMPType() {
		return "SEND";
	}
	
	
	public String getDestination() {
		return new String(super._headers.get("destination"));
	}
	
	
	public String getMessage() {
		return new String(super._body);
	}
	

}
