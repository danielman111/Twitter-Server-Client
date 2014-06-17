package threadPerClientServer.tokenizer;

public interface TokenizerFactory<T> {

	public Tokenizer<T> create();

}
