package com.twis.common;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.twis.common.exception.BaseException;
import com.twis.common.utils.DateTimeUtil;
import com.twis.common.utils.JsonResult;

import net.sf.json.JSONObject;

@Component
@Aspect
public class TwisAop {

	@Before(value = "@annotation(twis)")
	public void beforeRun(JoinPoint jp, Twis twis) throws Throwable {
	}
	
	@AfterReturning(pointcut = "@annotation(twis)")
	public void afterReturning(JoinPoint jp, Twis twis)throws Exception {

	}

	@AfterThrowing(pointcut = "@annotation(twis)", throwing = "e")
	public void afterThrowing(JoinPoint jp, Twis twis, Exception e)throws Exception {
		
	}
	
	private boolean checkDebug(ProceedingJoinPoint pjp,Twis twis) throws Throwable {
		boolean flag = false,flag1=false;
		Object[] args = pjp.getArgs();
		if ((args[0] instanceof Map)) {
			@SuppressWarnings("unchecked")
			Map<String, String> parameter = (Map<String, String>)args[0];
			if (parameter.containsKey("debug")){
				Object debug = parameter.get("debug");
				if ((Boolean)debug){
					flag1 = true;
				}
			}
			Object debug = System.getProperty("Debug");
			if(Boolean.parseBoolean((String)debug)||flag1) {
				flag = true;
			}
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Around(value = "@annotation(twis)")
	public Object around(ProceedingJoinPoint pjp,Twis twis) throws Throwable {
		if(!checkDebug(pjp,twis)) {
			if(twis.checkLogin().equals("true")){
				Object[] args = pjp.getArgs();
				if ((args != null) && (args.length > 0) && (args[0] instanceof Map)) {
					Map<String, String> parameter = (Map<String, String>)args[0];
					if(!CheckLogin.checkLogin(parameter)){
						return CheckLogin.checkLogin();
					}
				} else {
					return CheckLogin.noLoginToken();
				}
			}
		}
		
		if(Boolean.parseBoolean(StringUtils.hasText(System.getProperty("printUrl"))?System.getProperty("printUrl"):"false")){
			System.out.println("APP接口地址：------------------->" + twis.url());
			System.out.println("APP参数：        ------------------->" +  com.alibaba.fastjson.JSON.toJSONString(pjp.getArgs()));
		}
		
		if(Boolean.parseBoolean(StringUtils.hasText(System.getProperty("oneUrl"))?System.getProperty("oneUrl"):"false")){
			System.out.println("APP接口地址：------------------->" + twis.url());
			System.out.println("APP参数：        ------------------->" +  com.alibaba.fastjson.JSON.toJSONString(pjp.getArgs()));
		}
		
		
		JsonResult jsonResult = new JsonResult();
		
		Long sTime = new Date().getTime();
		Object o = null;
		try {
			o = pjp.proceed();
			ExecutorService pool = null;
			try {
				pool = Executors.newSingleThreadExecutor();
				if(twis.saveLog().equals("true")&&twis.saveResult().equals("false")){
					pool.execute(new LogThread(pjp,twis,null,sTime,null));
				}else if(twis.saveLog().equals("true")&&twis.saveResult().equals("true")){
					pool.execute(new LogThread(pjp,twis,null,sTime,jsonResult));
				}
			} finally {
				pool.shutdown();
			}
			
			sTime=null;
		} catch (Throwable e) {
			
			try {
				Object[] args = pjp.getArgs();
				System.out.println("around: " + DateTimeUtil.getCurrentDateStr(DateTimeUtil.TIME_PATTERN_DEFAULT));
				System.out.println(String.format("{%s}.{%s}", pjp.getTarget().getClass(), pjp.getSignature().getName()));
				if (args != null) {
					for(int i = 0; i< args.length; i++)
						if (args[i] != null) {
					          if ((args[i] instanceof Map)) {
					        	  System.out.println(JSONObject.fromObject(args[i]));
					          } else if ((args[i] instanceof String)) {
					        	  System.out.println(args[i]);
					          } else if (args[i].getClass().equals(Object.class)){
					        	  System.out.println(args[i].getClass());
					    	  }
						}
				}
			} catch(Exception ex) {
				
			}
			
			e.printStackTrace();
			
			if (e.getClass().getSuperclass()!=null&&e.getClass().getSuperclass().getTypeName().equals(BaseException.class.getTypeName())){
				
				String msg = e.getMessage();
				if ((msg == null) || ("".equals(msg))) {
					msg = "后台错误！请与管理员联系！";
				}
				
				jsonResult.setMsg(msg);
				BaseException baseException = (BaseException) e;
				jsonResult.setErrorCode(baseException.getCode());
			}else{
				jsonResult.setMsg("后台错误！请与管理员联系！");
				jsonResult.setErrorCode(-1);
			}
			jsonResult.setSuccess(false);
			ExecutorService pool = null;
			try {
				pool = Executors.newSingleThreadExecutor();
				if(twis.saveLog().equals("true")&&twis.saveResult().equals("false")){
					pool.execute(new LogThread(pjp,twis,e,sTime,null));
				}else if(twis.saveLog().equals("true")&&twis.saveResult().equals("true")){
					pool.execute(new LogThread(pjp,twis,e,sTime,jsonResult));
				}
			} finally {
				pool.shutdown();
			}
			
			return jsonResult;
		}
		sTime=null;
		return o;
	}
}
