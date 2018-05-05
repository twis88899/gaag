package com.twis.biz.service.sysparam;

import java.util.Map;

import com.twis.common.service.BaseService;
import com.twis.common.utils.JsonResult;

public interface SysparamOfMysqlService extends BaseService{
	public JsonResult querySysparamValue(Map<String,Object> parameter) throws Exception;
}
