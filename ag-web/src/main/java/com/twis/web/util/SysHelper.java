package com.twis.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yxm
 * 读取 或 写入 property文件
 * */
public class SysHelper {

	private  SysHelper(){};
	public static SysHelper getInstance(){
		return SysHelperHandle.INSTANCE;
	}
	private static class SysHelperHandle{
		private static final SysHelper INSTANCE = new SysHelper();
	}
	/**
	 * 读取property配置
	 * */
	public static String getProperty(String key,String propertyURL){
		Properties prop = new Properties();
	    InputStream in = SysHelper.class.getResourceAsStream(propertyURL);
        try {
			prop.load(in);
		} catch (IOException e){
			e.printStackTrace();
		}
		return prop.getProperty(key);
	}

	public static boolean isSuccessRequest(String jsonStr){
		if(jsonStr!=null&&jsonStr.contains("\"success\":true")) return true;
		else return false;
	}

	public static boolean isSuccessAndNotNullDataRequest(String jsonStr){
		if(jsonStr!=null&&jsonStr.contains("\"success\":true")&&!jsonStr.contains("\"data\": []")) return true;
		else return false;
	}

	/**
	 * nginx代理和不能获取getRemoteAddr不能获取真实ip，用以下方法
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static String  getServerIp(){
		try {
			for (Enumeration<NetworkInterface> nis = NetworkInterface
					.getNetworkInterfaces(); nis.hasMoreElements();) {
				NetworkInterface ni = nis.nextElement();
				if (ni.isLoopback() || !ni.isUp())
					continue;
				for (Enumeration<InetAddress> ias = ni.getInetAddresses(); ias.hasMoreElements();) {
					InetAddress ia = ias.nextElement();
					if (ia instanceof Inet6Address) continue;
					return ia.getHostAddress();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	public static String getLocalIP(){
		InetAddress addr = null;
		try {
			addr = InetAddress.getLocalHost();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		byte[] ipAddr = addr.getAddress();
		String ipAddrStr = "";
		for (int i = 0; i < ipAddr.length; i++) {
			if (i > 0) {
				ipAddrStr += ".";
			}
			ipAddrStr += ipAddr[i] & 0xFF;
		}
		//System.out.println(ipAddrStr);
		return ipAddrStr;
	}
}
