package com.twis.common;

import java.util.Map;
import java.util.TreeMap;

public class MapSort {
	public static Map<String, Object> sortMapByKey(Map<String, Object> map) { 
		if (map == null || map.isEmpty()) {  
            return null;  
        } 
		Map<String, Object> sortMap = new TreeMap<String, Object>(new MapKeyComparator()); 
		sortMap.putAll(map); 
		return sortMap;  
	}
}
