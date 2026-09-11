package simulator.control.exception;

public class IllegalArgumentException extends Exception{
	
	private static final long serialVersionUID = 1L;
	
	public IllegalArgumentException(String message) {
		
		super(message);
		
	}
	
	public IllegalArgumentException(String message, Throwable cause ) {
		
		super(message, cause);
		
	}
	
	public  IllegalArgumentException(Throwable cause) {
		
		super(cause);
		
	}

}
