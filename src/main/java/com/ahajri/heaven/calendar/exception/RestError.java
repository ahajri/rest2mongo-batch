package com.ahajri.heaven.calendar.exception;

import java.io.Serializable;
import java.util.Optional;

/**
 * Classe du message d'erreur REST
 * 
 * @author ahajri
 * @version 1
 */
public class RestError implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9054040415035607804L;

	private int status;
	private String msg, moreInfo, code;

	public RestError(int status, String code, String msg, Optional<Throwable> moreInfo) {
		super();
		this.status = status;
		this.code = code;
		this.msg = msg;
		this.moreInfo = moreInfo.get().getMessage();
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getMoreInfo() {
		return moreInfo;
	}

	public void setMoreInfo(String moreInfo) {
		this.moreInfo = moreInfo;
	}

	@Override
	public String toString() {
		return "RestError [status=" + status + ", code=" + code + ", msg=" + msg + ", moreInfo=" + moreInfo + "]";
	}

}
