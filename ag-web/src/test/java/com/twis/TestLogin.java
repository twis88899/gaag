package com.twis;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSON;
import com.twis.biz.service.sysparam.SysparamOfMysqlService;
import com.twis.common.utils.JsonResult;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath*:applicationContextTest.xml" })
public class TestLogin {

	private static Logger log = LoggerFactory.getLogger(TestLogin.class);
	
	@Autowired
	private SysparamOfMysqlService sysparamOfMysqlService;
	

	@BeforeClass
	public static void init() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		JoranConfigurator configurator = new JoranConfigurator();
		configurator.setContext(lc);
		lc.reset();
		try {
			configurator.doConfigure("src/test/resources/logback-ts.xml");
		} catch (JoranException e) {
			e.printStackTrace();
		}
		StatusPrinter.printInCaseOfErrorsOrWarnings(lc);
	}


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
