package utilities.STOMP;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


public abstract class StompFrame {

	protected HashMap<String, String> _headers;
	protected String _body;
	public static final char nullChar= '\u0000';
	public static final char endlineChar= '\n';
	public static final int lenghtOfEndlineChar= 1;
	
	
	public StompFrame(String wholeMessage) {
		_headers= new HashMap<String, String>();
		parseMessage(wholeMessage);
	}
	
	
	public abstract String getSTOMPType();
	
	
	public boolean isDisconnectFrame(){
		return getSTOMPType().equals("DISCONNECT");
	}
	
	
	public String getWholeSTOMPMessage() {
		StringBuilder builder= new StringBuilder(getSTOMPType());
		builder.append(StompFrame.endlineChar);
		Iterator<Entry<String, String>> it = _headers.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, String> pairs = (Map.Entry<String, String>)it.next();
	        builder.append(pairs.getKey()).append(":").append(pairs.getValue()).append(StompFrame.endlineChar);
	    }
		builder.append(StompFrame.endlineChar).append(_body).append(StompFrame.endlineChar).append(nullChar);
		return builder.toString();
	}
	
	
	private void parseMessage(String wholeMessage) {
		String tempMessage= wholeMessage;
		int indexOfNewline= tempMessage.indexOf(StompFrame.endlineChar);
		String newLine= tempMessage.substring(0, indexOfNewline);
		tempMessage= tempMessage.substring(indexOfNewline + StompFrame.lenghtOfEndlineChar);
		while (!newLine.equals("")) {
			int indexOfDelimiter= newLine.indexOf(":"); 
			_headers.put(newLine.substring(0, indexOfDelimiter), newLine.substring(indexOfDelimiter + 1));
			indexOfNewline= tempMessage.indexOf(StompFrame.endlineChar);
			newLine= tempMessage.substring(0, indexOfNewline);
			tempMessage= tempMessage.substring(indexOfNewline + StompFrame.lenghtOfEndlineChar);
		}
		int indexOfNullChar= tempMessage.indexOf(nullChar);
		tempMessage= tempMessage.substring(0, indexOfNullChar);
		if (tempMessage.endsWith("" + StompFrame.endlineChar))
			tempMessage= tempMessage.substring(0, tempMessage.length() - StompFrame.lenghtOfEndlineChar);
		_body= tempMessage;
	}
	
	protected static HashMap<String, String> parseHeaders(String wholeMessage){
		HashMap<String, String> answer= new HashMap<String, String>();
		try {
			String tempMessage= wholeMessage;
			int indexOfNewline= tempMessage.indexOf(StompFrame.endlineChar);
			String newLine= tempMessage.substring(0, indexOfNewline);
			tempMessage= tempMessage.substring(indexOfNewline + StompFrame.lenghtOfEndlineChar);
			while (!newLine.equals("")) {
				int indexOfDelimiter= newLine.indexOf(":");
				Object oldVal= answer.put(newLine.substring(0, indexOfDelimiter), newLine.substring(indexOfDelimiter + 1));
				if (null != oldVal)
					return null;
				indexOfNewline= tempMessage.indexOf(StompFrame.endlineChar);
				newLine= tempMessage.substring(0, indexOfNewline);
				tempMessage= tempMessage.substring(indexOfNewline + StompFrame.lenghtOfEndlineChar);
			}
		} catch (Exception e) {
			return null;
		}
		return answer;
	}

}
