package com.github.hyacinth;

/**
 * HyacinthException
 */
public class HyacinthException extends RuntimeException {
	
	private static final long serialVersionUID = 342820722361408621L;
	
	public HyacinthException(String message) {
		super(message);
	}
	
	public HyacinthException(Throwable cause) {
		super(cause);
	}
	
	public HyacinthException(String message, Throwable cause) {
		super(message, cause);
	}
}