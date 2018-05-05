package com.twis.common.utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
   /**
   *  去掉指定字符串的开头和结尾的指定字符

   * @param stream 要处理的字符串
   * @param trimstr 要去掉的字符串
   * @return 处理后的字符串
   */
  public static String sideTrim(String stream, String trimstr) {
    // null或者空字符串的时候不处理
    if (stream == null || stream.length() == 0 || trimstr == null || trimstr.length() == 0) {
      return stream;
    }
 
    // 结束位置
    int epos = 0;
 
    // 正规表达式
    String regpattern = "[" + trimstr + "]*+";
    Pattern pattern = Pattern.compile(regpattern, Pattern.CASE_INSENSITIVE);
 
    // 去掉结尾的指定字符 
    StringBuffer buffer = new StringBuffer(stream).reverse();
    Matcher matcher = pattern.matcher(buffer);
    if (matcher.lookingAt()) {
      epos = matcher.end();
      stream = new StringBuffer(buffer.substring(epos)).reverse().toString();
    }
 
    // 去掉开头的指定字符 
    matcher = pattern.matcher(stream);
    if (matcher.lookingAt()) {
      epos = matcher.end();
      stream = stream.substring(epos);
    }
 
    // 返回处理后的字符串
    return stream;
  }
  
  /**
      *获取map指定键的字符串,没有返回缺省值
   */
  public static String getMapStringValue(Map<String, Object> parameter, String keyStr, String defValue){
	  if (parameter != null) {
		  if (parameter.containsKey(keyStr) && (parameter.get(keyStr) != null)) {
			  return parameter.get(keyStr).toString();
		  } else {
			  return defValue;
		  }
	  } else {
		  return defValue;
	  }
  }
  
  public static String getMapStringValueOfTrim(Map<String, Object> parameter, String keyStr, String defValue){
	  if (parameter != null) {
		  if (parameter.containsKey(keyStr) && (parameter.get(keyStr) != null)) {
			  return parameter.get(keyStr).toString().trim();
		  } else {
			  return defValue;
		  }
	  } else {
		  return defValue;
	  }
  }
  
  public static Object getMapValue(Map<String, Object> parameter, String keyStr, Object defValue){
	  if (parameter != null) {
		  return parameter.containsKey(keyStr) ? parameter.get(keyStr) : defValue;
	  } else {
		  return defValue;
	  }
  }
  
  public static String getMapStringValueByArray(Map<String, Object> parameter, String keyStr, String defValue){
	  if (parameter != null) {
		  if (parameter.containsKey(keyStr)) {
			  @SuppressWarnings("unchecked")
			  ArrayList<Object> arys = (ArrayList<Object>) parameter.get(keyStr);
			  if((arys != null) && (arys.size() > 0)) {
				  String values = "";
				  for(int i=0; i<arys.size();i++){
						if(i<arys.size()-1){
							values +=  arys.get(i)+",";
						}else{
							values +=  arys.get(i);
						}
				  }
				  return values;
			  }
		  } 		
	  }
	  return defValue;
  }
  
  public static Date getMapDateValue(Map<String, Object> parameter, String keyStr){
	  if (parameter != null) {
		  if (parameter.containsKey(keyStr)) {
			  String dateStr = StringUtil.getMapStringValue(parameter, keyStr, "");
			  if (isEmptyOrNull(dateStr)) {
				  return null;
			  } else {
				  return DateTimeUtil.parseDayDateByAuto(dateStr);
			  }
		  } 		
	  }
	  return null;
  }
  
  /**
   *获取map指定键的值,没有返回缺省值
  */
	public static Integer getMapStringValue(Map<String, Object> parameter, String keyStr, Integer defValue){
		  if (parameter != null) {
			  try {
				  String str = parameter.containsKey(keyStr) ? parameter.get(keyStr).toString() : defValue.toString();
				  return Integer.parseInt(str);
			  } catch (Exception ex) {
				  return defValue;
			  }
		  } else {
			  return defValue;
		  }
	}
	
  /**
   *获取map指定键的值,没有返回缺省值
  */
	public static Long getMapStringValue(Map<String, Object> parameter, String keyStr, Long defValue){
		  if (parameter != null) {
			  try {
				  String str = parameter.containsKey(keyStr) ? parameter.get(keyStr).toString() : defValue.toString();
				  return Long.parseLong(str);
			  } catch (Exception ex) {
				  return defValue;
			  }
		  } else {
			  return defValue;
		  }
	}
	

  /**
   *获取map指定键的值,没有返回缺省值
  */
	public static Float getMapStringValue(Map<String, Object> parameter, String keyStr, Float defValue){
		  if (parameter != null) {
			  try {
				  String str = parameter.containsKey(keyStr) ? parameter.get(keyStr).toString() : defValue.toString();
				  return Float.parseFloat(str);
			  } catch (Exception ex) {
				  return defValue;
			  }
		  } else {
			  return defValue;
		  }
	}
  
  /**
    *检测字符串为null或空
   */
  public static boolean isEmptyOrNull(String str) {
	  return str == null || "".equals(str);
  }
  
  public static String getFromBase64(String str) {
		byte[] asBytes = Base64.getDecoder().decode(str);  
		try {
			return new String(asBytes, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return "";
  }
  
  public static String getBase64(String str) {
	byte[] b = null;
	String s = null;
	b = str.getBytes();
	if (b != null) {
		s = Base64.getEncoder().encodeToString(b);
	}
	return s;	
  }
  
  /**

   * 获取字符串的长度，如果有中文，则每个中文字符计为2位
   * 
   * @param value
   *            指定的字符串
   * @return 字符串的长度
   */
  public static int length(String value) {
	  if (value == null) return 0;
      int valueLength = 0;
      String chinese = "[\u0391-\uFFE5]";
      /* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
      for (int i = 0; i < value.length(); i++) {
          /* 获取一个字符 */
          String temp = value.substring(i, i + 1);
          /* 判断是否为中文字符 */
          if (temp.matches(chinese)) {
              /* 中文字符长度为2 */
              valueLength += 2;
          } else {
              /* 其他字符长度为1 */
              valueLength += 1;
          }
      }
      return valueLength;
  }
}
