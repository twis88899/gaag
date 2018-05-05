package com.twis.web.util.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.twis.web.util.annotation.Permission;

/**
 * 权限验证
 * @author Brainhu
 *
 */
@Service
public class PermissionInterceptor extends HandlerInterceptorAdapter{

	
	public void postHandle(HttpServletRequest request,
			   HttpServletResponse response, Object handler,
			   ModelAndView mv) throws Exception {

	}

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		
		//处理Permission Annotation，实现方法级权限控制  
        HandlerMethod method = (HandlerMethod)handler;  
        Permission permission = method.getMethodAnnotation(Permission.class);  
          
       
        return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
