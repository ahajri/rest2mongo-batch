package com.ahajri.heaven.calendar.exception;



public class BusinessException extends Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2876696568345332875L;

	String functionalMessage;

    public BusinessException(Throwable cause) {
        super(cause);
    }

    public BusinessException(String errorMsg) {
        super(errorMsg);
    }

    public BusinessException(Throwable cause, String functionalMessage) {
        super(cause);
        this.functionalMessage = functionalMessage;
    }


    public String getFunctionalMessage() {
        return functionalMessage;
    }
}
