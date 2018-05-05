package com.twis.common.service;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.twis.common.exception.ModelException;

public class ModelUtil {
	/**
	  * @Title: extractDataByJsonToModel	
	  * @Description: 从 'json'中抽取key值跟model字段名相同的数据装配到对应model,返回:<"msg",String>错误信息    和    <"object",modelObject>model对象
	  * @param Map<String, String> json		
	  * @param Class<?> clazz
	  * @return Map<String, Object> 
	  */
	public static Map<String, Object> extractDataByJsonToModel(Map<String, String> json,Class<?> clazz){
		StringBuilder msg = new StringBuilder();
		Map<String, Object> result = new HashMap<String, Object>();
		Set<String> keySet = json.keySet();
		Object object = null ;
		try{
			object = clazz.newInstance();
			Field[] fields = clazz.getDeclaredFields();
			for (Field field : fields) {
				String fieldName = field.getName();
				for (String key : keySet) {
					if(key.equalsIgnoreCase(fieldName)){
						String value = json.get(key);
						if(!StringUtils.isEmpty(value)){
							Class<?> fieldType = field.getType();
							String fieldTypeName = fieldType.getSimpleName().toString();
							String methodName = "set"+fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
							Method md = clazz.getDeclaredMethod(methodName, fieldType);
							if(methodName.equals(md.getName())){
								if("String".equals(fieldTypeName)){
									md.invoke(object,value);
								}else if("Integer".equals(fieldTypeName)){
									md.invoke(object,Integer.parseInt(value));
								}else if("Long".equals(fieldTypeName)){
									md.invoke(object,Long.parseLong(value));
								}else if("Float".equals(fieldTypeName)){
									md.invoke(object,Float.parseFloat(value));
								}else if("Double".equals(fieldTypeName)){
									md.invoke(object,Double.parseDouble(value));
								}else if("Date".equals(fieldTypeName)){
									SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
									md.invoke(object,format.parse(value));
								}else if("Short".equals(fieldTypeName)){
									md.invoke(object, Short.parseShort(value));
								}else if("Byte".equals(fieldTypeName)){
									md.invoke(object, Byte.parseByte(value));
								}else if("Boolean".equals(fieldTypeName)){
									md.invoke(object, Boolean.parseBoolean(value));
								}else{
									msg.append(" ,"+key+":"+value+" 类型为："+fieldTypeName+" 转换失败!!! ");
								}
							}
						}else continue;
					}
				}
				
			}
		}catch(NumberFormatException e){
			e.printStackTrace();
			msg.append(" 数字转换失败. ");
		}catch(ParseException e){
			e.printStackTrace();
			msg.append(" 日期转换失败 ");
		}catch(Exception e){
			e.printStackTrace();
			msg.append(" 内部错误 ");
		}
		result.put("msg", msg.toString());
		result.put("object", object);
		return result;
	}
	
	
	
	/**
	 * json中key值名要跟Model类的field名一致,其它参数手动获取
	 * */
	public static Map<String, Object> setParamValueOfJson2(Map<String, String> json,Class<?> clazz){
		StringBuilder msg = new StringBuilder();
		Map<String, Object> result = new HashMap<String, Object>();
		Object object = null ;
		Set<String> keySet = json.keySet();
		try{
			for (String key : keySet) {
				try{
					Field field = clazz.getDeclaredField(key);
					if(field!=null){
					BeanInfo beanInfo = Introspector.getBeanInfo(clazz, Object.class);
					PropertyDescriptor[] propDescs = beanInfo.getPropertyDescriptors();
					if(propDescs.length<=0||propDescs==null)return null;
					Object obj = clazz.newInstance();
						for (PropertyDescriptor propDesc : propDescs) {
							String fieldName = propDesc.getName();
							String fieldType = propDesc.getPropertyType().getSimpleName();
							if(json.containsKey(fieldName)){
								String value = json.get(fieldName);
								Object val = null;
								if(StringUtils.isEmpty(value)){
									continue;
								}
								if("Integer".equals(fieldType)){
									val =  Integer.parseInt(value);
								}else if("Long".equals(fieldType)){
									val = Long.parseLong(value);
								}else if("Double".equals(fieldType)){
									val = Double.parseDouble(value);
								}else if("Date".equals(fieldType)){
									SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
									val = format.parse(value);
								}else if("Float".equals(fieldType)){
									val = Float.parseFloat(value);
								}else{
									throw new ModelException("转换失败");
								}
								propDesc.getWriteMethod().invoke(obj,val);
							}
						}
					}
				}catch (NoSuchFieldException  e2) {
				}catch (SecurityException e) {
				}
			}
			
		}catch(NumberFormatException e){
			e.printStackTrace();
			msg.append(" 数字转换失败 ");
		}catch(ParseException e){
			e.printStackTrace();
			msg.append(" 日期转换失败 ");
		}catch(Exception e){
			e.printStackTrace();
			msg.append("内部错误");
		}
		result.put("msg", msg.toString());
		result.put("object", object);
		return result;
	}
}
