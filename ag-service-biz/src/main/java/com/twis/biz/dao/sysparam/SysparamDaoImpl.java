package com.twis.biz.dao.sysparam;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.twis.common.dao.impl.BaseExDaoOfMysqlImpl;
import com.twis.model.model.SysparamModel;

@Repository
public class SysparamDaoImpl extends BaseExDaoOfMysqlImpl<SysparamModel> implements SysparamDao {

	@Override
	public List<Object> queryParamValue() throws Exception {
		String sql = "select * from sb_ft_sysparam where isstatus = 1";
		return this.query(sql, new Object[]{}, SysparamModel.class);
	}

	@Override
	public String getParamVal(String paramkey, String paramName) throws Exception {
		String sql = "select paramvalue from sb_ft_sysparam where paramkey=? and paramname=? and isstatus=1";
		return this.getFirstValueByArgs(sql, new Object[]{paramkey,paramName}, "");
	}

}
