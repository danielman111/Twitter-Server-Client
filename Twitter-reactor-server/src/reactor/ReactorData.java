package reactor;

import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.nio.channels.Selector;

import protocol.*;
import tokenizer.*;

/**
 * a simple data structure that hold information about the reactor, including getter methods
 */
public class ReactorData<T> {

    private final ExecutorService _executor;
    private final Selector _selector;
    private final ServerProtocolFactory<T> _protocolMaker;
    private final TokenizerFactory<T> _tokenizerMaker;
    private Vector<ConnectionHandler<T>> _allConnections;
    
    public ExecutorService getExecutor() {
        return _executor;
    }

    public Selector getSelector() {
        return _selector;
    }

	public ReactorData(ExecutorService _executor, Selector _selector, ServerProtocolFactory<T> protocol, TokenizerFactory<T> tokenizer) {
		this._executor = _executor;
		this._selector = _selector;
		this._protocolMaker = protocol;
		this._tokenizerMaker = tokenizer;
		this._allConnections = new Vector<ConnectionHandler<T>>();
	}

	public ServerProtocolFactory<T> getProtocolMaker() {
		return _protocolMaker;
	}

	public TokenizerFactory<T> getTokenizerMaker() {
		return _tokenizerMaker;
	}
	
	public void addConnection(ConnectionHandler<T> newConnection) {
		_allConnections.add(newConnection);
	}

	public void shutDown() {
		for (ConnectionHandler<T> connection : _allConnections) {
			connection.stopServer();
		}
	}

}
