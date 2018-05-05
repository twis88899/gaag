package com.twis.common.exception;

public class ServiceException extends BaseException {

	private static final long serialVersionUID = 1L;

	
	public ServiceException() {

	}

	public ServiceException(String message) {
		super(message);
		errorCode = -1;
	}
	/**
	 * 自定义 errorCode
	 * @author xukai
	 * */
	public ServiceException(String message,int errorCode) {
		super(message);
		this.errorCode = errorCode;
	}
	
	public ServiceException(String message, Throwable cause) {
		super(message, cause);
		errorCode = -1;
	}

	public ServiceException(Throwable cause) {
		super(cause);
		errorCode = -1;
	}

	protected ServiceException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
		errorCode = -1;
	}
	
}
