package com.twis.web.service;

import java.util.Map;

import com.twis.common.utils.JsonResult;

/**
 * 控制服务基类
 * @author Administrator
 *
 */
public interface BaseService {
	JsonResult buildService(Map<String,Object> parameter);
}	
