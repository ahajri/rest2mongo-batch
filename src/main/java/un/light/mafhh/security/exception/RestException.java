package un.light.mafhh.security.exception;

import org.springframework.http.HttpStatus;

/**
 * Common Rest Controller Exception
 * 
 * @author ahajri
 * @version 1
 */
public class RestException extends Throwable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4277888281465088142L;

	private HttpStatus httpStatus;
	private String code;

	public RestException(String message, Exception ex, HttpStatus httpStatus, String code) {
		super(message);
		this.httpStatus = httpStatus;
		this.code = code;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
