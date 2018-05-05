/**

 * @Description:TODO

 * @author:ben

 * @time:2016年5月25日 下午3:03:54

 */

package com.twis.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.json.JSONObject;

public class MapUtil {
	public static Map<String, Object> getNewHashMap(Map<String, Object> mapObj, String nameList) {
		Map<String, Object> newMap = new HashMap<String, Object>();
		String[] nameArray = nameList.split(",");
		if (nameArray != null) {
			for (int i = 0; i < nameArray.length; i++) {
				String sName = nameArray[i];
				if (!StringUtil.isEmptyOrNull(sName)) {
					if (mapObj.containsKey(sName)) {
						newMap.put(sName, mapObj.get(sName));
					}
				}
			}
		}
		return newMap;
	}

	public static Map<String, Object> getNewHashMapByToken(Map<String, Object> sourceMap) {
		Map<String, Object> newMap = new HashMap<String, Object>();
		if (sourceMap != null) {
			if (sourceMap.containsKey("token"))
			  newMap.put("token", sourceMap.get("token"));
		}
		return newMap;
	}
	
	public static void copyToken(Map<String, Object> sourceMap, Map<String, Object> destMap) {
		if ((sourceMap != null) && (destMap != null)) {
			if (sourceMap.containsKey("token"))
				destMap.put("token", sourceMap.get("token"));
		}
	}
	
	public static Map<String, Object> getNewHashMapByPageInf(Map<String, Object> sourceMap) {
		return MapUtil.getNewHashMapByPageInf(sourceMap, "");
	}
	
	public static int getQueryCurrentPage(Map<String, Object> map, int defValue) {
		return StringUtil.getMapStringValue(map, "currentPage", defValue);
	}

	
	public static int getQueryPageSize(Map<String, Object> map, int defValue) {
		return (int)StringUtil.getMapStringValue(map, "pageSize", defValue);
	}
	
	public static int getQueryPageSizeOfMaxValue(Map<String, Object> map, int maxvalue) {
		int curValue = StringUtil.getMapStringValue(map, "pageSize", maxvalue);
		if (curValue > maxvalue) {
			curValue = maxvalue;
		}
		return curValue;
	}
	
	public static Map<String, Object> getNewHashMapByPageInf(Map<String, Object> sourceMap, String nameList) {
		String sList = "currentPage,pageSize";
		if (!StringUtil.isEmptyOrNull(nameList)) {
			sList += "," + nameList;
		}
		return MapUtil.getNewHashMap(sourceMap, sList);
	}
	
	public static void setMapDefaultPageInf(Map<String, Object> map) {
		setMapDefaultPageInf(map, 1, 20);
	}
	
	public static  Map<String, Object> getNewHashMapByPageInf(int currentPage, int pageSize) {
		Map<String, Object> newMap = new HashMap<String, Object>();
		setMapDefaultPageInf(newMap, currentPage, pageSize);
		return newMap;
	}
	
	public static void setMapDefaultPageInf(Map<String, Object> map, int currentPage, int pageSize) {
		if (map != null) {
			if (!map.containsKey("currentPage")) {
				map.put("currentPage", currentPage);
			}
			
			if (!map.containsKey("pageSize")) {
				map.put("pageSize", pageSize);
			}
		}
	}
	
	public static void setMapDefaultPageInfByMaxPageSize(Map<String, Object> map, int currentPage, int maxPageSize) {
		if (map != null) {
			if (!map.containsKey("currentPage")) {
				map.put("currentPage", currentPage);
			}
			
			if (!map.containsKey("pageSize")) {
				map.put("pageSize", maxPageSize);
			} else {
				int curValue = StringUtil.getMapStringValue(map, "pageSize", maxPageSize);
				if (curValue > maxPageSize) {
					map.put("pageSize", maxPageSize);
				}
			}
		}
	}
	
	public static void setMapPageInf(Map<String, Object> map, int currentPage, int pageSize) {
		if (map != null) {
			map.put("currentPage", currentPage);
			map.put("pageSize", pageSize);
		}
	}

	public static Map<String, Object> getPreciseMapValue(Object value) {
		Map<String, Object> obj = new LinkedHashMap<String, Object>();
		obj.put("precise", value);
		return obj;
	}
	    
    public static Map<String, Object> getOrderByMapValue(String fieldName, boolean desc) {
		Map<String, Object> obj = new LinkedHashMap<String, Object>();
		if (desc) {
			obj.put(fieldName, "desc");
		} else {
			obj.put(fieldName, "asc");
		}
		return obj;
    }
    
    public static void setOrderField(Map<String, Object> map, String fieldName, boolean desc) {
    	if (map != null) {
    		map.put("orderBy", getOrderByMapValue(fieldName, desc));
    	}	
    }
    
    public static void seLessThanField(Map<String, Object> map, String fieldName, String value) {
    	if (map != null) {
    		ArrayList<String> aryValue = new ArrayList<String>();
    		aryValue.add("");
    		aryValue.add(value);
    		map.put(fieldName, aryValue);
    	}	
    }
    
    public static void processMapOfIdByLessThan(Map<String, Object> map) {
    	if (map.containsKey("id")) {
    		String id = StringUtil.getMapStringValue(map, "id", "");
    		map.remove("id");
    		seLessThanField(map, "id", id);
    	}
    }

	public static Map<String, Object> stringToMap(String agr) {
		Map<String, Object> re = new HashMap<String, Object>();
		String[] kvs = agr.split(";");
		for (String kv : kvs) {
			if (!kv.contains(":="))
				return null;
			String[] k_v = kv.split(":=");
			re.put(k_v[0], k_v[1]);
		}
		return re;
	}

	public static Map<String, Object> JSONObject2Map(Object object) {
		Map<String, Object> data = new HashMap<String, Object>();
		JSONObject jsonObject = JSONObject.fromObject(object);
		@SuppressWarnings("rawtypes")
		Iterator it = jsonObject.keys();
		while (it.hasNext()) {
			String key = String.valueOf(it.next());
			Object value = jsonObject.get(key);
			data.put(key, value);
		}
		return data;
	}
}
