package me.legault.hdlc;

public class InvalidFrameException extends RuntimeException {

	private static final long serialVersionUID = -5094190593377558038L;
	
	public InvalidFrameException(String message){
		super(message);
	}

}
