package utilities.STOMP;

import java.util.HashMap;

public class SubscribeFrame extends StompFrame{

	
	public SubscribeFrame(String wholeMessage) {
		super(wholeMessage);
	}
	
	
	public static StompFrame factory(String wholeMessage){
		HashMap<String, String> headers=parseHeaders(wholeMessage);
		if (headers == null)
			return ErrorFrame.factory(wholeMessage, "cannot read headers");
		if (headers.get("destination") == null)
			return ErrorFrame.factory(wholeMessage, "invalid subscribe: no destination entered");
		if (headers.get("id") == null) 
			return ErrorFrame.factory(wholeMessage, "invalid subscribe: no id entered");
		return new SubscribeFrame(wholeMessage);
	}

	
	public String getSTOMPType() {
		return "SUBSCRIBE";
	}
	
	
	public String getDestination() {
		return new String(super._headers.get("destination"));
	}
	
	
	public String getId() {
		return new String(super._headers.get("id"));
	}
	

}
