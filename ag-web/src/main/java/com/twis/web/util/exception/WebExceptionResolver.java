package com.twis.web.util.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

/**
 * 异常解析器
 *
 * @author Retina.Ye
 */
public class WebExceptionResolver implements HandlerExceptionResolver {

	private Log logger = LogFactory.getLog(this.getClass());

	@Override
	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		ModelAndView modelAndView = new ModelAndView();
		logger.error("发生系统级异常:" + ex.getMessage(), ex);
		return modelAndView;
	}

}