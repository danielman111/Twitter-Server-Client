package threadPerClientServer.tokenizer;

import java.io.BufferedReader;

public interface Tokenizer<T> {

	public T getMessage(BufferedReader br);
	
	public String serverClosed();

}
