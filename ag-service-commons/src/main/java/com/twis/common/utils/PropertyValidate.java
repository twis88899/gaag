package com.twis.common.utils;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.twis.model.Column;

/**
 * model属性验证
 * @author yxm
 *
 */
public class PropertyValidate<T> {
	
	/**
	 * 查询时进行字段校验 
	 * @param model
	 * @param propertys
	 * @return
	 */
	public JsonResult validateQuery(T model,Map<String,String> propertys){
		JsonResult jsonResult = new JsonResult();
		Class<?> obj = model.getClass();
		Field[] fields = obj.getDeclaredFields();
		for(Map.Entry<String, ? extends Object> entry : propertys.entrySet()) {
			String name = entry.getKey();
			if (name == null||name.equals("")) {
				continue;
			}
			for (Field field : fields) {
				Column column = field.getAnnotation(Column.class);
				if(column!=null){
					String value = propertys.get(field.getName());
					if(column.isId().equals("false")&&StringUtils.hasText(value)){
						jsonResult = validateAll(column,value,field);
						if(jsonResult!=null){
							return jsonResult; 
						}
					}
				}
				
			}
		}
		return null;
	}

	/**
	 * 插入时进行字段校验
	 * @param model
	 * @param propertys
	 * @return
	 */
	public JsonResult validateInsert(T model,Map<String,String> propertys){
		JsonResult jsonResult=null;
		Class<?> obj = model.getClass();
		Field[] fields = obj.getDeclaredFields();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if(column!=null){
				String value = propertys.get(field.getName());
				jsonResult = validateAll(column,value,field);
				if(jsonResult!=null){
					return jsonResult;
				}
			}
		}
		return jsonResult;
	}
	
	private JsonResult validateAll(Column column ,String value,Field field){
		JsonResult jsonResult = new JsonResult();
		if(column.required().equals("false")&&!StringUtils.hasText(value)){
			return null;
		}
		
		if(column.required().equals("true")){
			if(!validateRequired(value)){
				setJsonResult(jsonResult,column);
				return jsonResult;
			}
			
		} 
		
		if("class java.math.BigDecimal".equals(field.getGenericType().toString())){
			if(!validateBigDecimal(value)){
				setJsonResult(jsonResult,column);
				return jsonResult;
			}
		}else if("class java.lang.Boolean".equals(field.getGenericType().toString())){
			if(!validateBoolean(value)){
				setJsonResult(jsonResult,column);
				return jsonResult;
			} 
		}else if("class java.util.Date".equals(field.getGenericType().toString())){
			if(!validateDate(value)){
				setJsonResult(jsonResult,column);
				return jsonResult;
			}
		}else if("class java.lang.Double".equals(field.getGenericType().toString())){
			if(!validateDouble(value)){
				setJsonResult(jsonResult,column);
				return jsonResult;
			}
		}else if("class java.lang.Float".equals(field.getGenericType().toString())){
			if(!validateFloat(value)){
				setJsonResult(jsonResult,column);
				return jsonResult;
			}
		}else if("class java.lang.Integer".equals(field.getGenericType().toString())){
			if(!validateInteger(value)){
				setJsonResult(jsonResult,column);
				return jsonResult;
			}
		}else if ("class java.lang.Long".equals(field.getGenericType().toString())){
			if(!validateLong(value)){
				setJsonResult(jsonResult,column);
				return jsonResult;
			}
		}
		return null;
	}
	
	/**
	 * 更新属性验证
	 * @param model
	 * @param propertys
	 * @return
	 */
	public JsonResult validateUpdate(T model,Map<String,String> propertys){
		JsonResult jsonResult = new JsonResult();
		Class<?> obj = model.getClass();
		Field[] fields = obj.getDeclaredFields();
		for(Map.Entry<String, ? extends Object> entry : propertys.entrySet()) {
			String name = entry.getKey();
			if (name == null||name.equals("")) {
				continue;
			}
			for (Field field : fields) {
				Column column = field.getAnnotation(Column.class);
				if(column!=null){
					String value = propertys.get(field.getName());
					if(column.isId().equals("true")){
						if(!validateId(value)){
							jsonResult = new JsonResult();
							jsonResult.setSuccess(false);
							jsonResult.setErrorCode(90001);
							jsonResult.setMsg("ID不能为空/ID非数字");
							return jsonResult;
						}
					}
					if(field.getName().equals(name))
					{
						jsonResult = validateAll(column,value,field);
						if(jsonResult!=null){
							return jsonResult;
						}
					}
					
					
				}
				
			}
		}
		return null;
	}
	
	/**
	 * 删除属性验证
	 * @param model
	 * @param propertys
	 * @return
	 */
	public JsonResult validateDelete(T model,Map<String,String> propertys){
		JsonResult jsonResult = new JsonResult();
		Class<?> obj = model.getClass();
		Field[] fields = obj.getDeclaredFields();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if(column!=null){
				String value =  propertys.get(field.getName());
				if(column.isId().equals("true")){
					if(!validateId(value)){
						jsonResult = new JsonResult();
						jsonResult.setSuccess(false);
						jsonResult.setErrorCode(90001);
						jsonResult.setMsg("ID不能为空/ID非数字");
						return jsonResult;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private Boolean validateId(String value){
		if(value!=null&&!value.equals(""))
		{
			return validateInteger(value);
		}else if(value==null||value.equals(""))
		{
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private Boolean validateRegex(String value){
		
		return true;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private Boolean validateLong(String value){
		try {
			Long.valueOf(value);
		}catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private Boolean validateInteger(String value){
		try {
			Integer.valueOf(value);
		}catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private Boolean validateFloat(String value){
		try {
			Float.valueOf(value);
		}catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private Boolean validateDouble(String value){
		try {
			Double.valueOf(value);
		}catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private Boolean validateDate(String value){
		
		String[] str = new String[]{"yyyy-MM-dd HH:mm:ss","yyyy-MM-dd","yyyy/MM/dd","yyyy/MM/dd HH:mm:ss","yyyy.MM.dd HH:mm:ss","yyyy.MM.dd"};
		for (String string : str) {
			SimpleDateFormat sdf = new SimpleDateFormat(string);
			try {
				sdf.parse(value);
				return true;
			} catch (ParseException e) {
//				b= false;
			}
		}
		return false;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private Boolean validateBoolean(String value){
		if(value.equals("1")||value.toLowerCase().equals("true"))
		{
			return true;
		}else if(value.equals("0")||value.toLowerCase().equals("false")){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private Boolean validateBigDecimal(String value){
		try{
			new BigDecimal(value);
		}catch(Exception ex){
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param value
	 * @return
	 */
	private Boolean validateRequired(String value){
		if(value==null||value.equals("")){
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param jsonResult
	 * @param column
	 */
	private void setJsonResult(JsonResult jsonResult,Column column){
		jsonResult.setSuccess(false);
		jsonResult.setErrorCode(column.errorCode());
		jsonResult.setMsg(column.errorMsg());
	}
}
