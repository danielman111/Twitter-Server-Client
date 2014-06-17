package utilities.STOMP;

import java.util.HashMap;

public class ConnectFrame extends StompFrame{

	
	protected ConnectFrame(String wholeMessage) {
		super(wholeMessage);
	}

	
	public static StompFrame factory(String wholeMessage){
		HashMap<String, String> headers= parseHeaders(wholeMessage);
		if (headers == null)
			return ErrorFrame.factory(wholeMessage, "cannot read headers");
		if (headers.get("accept-version") == null)
			return ErrorFrame.factory(wholeMessage, "invalid connection: no accept-version entered");
		if (headers.get("host") == null)
			return ErrorFrame.factory(wholeMessage, "invalid connection: no host entered");
		if (headers.get("login") == null)
			return ErrorFrame.factory(wholeMessage, "invalid connection: no username entered");
		if (headers.get("passcode") == null) 
			return ErrorFrame.factory(wholeMessage, "invalid connection: no passcode entered");
		return new ConnectFrame(wholeMessage);
	}
	

	public String getSTOMPType() {
		return "CONNECT";
	}

	
	public String getUsername() {
		return new String(super._headers.get("login"));
	}
	
	
	public String getPasscode() {
		return new String(super._headers.get("passcode"));
	}

}
