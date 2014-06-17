package utilities.STOMP;

import java.util.HashMap;

public class DisconnectFrame extends StompFrame{

	
	public DisconnectFrame(String wholeMessage) {
		super(wholeMessage);
	}

	
	public static StompFrame factory(String wholeMessage){
		HashMap<String, String> headers=parseHeaders(wholeMessage);
		if (headers == null)
			return ErrorFrame.factory(wholeMessage, "cannot read headers");
		if (headers.get("receipt") == null)
			return ErrorFrame.factory(wholeMessage, "invalid disconnect: no receipt entered");
		return new DisconnectFrame(wholeMessage);
	}
	

	public String getSTOMPType() {
		return "DISCONNECT";
	}

	
	public String getReceiptId() {
		return new String(super._headers.get("receipt"));
	}
	
	
}
