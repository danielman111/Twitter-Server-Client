package threadPerClientServer.protocol;

import utilities.STOMP.StompFrame;


public class MessageProtocolFactory implements ServerProtocolFactory<StompFrame> {

	
	public ServerProtocol<StompFrame> create(){
		return new StompProtocol();
	}

}


