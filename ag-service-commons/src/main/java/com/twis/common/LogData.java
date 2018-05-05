package com.twis.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.twis.common.utils.JsonResult;

public class LogData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3335849916351979411L;

	private String serviceName;
	
	private Date date;
	
	private List<Object> data = new ArrayList<Object>();
	
	private JsonResult jsonResult;
	
	private Boolean isException =false;
	
	private String exceptionMsg;
	
	private StackTraceElement[] exceptionStackTrace;
	
	private Long executeTime;
	
	private String user;
	

	private String url;
	
	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public Date getDate() {
		return new Date();
	}


	public List<Object> getData() {
		return data;
	}

	public void setData(Object data) {
		this.data.add(data);
	}

	public JsonResult getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(JsonResult jsonResult) {
		this.jsonResult = jsonResult;
	}

	public Boolean getIsException() {
		return isException;
	}

	public void setIsException(Boolean isException) {
		this.isException = isException;
	}

	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

	public StackTraceElement[] getExceptionStackTrace() {
		return exceptionStackTrace;
	}

	public void setExceptionStackTrace(StackTraceElement[] exceptionStackTrace) {
		this.exceptionStackTrace = exceptionStackTrace;
	}

	public Long getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Long executeTime) {
		this.executeTime = executeTime;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	
	
	
	
}
