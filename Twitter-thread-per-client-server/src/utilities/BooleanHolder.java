package utilities;

public class BooleanHolder {

	private boolean _value;
	
	public BooleanHolder(){
		_value= false;
	}
	
	
	public void setTrue(){
		_value= true;
	}
	

	public void setFalse(){
		_value= false;
	}
	
	
	public boolean getValue(){
		return _value;
	}

}
