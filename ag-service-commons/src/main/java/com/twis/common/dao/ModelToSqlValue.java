package com.twis.common.dao;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.twis.model.Column;
import com.twis.model.JoinTable;
import com.twis.common.exception.ModelException;
import com.twis.common.utils.JsonResult;
import com.twis.common.utils.PropertyValidate;

/**
 * 根据model生产sql与参数相对应的值
 * @author yxm
 *
 */
public abstract class ModelToSqlValue {

	public synchronized  static  Object convertParameter(Column column,Field field,Object obj) throws Exception{
		Class<?> type = field.getType();
		if (type.equals(Long.class) || type.equals(long.class)) {
			try{
				if(obj!=null){
					if(StringUtils.hasText(String.valueOf(obj))){
						return Long.valueOf(obj.toString());
					}else{
						return null;
					}
				}
				
			}catch(Exception e){
				 throw new ModelException(column.errorMsg());
			}
		} else if (type.equals(Integer.class) || type.equals(int.class)) {
			try{
				if(obj!=null){
					if(StringUtils.hasText(String.valueOf(obj))){
						return Integer.valueOf(obj.toString());
					}else{
						return null;
					}
				}
				
			}catch(Exception e){
				String msg = column.errorMsg();
				if ((msg==null) || ("".equals(msg))) {
					msg = column.name();
				}
				throw new ModelException(msg); 
			}
		} else if (type.equals(String.class)) {
			try{
				if(obj!=null){
					if(StringUtils.hasText(String.valueOf(obj))){
						return String.valueOf(obj);
					}else{
						return "";
					}
				
				}
					
			}catch(Exception e){
				 throw new ModelException(column.errorMsg());
			}
		} else if (type.equals(Date.class)) {
			if(obj!=null){
				if(StringUtils.hasText(String.valueOf(obj))){
					if(obj.getClass().equals(Date.class)){
						return obj;
					}
					String[] str =  new String[] { "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss","yyyy.MM.dd HH:mm:ss","yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd" };
					Date date =null;
					for(String format :str){
						SimpleDateFormat sdf = new SimpleDateFormat(format);  
						try {
							date = sdf.parse(obj.toString());
							return date;
							
						} catch (ParseException e) {
							
						} 
					}
					if(date==null){
						throw new ModelException(column.errorMsg());
					}
				}else{
					return new Date();
				}
			}
		} else if (type.equals(BigDecimal.class)) {
			try{
				if(obj!=null){
					if(StringUtils.hasText(String.valueOf(obj))){
						return new BigDecimal(obj.toString());
					}else{
						return null;
					}
				}
					
			}catch (Exception e) {
				throw new ModelException(column.errorMsg());
			} 
		} else if (type.equals(Float.class) || type.equals(float.class)) {
			try{
				if(obj!=null){
					if(StringUtils.hasText(String.valueOf(obj))){
						return Float.valueOf(obj.toString());
					}else{
						return null;
					}
				}
				
			}catch (Exception e) {
				throw new ModelException(column.errorMsg());
			} 
		} else if (type.equals(Double.class) || type.equals(double.class)) {
			try{
				if(obj!=null){
					if(StringUtils.hasText(String.valueOf(obj))){
						return Double.valueOf(obj.toString());
					}else{
						return null;
					}
				}
				
			}catch (Exception e) {
				throw new ModelException(column.errorMsg());
			} 
		} else if (type.equals(Boolean.class) || type.equals(boolean.class)) {
			try{
				if(obj!=null){
					if(StringUtils.hasText(String.valueOf(obj))){
						if("false,no,n,off".indexOf(String.valueOf(obj).toLowerCase())>-1){
							return false;
						}else if("true,yes,y,on".indexOf(String.valueOf(obj).toLowerCase())>-1){
							return true;
						}
					}else{
						return false;
					}
					
				}
			}catch (Exception e) {
				throw new ModelException(column.errorMsg());
			}
		}
		return "";
	}
	
	/**
	 * 检查置是否允许为空
	 * @param column
	 * @param obj
	 * @throws Exception
	 */
	public synchronized static void checkRequired(Column column,Object obj) throws Exception{
		if(column.required().equals("true")&&obj==null){
			throw new ModelException(column.errorMsg()+"(不能为空！)");
		}
	}
	
	public synchronized static void checkRequiredByNull(Column column ,Object obj) throws Exception{
		if (column.enableNull() == false) {
			if ((obj == null) || ("".equals(obj))) {
				throw new ModelException(column.errorMsg()+"(不能为空！)");
			}
		}
	}
	
	public synchronized  static int convertParameterBoolean(Object obj){
		if((Boolean)obj){
			return 1;
		}else{
			return 2;
		}
	}
	
	
	/** 
	 * json的校验 （应用于多表联合的时候进行其它表的校验）
	 * @author xukai
	 * @param json map
	 * @param modParameter mod的参数名
	 * @param model mod
	 * **/
	protected synchronized static <T> JsonResult JsonCheck(Map<String, String> json, String modParameter, T model) {
		Map<String, String> jsonz = new HashMap<String, String>();
		for (Map.Entry<String, ? extends Object> entry : json.entrySet()) {
			String name = entry.getKey();
			String value = entry.getValue().toString();
			if (name.matches(modParameter + "\\..+")) {
				String[] key = name.split("\\.");
				jsonz.put(key[1], value);
			}
		}
		// 校验 Model
		JsonResult jsonResult = new PropertyValidate<T>().validateQuery(model, jsonz);
		return jsonResult;
	}
	/** 
	 * json的校验 （应用于多表联合的时候进行其它表的校验）
	 * @author xukai
	 * @param json map
	 * @param model 主表Model
	 * **/
	public synchronized static <T> JsonResult JsonCheck(Map<String, String> json,T model) throws Exception {
		JsonResult jsonResult = null;
		jsonResult = new PropertyValidate<T>().validateQuery(model, json);
		Field[] fields = model.getClass().getDeclaredFields();
		for (Field field : fields) {
			if (jsonResult != null)
				return jsonResult;
			JoinTable joinTable = field.getAnnotation(JoinTable.class);
			if (joinTable != null) {
				Object obj = field.getType().newInstance();
				jsonResult = JsonCheck(json, field.getName(), obj);
			}
		}
		return jsonResult;
	}
}
