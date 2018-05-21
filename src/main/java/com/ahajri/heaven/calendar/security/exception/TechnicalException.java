package com.ahajri.heaven.calendar.security.exception;

public class TechnicalException extends Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2710920803659092057L;
	
	
	public TechnicalException(Throwable cause){
		super(cause);
	}
	public TechnicalException(String msg, Throwable cause){
		super(msg,cause);
	}
}
