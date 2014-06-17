package threadPerClientServer.tokenizer;

import utilities.STOMP.StompFrame;

public class StompTokenizerFactory implements TokenizerFactory<StompFrame>{

	public Tokenizer<StompFrame> create(){
		return new StompTokenizer();
	}

}
