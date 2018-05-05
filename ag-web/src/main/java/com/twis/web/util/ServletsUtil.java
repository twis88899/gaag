/**

 * @Description:TODO

 * @author:ben

 * @time:2016年5月9日 下午8:26:03

 */

package com.twis.web.util;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.twis.web.util.security.Token;

public class ServletsUtil {
	/**
	 * 取得带相同前缀的Request Parameters, copy from spring WebUtils.
	 * 
	 * 返回的结果的Parameter名已去除前缀.
	 */
	public static Map<String, Object> getParametersStartingWith(ServletRequest request, String prefix) {
		Enumeration<String> paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		if (prefix == null) {
			prefix = "";
		}
		while ((paramNames != null) && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if ("".equals(prefix) || paramName.startsWith(prefix)) {
				String unprefixed = paramName.substring(prefix.length());
				String[] values = request.getParameterValues(paramName);
				if ((values == null) || (values.length == 0)) {
					// Do nothing, no values found at all.
				} else if (values.length > 1) {
					params.put(unprefixed, values);
				} else {
					params.put(unprefixed, values[0]);
				}
			}
		}
		return params;
	}
	
	public static String getParamterValue(ServletRequest request, String paramName, String defValue) {
		if ((request != null) && (paramName != null)) {
			Object value = request.getParameter(paramName);
			if (value != null)
				return value.toString();
			else 
				return defValue;
		} else {
			return defValue;
		}
	}
	
	public static int getParamterValue(ServletRequest request, String paramName, int defValue) {
		String sValue = getParamterValue(request, paramName, String.valueOf(defValue));
		try {
			return Integer.parseInt(sValue);
		} catch(Exception ex) {
			return defValue;
		}
	}
	
	public static Map<String, Object> getParametersNotStartingWith(ServletRequest request, String prefix) {
		return getParametersNotStartingWithEx(request, prefix, false);
	}
	
	public static Map<String, Object> getParametersNotStartingWithEx(ServletRequest request, String prefix, boolean delEmptyValue) {
		Enumeration<String> paramNames = request.getParameterNames();
		Map<String, Object> params = new TreeMap<String, Object>();
		if (prefix == null) {
			prefix = "";
		}
		while ((paramNames != null) && paramNames.hasMoreElements()) {
			String paramName = (String) paramNames.nextElement();
			if ((!("".equals(prefix))) && paramName.startsWith(prefix))
				continue;
			
			String[] values = request.getParameterValues(paramName);
			
			if ((values == null) || (values.length == 0)) {
				// Do nothing, no values found at all.
			} else if (values.length > 1) {
				params.put(paramName, values);
			} else {
				String tempStr = values[0];
				if (delEmptyValue) {
					if ((tempStr != null) && (tempStr.length() != 0)) {
						params.put(paramName, tempStr);
					}
				} else {
					params.put(paramName, tempStr);
				}
			}
		}
		return params;
	}
		
	public static void setResponseResultString(HttpServletResponse response, String dataStr) {
		try {
			OutputStream outputStream = response.getOutputStream();
	   		 response.setHeader("content-type", "text/html;charset=UTF-8");
	   		 byte[] dataByteArr = dataStr.toString().getBytes("UTF-8");
	   		 outputStream.write(dataByteArr);
	   		 response.flushBuffer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getCurrentSessionId(){
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();
		HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();
		return request.getSession().getId();
	}
	
	public Object getWebUserAttribute(String name) {
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();  
		HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest();  
		Object obj=null;
		if (!name.trim().equals("")) {
			obj= request.getSession().getAttribute(name);
		}
		return obj;
	}
	
	public static Token getToken(){
		Token token =new Token(""); 
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();  
		HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest(); 
		if (request.getSession()!=null) {
			//RedisUtils redis = new RedisUtils();
			//token.setToken(redis.getValue("sessionId@"+request.getSession().getId()));
		}
		return token;
	}
	
	public static void setToken(String token){
		RequestAttributes ra = RequestContextHolder.getRequestAttributes();  
		HttpServletRequest request = ((ServletRequestAttributes)ra).getRequest(); 
		//RedisUtils redis = new RedisUtils();
		//redis.save("sessionId@"+request.getSession().getId(), token);
	}

	public static Map<String,Object> getRequestParams(ServletRequest request) {
		return ServletsUtil.getParametersNotStartingWith((ServletRequest)request, "_");
	}
	
	public static Map<String,Object> getRequestParamsByDelEmptyValue(ServletRequest request) {
		return ServletsUtil.getParametersNotStartingWithEx((ServletRequest)request, "_", true);
	}
	
	public static String getRequestParamsByJsonStr(ServletRequest request) {
		return JsonMapper.toNonNullJson(getRequestParams(request));
	}
	
	
	public static void addTokenToMap(Map<String, Object> map) {
		if(map != null) {
			map.put("token", getToken().getToken());
		}
	}
	
	public static Map<String,Object> getParam(HttpServletRequest request){
		Map<String,Object> map = new HashMap<String, Object>();
		Enumeration<String> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
				String paramName = (String) paramNames.nextElement();
				if("_v".equals(paramName))
					continue;
				String[] paramValues = request.getParameterValues(paramName);
				if (paramValues.length == 1) {
					String paramValue = paramValues[0];
					if (StringUtils.isNotEmpty(StringEscapeUtils.unescapeJava(paramValue.trim()))) {
						map.put(paramName, paramValue);
					}
				}else{
					StringBuilder sb = new StringBuilder();
					for(String pv:paramValues){
						sb.append(pv);
						sb.append(",");
					}
					String paramValue = sb.toString().substring(0,sb.toString().length()-1);
					if (StringUtils.isNotEmpty(StringEscapeUtils.unescapeJava(paramValue.trim()))) {
						map.put(paramName, paramValue);
					}
				}
		}
		return map;
	}
	
	/**
	 * 获取浏览器客户端得真实IP地址
	 * @param req
	 * @return
	 */
	public static String getIp(HttpServletRequest req) {
		String ip_for = req.getHeader("x-forwarded-for");
		String ip_client = req.getHeader("http_client_ip");
		String un = "unknown";

		if (ip_for != null && !ip_for.equalsIgnoreCase(un)
				&& ip_for.trim().length() > 0) {
			return ip_for;
		} else if (ip_client != null && !ip_client.equalsIgnoreCase(un)
				&& ip_client.trim().length() > 0) {
			return ip_client;
		} else {
			return req.getRemoteAddr();
		}
	}
	
	
}
