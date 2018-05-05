package com.twis.common.exception;

public class UtilException extends BaseException {

	private static final long serialVersionUID = 1L;
	
	public UtilException() {

	}

	public UtilException(String message) {
		super(message);
	}

	public UtilException(String message, Throwable cause) {
		super(message, cause);
	}

	public UtilException(Throwable cause) {
		super(cause);
	}

	protected UtilException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
}
