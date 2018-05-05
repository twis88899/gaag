package com.twis.common.exception;

import java.io.Serializable;

public class BaseException extends Exception implements Serializable {

	private static final long serialVersionUID = 1L;
	
	protected  int errorCode = -1;
	
	public BaseException() {

	}

	public BaseException(String message) {
		super(message);
	}

	public BaseException(String message, Throwable cause) {
		super(message, cause);
	}

	public BaseException(Throwable cause) {
		super(cause);
	}

	protected BaseException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
	
	public void setCode(int errorCode){
		this.errorCode =  errorCode;
	}
	public int getCode(){
		return this.errorCode;
	}

	public static void main(String[] args) {
		System.out.println((new BaseException("ok")).getMessage());
	}
}
