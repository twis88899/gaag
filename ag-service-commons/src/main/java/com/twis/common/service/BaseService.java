package com.twis.common.service;

import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.twis.common.utils.JsonResult;


public interface BaseService {
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class})
	public JsonResult insert(Map<String, Object> parameter) throws Exception;
	
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class})
	public JsonResult update(Map<String, Object> parameter) throws Exception;
	
	@Transactional(propagation=Propagation.REQUIRES_NEW,rollbackFor={Exception.class})
	public JsonResult delete(Map<String, Object> parameter) throws Exception;
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public  JsonResult query(Map<String, Object> parameter) throws Exception;
}
