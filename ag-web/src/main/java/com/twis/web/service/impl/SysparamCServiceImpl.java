package com.twis.web.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.twis.biz.service.sysparam.SysparamOfMysqlService;
import com.twis.common.service.impl.BaseServiceImpl;
import com.twis.common.utils.JsonResult;
import com.twis.web.service.SysparamCService;
@Service
public class SysparamCServiceImpl extends BaseServiceImpl implements SysparamCService {

	private static Logger log = LoggerFactory.getLogger(SysparamCServiceImpl.class);
	@Autowired
	private SysparamOfMysqlService sysparamOfMysqlService;
	@Override
	public JsonResult buildService(Map<String, Object> parameter) {
		JsonResult jsonResult = new JsonResult();
		try {
			jsonResult = sysparamOfMysqlService.querySysparamValue(parameter);
			if(log.isDebugEnabled()) {
				log.debug("{},{},{}","SysparamCServiceImpl","querySysparamValue",JSON.toJSONString(jsonResult));
			}
		} catch (Exception e) {
			log.error("{},{},{},{}","SysparamCServiceImpl","querySysparamValue",JSON.toJSONString(jsonResult),e);
			e.printStackTrace();
		}
		return jsonResult;
	}

}
