package com.ahajri.hc.enums;

/**
 * 
 * @author ahajri
 *
 */
public enum MessageEnum {

	USER_CREATED_OK("User %s created with success"), 
	USER_UPDATED_OK("User %s updated with success"), 
	USER_DELETED_OK("User %s deleted with success"), 
	LOGIN_OK("Login success"), 
	USER_ACTIVATED_OK("User activated!");

	private String message;

	private MessageEnum(String message) {
		this.message = message;
	}

	public String getMessage(Object... params) {
		return String.format(message, params);
	}
}
