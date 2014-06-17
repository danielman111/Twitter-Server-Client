package utilities;

public class BooleanHolder {

	private boolean _value;
	
	public BooleanHolder(){
		_value= true;
	}
	

	public synchronized void setFalse(){
		_value= false;
	}
	
	
	public synchronized boolean getValue(){
		return _value;
	}

}
