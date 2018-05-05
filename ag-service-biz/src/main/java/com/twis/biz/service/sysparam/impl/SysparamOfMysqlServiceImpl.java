package com.twis.biz.service.sysparam.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.twis.biz.dao.sysparam.SysparamDao;
import com.twis.biz.service.sysparam.SysparamOfMysqlService;
import com.twis.common.utils.JsonResult;

@Service
public class SysparamOfMysqlServiceImpl implements SysparamOfMysqlService {

	@Autowired
	private SysparamDao sysparamDao;
	@Override
	public JsonResult insert(Map<String, Object> parameter) throws Exception {
		return null;
	}

	@Override
	public JsonResult update(Map<String, Object> parameter) throws Exception {
		return null;
	}

	@Override
	public JsonResult delete(Map<String, Object> parameter) throws Exception {
		return null;
	}

	@Override
	public JsonResult query(Map<String, Object> parameter) throws Exception {
		return null;
	}

	@Override
	public JsonResult querySysparamValue(Map<String, Object> parameter) throws Exception {
		JsonResult jsonResult = new JsonResult();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("data",sysparamDao.queryParamValue());
		jsonResult.setData(map);
		jsonResult.setSuccess(true);
		return jsonResult;
	}

}
