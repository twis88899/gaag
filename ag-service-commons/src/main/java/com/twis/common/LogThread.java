package com.twis.common;

import org.aspectj.lang.ProceedingJoinPoint;

import com.twis.common.utils.JsonResult;

public class LogThread extends Thread {
	private ProceedingJoinPoint pjp;
	private Twis zhiTe;
	private Throwable e;
	private Long sTime;
	private JsonResult jsonResult;
	public LogThread(ProceedingJoinPoint pjp,Twis zhiTe,Throwable e,Long sTime,JsonResult jsonResult){
		this.pjp = pjp;
		this.zhiTe = zhiTe;
		this.e = e;
		this.sTime = sTime;
		this.jsonResult = jsonResult;
	}
	public void run() {
		//Log.sysLog(pjp,zhiTe,e,sTime,jsonResult);
	}
}
