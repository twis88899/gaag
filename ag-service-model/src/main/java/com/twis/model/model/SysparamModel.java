package com.twis.model.model;

import com.twis.model.BaseObject;
import com.twis.model.Column;
import com.twis.model.Table;

@Table(name="SB_FT_SYSPARAM")
public class SysparamModel extends BaseObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3785465190409274449L;
	@Column(name="ID")
	private Long id;
	@Column(name="PARAMKEY")
	private String paramKey;
	@Column(name="PARAMNAME")
	private String paramName;
	@Column(name="PARAMVALUE")
	private String paramValue;
	@Column(name="PARAMDESC")
	private String paramDesc;
	@Column(name="ISSTATUS")
	private Integer isStatus;
	@Column(name="VERSION")
	private Integer version;
	
	public SysparamModel(){}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getParamKey() {
		return paramKey;
	}

	public void setParamKey(String paramKey) {
		this.paramKey = paramKey;
	}

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getParamDesc() {
		return paramDesc;
	}

	public void setParamDesc(String paramDesc) {
		this.paramDesc = paramDesc;
	}

	public Integer getIsStatus() {
		return isStatus;
	}

	public void setIsStatus(Integer isStatus) {
		this.isStatus = isStatus;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}
	
	
	
}
