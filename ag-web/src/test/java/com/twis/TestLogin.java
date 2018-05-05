package com.twis;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.twis.biz.service.sysparam.SysparamOfMysqlService;
import com.twis.common.utils.JsonResult;

public class TestLogin extends BaseTestCase {

	private static Logger log = LoggerFactory.getLogger(TestLogin.class);
	
	@Autowired
	private SysparamOfMysqlService sysparamOfMysqlService;



	@Test
	public void getSysparam() throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		JsonResult jsonResult = sysparamOfMysqlService.querySysparamValue(map);
		log.debug("sysparamOfMysqlService test {}, {}", "ok", JSON.toJSONString(jsonResult));
	}
	
	
	@Test
	public void getUid() throws Exception {
		log.debug(UUID.randomUUID().toString());
	}
	
	
}
