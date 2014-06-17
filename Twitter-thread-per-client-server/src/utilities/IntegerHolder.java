package utilities;

public class IntegerHolder {
	
	private int _value;
	
	
	public IntegerHolder(){
		_value= 0;
	}
	
	
	public IntegerHolder(int x){
		_value= x;
	}
	
	
	public void setValue(int x){
		_value= x;
	}
	
	
	public int getValue(){
		return _value;
	}

}
