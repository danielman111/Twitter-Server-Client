package utilities.STOMP;

import java.util.HashMap;

public class UnsubscribeFrame extends StompFrame{

	
	public UnsubscribeFrame(String wholeMessage) {
		super(wholeMessage);
	}

	
	public static StompFrame factory(String wholeMessage){
		HashMap<String, String> headers=parseHeaders(wholeMessage);
		if (headers == null)
			return ErrorFrame.factory(wholeMessage, "cannot read headers");
		if (headers.get("id") == null) 
			return ErrorFrame.factory(wholeMessage, "invalid unsubscribe: no id entered");
		return new UnsubscribeFrame(wholeMessage);
	}
	
	
	public String getSTOMPType() {
		return "UNSUBSCRIBE";
	}
	
	
	public String getId() {
		return new String(super._headers.get("id"));
	}
	

}