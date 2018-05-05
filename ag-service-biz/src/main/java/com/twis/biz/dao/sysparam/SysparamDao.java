package com.twis.biz.dao.sysparam;

import java.util.List;

import com.twis.common.dao.BaseDao;

public interface SysparamDao extends BaseDao {
	List<Object> queryParamValue() throws Exception;
	String getParamVal(String paramkey,String paramName) throws Exception;
}
