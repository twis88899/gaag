package com.twis.web.control;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.twis.common.Twis;
import com.twis.common.utils.JsonResult;
import com.twis.web.service.SysparamCService;
import com.twis.web.util.ServletsUtil;

@Controller
public class SysparamContoller extends BaseController{
	private static Logger log = LoggerFactory.getLogger(SysparamContoller.class);
	@Autowired
	private SysparamCService sysparamCService;
	/**
	 * 单位信息
	 * @param map
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/sysparamInfo")
	@ResponseBody
	@Twis(checkLogin="false",logName="系统参数信息")
	public JsonResult sysparamInfo(@RequestBody Map<String, Object> map, HttpServletRequest request,
			HttpServletResponse response) {
		if(log.isDebugEnabled()) {
			log.debug("SysparamContoller sysparamInfo {}, {}", "系统参数信息", JSON.toJSONString(map));
		}
		map.put("remoteIp", ServletsUtil.getIp(request));
		return sysparamCService.buildService(map);
	}
}
