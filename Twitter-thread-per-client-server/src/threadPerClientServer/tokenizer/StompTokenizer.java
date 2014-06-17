package threadPerClientServer.tokenizer;

import java.io.BufferedReader;
import java.io.IOException;
import utilities.STOMP.*;

public class StompTokenizer implements Tokenizer<StompFrame>{

	private final char _delimiter= StompFrame.nullChar;
	private boolean _serverShutdown= false;;

	
	public StompFrame getMessage(BufferedReader br) {
		StringBuffer fullMessage= new StringBuffer();
		int newchar;
		try {
			while ((!_serverShutdown) && ((newchar= br.read()) != -1)){
				if (newchar == _delimiter){
					fullMessage.append(_delimiter);
					break;
				}
				else
					fullMessage.append((char)newchar);
			}
			if (!_serverShutdown){
				if (fullMessage.substring(0, StompFrame.lenghtOfEndlineChar) == StompFrame.endlineChar)
					fullMessage= new StringBuffer(fullMessage.substring(StompFrame.lenghtOfEndlineChar));
				return constructMessage(fullMessage);
			}
			else
				return null;
		} catch (IOException e) {
			if (!_serverShutdown)
				e.printStackTrace();
		}
		return null;
	}
	
	
	public String serverClosed(){
		String closeNotice= ErrorFrame.formatIntoErrorMessage("the server is shutting down", "server shutdown"); 
		_serverShutdown= true;
		return (closeNotice);
	}
	

	private StompFrame constructMessage(StringBuffer incomingMessage) {
		String fullMessage= incomingMessage.toString();
		System.out.println("Server received: ");
		System.out.println(fullMessage.toString());
		int indexOfNewline= fullMessage.indexOf("" + StompFrame.endlineChar);
		if (indexOfNewline == 0){
			fullMessage= fullMessage.substring(StompFrame.lenghtOfEndlineChar);
			indexOfNewline= fullMessage.indexOf("" + StompFrame.endlineChar);
		}
		String header= fullMessage.substring(0, indexOfNewline);
		if (header.equals("CONNECT")){
			return ConnectFrame.factory(fullMessage.substring(indexOfNewline + StompFrame.lenghtOfEndlineChar));
		}
		if (header.equals("SEND")){
			return SendFrame.factory(fullMessage.substring(indexOfNewline + StompFrame.lenghtOfEndlineChar));
		}
		if (header.equals("SUBSCRIBE")){
			return SubscribeFrame.factory(fullMessage.substring(indexOfNewline + StompFrame.lenghtOfEndlineChar));
		}
		if (header.equals("UNSUBSCRIBE")){
			return UnsubscribeFrame.factory(fullMessage.substring(indexOfNewline + StompFrame.lenghtOfEndlineChar));
		}
		if (header.equals("DISCONNECT")){
			return DisconnectFrame.factory(fullMessage.substring(indexOfNewline + StompFrame.lenghtOfEndlineChar));
		}
		return ErrorFrame.factory(fullMessage.toString(), "malformed STOMP message");
	}
	
	

}
