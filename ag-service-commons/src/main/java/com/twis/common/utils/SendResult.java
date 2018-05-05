/**
 * Beijing ZhangMeng online technology co.,LTD,SMS platform JAVA SDK file
 * Copyright(R)��2012--2016 Beijing ZhangMeng online technology co.,LTD.All rights reserved. 
 * @author Tongwei wu
 * @version 1.0
 *
 */

package com.twis.common.utils;

public class SendResult {
	private String ticket;
	private int status;
	private String description;
	private String faillist;
	private String taskid;

	public String getTicket() {
		return this.ticket;
	}

	public int getStatusCode() {
		return this.status;
	}

	public void setTicket(String str) {
		ticket = str;
	}

	public void setStatusCode(int i) {
		status = i;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFaillist() {
		return faillist;
	}

	public void setFaillist(String faillist) {
		this.faillist = faillist;
	}

	public String getTaskid() {
		return taskid;
	}

	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	
	
}
