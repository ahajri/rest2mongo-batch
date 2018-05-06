package com.ahajri.heaven.calendar.security.exception;

public class FunctionalException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8603304586141729163L;
	
	public FunctionalException(Throwable throwable){
		super(throwable);
	}
	public FunctionalException(String msg){
		super(msg);
	}

}
