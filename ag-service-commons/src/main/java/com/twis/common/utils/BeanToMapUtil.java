/**

 * @Description:TODO

 * @author:ben

 * @time:2016年5月13日 下午12:54:21

 */


package com.twis.common.utils;

import java.beans.BeanInfo; 
import java.beans.Introspector; 
import java.beans.PropertyDescriptor; 
import java.lang.reflect.Method; 
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map; 

public class BeanToMapUtil {
	public static <T> T mapToObject(Map<String, Object> map, Class<T> beanClass) throws Exception {    
        T obj = beanClass.newInstance(); 
        if (map != null) {
        	BeanUtils.populate(obj, map);
        }
        return obj; 
    }  

      
    public static Map<String, Object> objectToMap(Object obj, boolean bNotNull) throws Exception {    
        if(obj == null)  
            return null;      
  
        Map<String, Object> map = new HashMap<String, Object>();   
  
        BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());    
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();    
        for (PropertyDescriptor property : propertyDescriptors) {    
            String key = property.getName();    
            if (key.compareToIgnoreCase("class") == 0) {   
                continue;  
            }  
            Method getter = property.getReadMethod();  
            Object value = getter!=null ? getter.invoke(obj) : null;  
            if ((value == null) && (bNotNull))
            	continue;
            map.put(key, value); 
        }    
  
        return map;  
    }  
    
    public static Map<String, Object> objectToMapWithNotNull(Object obj) throws Exception {    
        return objectToMap(obj, true);
    }
    
    public static Map<String, Object> objectToPreciseQueryMap(Object obj) throws Exception {
    	 if(obj == null)  
             return null;      
   
         Map<String, Object> map = new HashMap<String, Object>();   
   
         BeanInfo beanInfo = Introspector.getBeanInfo(obj.getClass());    
         PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();    
         for (PropertyDescriptor property : propertyDescriptors) {    
             String key = property.getName();    
             if (key.compareToIgnoreCase("class") == 0) {   
                 continue;  
             }  
             Method getter = property.getReadMethod();  
             Object value = getter!=null ? getter.invoke(obj) : null;  
             if ((value == null))
             	continue;
             
			if (value.getClass().equals(String.class)) {
				Map<String, Object> precise = new LinkedHashMap<String, Object>();
				precise.put("precise", value);				
				map.put(key, precise);
			} else {
				map.put(key, value);
			}
         }    
   
         return map;
    }
}

