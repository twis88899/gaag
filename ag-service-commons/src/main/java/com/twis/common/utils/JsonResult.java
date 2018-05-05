package com.twis.common.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 198207868642348481L;
	
	//add by ben 
	public static JsonResult getSuccessResult(){
		JsonResult jsonResult = new JsonResult();
		jsonResult.setSuccess(true);
		return jsonResult;
	}
	
	public static JsonResult getSuccessResult(String msgStr) {
		JsonResult jsonResult = getSuccessResult();
		jsonResult.setMsg(msgStr);
		return jsonResult;
	}
	
	public static JsonResult getFailResult(String msgStr){
		JsonResult jsonResult = new JsonResult();
		jsonResult.setMsg(msgStr);
		jsonResult.setSuccess(false);
		return jsonResult;
	}
	
	public static JsonResult getFailResult(String msgStr, int errorCode) {
		JsonResult jsonResult = getFailResult(msgStr);
		jsonResult.setErrorCode(errorCode);
		return jsonResult;
	}
	
	public static JsonResult getInsertResult(int execNum) {
		if (execNum > 0) {
			return JsonResult.getSuccessResult("共添加" + execNum);
		} else {
			return JsonResult.getFailResult("数据没有添加", 1);
		}
	}
	
	public static JsonResult getInsertResult(boolean bFlag) {
		if (bFlag) {
			return JsonResult.getSuccessResult("添加数据成功!");
		} else {
			return JsonResult.getFailResult("数据没有添加", 1);
		}
	}
	
	public static JsonResult getInsertResultById(Long id) {
		 JsonResult jsonObj = JsonResult.getInsertResult(id != null);
		 jsonObj.setData("id", id);
		 return jsonObj;
	}
	
	public static JsonResult getUpdateResult(int execNum) {
		if (execNum > 0) {
			return JsonResult.getSuccessResult("共更新" + execNum);
		} else {
			return JsonResult.getFailResult("无此数据", 1);
		}
	}
	
	public static JsonResult getDeleteResult(int execNum) {
		if (execNum > 0) {
			return JsonResult.getSuccessResult("共删除" + execNum);
		} else {
			return JsonResult.getFailResult("无此数据", 1);
		}
	}
	
	public static JsonResult getDeleteResultByAlwaysSuccess(int execNum) {
		return JsonResult.getSuccessResult("共删除" + execNum);
	}
	
	public static JsonResult getQueryResult(Map<String,Object> value) {
		JsonResult jsonResult = JsonResult.getSuccessResult("查询完毕");
		jsonResult.setData(value);
		return jsonResult;
	}
	
	public static JsonResult getQueryResultByDataAry(Object value) {
		return getQueryResult(getDataAryMap(value));
	}
	
	public static Map<String, Object> getDataAryMap(Object value) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		if(value instanceof java.util.List){
			returnMap.put("data", value);
		} else {
			List<Object> list = new ArrayList<Object>();
			if (value != null) {
				list.add(value);	
			}
			returnMap.put("data",  list);
		}
		return returnMap;
	}
	//

	protected boolean success = false;

	protected String msg = "";
	
	protected int errorCode=0;
	
	

	private Map<String, Object> data = new HashMap<String, Object>();

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(String key,Object value) {
		this.data.put(key, value);
	}
	
	public void setData(Map<String,Object> value) {
		this.data=value;
	}
	
	public void setDataByMapDataAry(Object value) {
		this.setData(getDataAryMap(value));
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}	
}
