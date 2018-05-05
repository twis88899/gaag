package com.twis.common.dao;

import org.springframework.context.ApplicationContext;
import org.springframework.util.StringUtils;

public class SysConfigBean {
	public static String caservice;
	public static String appPlatformUrl;
	public static String appPlatformPort;
	public static String appPlatformVersion;
	public static String policyTime;
	public static String platformPort;
	public static String platformVersion;
	public static String platformUrl;
	public static ApplicationContext applicationContext;
	@SuppressWarnings("static-access")
	public void initData(SysConfigBean bean){
		SysConfigBean.caservice = bean.getCaservice();
		SysConfigBean.appPlatformUrl = bean.getAppPlatformUrl();
		SysConfigBean.appPlatformPort = bean.getAppPlatformPort();
		SysConfigBean.appPlatformVersion = bean.getAppPlatformVersion();
		SysConfigBean.policyTime = StringUtils.hasText(bean.getPolicyTime())?bean.getPolicyTime():"0";
		SysConfigBean.platformUrl = StringUtils.hasText(bean.getPlatformUrl())?bean.getPlatformUrl():"";
		SysConfigBean.platformPort = StringUtils.hasText(bean.getPlatformPort())?bean.getPlatformPort():"";
		SysConfigBean.platformVersion = StringUtils.hasText(bean.getPlatformVersion())?bean.getPlatformVersion():"";
		SysConfigBean.applicationContext = bean.applicationContext;
	}
	
	
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}


	public static void setApplicationContext(ApplicationContext applicationContext) {
		SysConfigBean.applicationContext = applicationContext;
	}


	public static String getPlatformPort() {
		return platformPort;
	}


	public static void setPlatformPort(String platformPort) {
		SysConfigBean.platformPort = platformPort;
	}


	public static String getPlatformVersion() {
		return platformVersion;
	}


	public static void setPlatformVersion(String platformVersion) {
		SysConfigBean.platformVersion = platformVersion;
	}


	public static String getPlatformUrl() {
		return platformUrl;
	}


	public static void setPlatformUrl(String platformUrl) {
		SysConfigBean.platformUrl = platformUrl;
	}


	public static String getPolicyTime() {
		return policyTime;
	}


	public static void setPolicyTime(String policyTime) {
		SysConfigBean.policyTime = policyTime;
	}


	public static String getCaservice() {
		return caservice;
	}
	public static void setCaservice(String caservice) {
		SysConfigBean.caservice = caservice;
	}
	public static String getAppPlatformUrl() {
		return appPlatformUrl;
	}
	public static void setAppPlatformUrl(String appPlatformUrl) {
		SysConfigBean.appPlatformUrl = appPlatformUrl;
	}
	public static String getAppPlatformPort() {
		return appPlatformPort;
	}
	public static void setAppPlatformPort(String appPlatformPort) {
		SysConfigBean.appPlatformPort = appPlatformPort;
	}
	public static String getAppPlatformVersion() {
		return appPlatformVersion;
	}
	public static void setAppPlatformVersion(String appPlatformVersion) {
		SysConfigBean.appPlatformVersion = appPlatformVersion;
	}
	

	
	
	
}
