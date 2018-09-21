package com.ahajri.heaven.calendar.enums;

public enum ErrorMessageEnum {
	
	USER_CREATION_KO("User creation failed due to %s"), 
	USER_NOT_FOUND_FOR_EMAIL("User not found for Email:  %s"), 
	MORE_THAN_ONE_USER_FOR_EMAIL("More than one user found for email: %s"), 
	WRONG_PASSWORD("Wrong password !"), 
	USER_STILL_NOT_ACTIF("User %s still not actif"), 
	EMAIL_FORMAT_ROC("Email is not in ROC format"), 
	LOGIN_FAILED("Login failed : %s"), 
	USER_UPADATE_KO("User update fail"), 
	DELETE_DOCUMENT_KO("Could not delete document %s"), 
	USER_NOT_FOUND_FOR_TOKEN("User not found for token %s"), 
	MORE_THAN_ONE_USER_FOR_TOKEN("More than one user found for token: %s"), 
	USER_ACTIVATION_KO("User activation failed"), 
	EMAIL_TOKEN("Email %s already token"), 
	FIND_DOCUMENT_KO("Find document failed"), 
	SEARCH_DOCUMENT_KO("Search document failed"), 
	SEARCH_USER_KO("Search user failed"), 
	UNAUTHORIZED_RESOURCE("Acesss unauthorized !"),
	 AUTHENTIFICATION_KO("Authentication failed !"),;

	private String message;

	private ErrorMessageEnum(String message) {
		this.message = message;
	}

	public String getMessage(Object... params) {
		return String.format(message, params);
	}
}
