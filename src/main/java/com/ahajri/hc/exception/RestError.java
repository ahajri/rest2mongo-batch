package com.ahajri.hc.exception;

import java.io.Serializable;

/**
 * REST Error Message
 * 
 * @author
 *         <p>
 * 		ahajri
 *         </p>
 *         
 * @version 1
 */
public class RestError implements Serializable {

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 702453970711605300L;

	/**
	 * HTTP Status
	 */
	private int httpStatus;

	/**
	 * Message technique dédiées au développeur
	 */
	private String technicalMessage;

	/**
	 * Message fonctionnel dédiées à l'utilisateur
	 */
	private String functionalMessage;

	public RestError(int httpStatus, String technicalMessage, String functionalMessage) {
		this.httpStatus = httpStatus;
		this.technicalMessage = technicalMessage;
		this.functionalMessage = functionalMessage;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getTechnicalMessage() {
		return technicalMessage;
	}

	public void setTechnicalMessage(String technicalMessage) {
		this.technicalMessage = technicalMessage;
	}

	public String getFunctionalMessage() {
		return functionalMessage;
	}

	public void setFunctionalMessage(String functionalMessage) {
		this.functionalMessage = functionalMessage;
	}

}
