package com.twis.common.dao.impl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.twis.common.dao.BaseExDao;
import com.twis.common.dao.ModelToSelect;
import com.twis.common.exception.ModelException;
import com.twis.common.utils.BeanToMapUtil;
import com.twis.common.utils.StringUtil;

public class BaseExDaoOfMysqlImpl<T> extends BaseDaoOfMysqlImpl implements BaseExDao {
	protected Class<T> entityClass;
	
	//模型的嵌套层次
	protected int getSelectSqlLinkNum() {
		return 2;
	}
	
	public BaseExDaoOfMysqlImpl() {
		super();
		resovleClazzInfo();
	    //System.out.println(entityClass.getSimpleName());
	}
    
	@SuppressWarnings("unchecked")
	private void resovleClazzInfo() {
        Type genType = getClass().getGenericSuperclass();  
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        entityClass = (Class<T>) params[0];
    }
	
	@Override
	public Map<String, Object> queryCorrelation(Map<String, Object> parameter) throws Exception {
		Map<String, Object> map = ModelToSelect.convertMoreModelSelectSql(entityClass.newInstance(), parameter, getSelectSqlLinkNum());
		String sql = map.get("sql").toString();
		
		@SuppressWarnings("unchecked")
		List<Object> agrs = (List<Object>) map.get("para");
		// 判断是否分页查询
		if (parameter.containsKey("currentPage") ) {
			if (parameter.containsKey("pageSize")) {
				return queryNavigator(sql,agrs.toArray(),Integer.valueOf(parameter.get("currentPage").toString()),
						Integer.valueOf(parameter.get("pageSize").toString()),
						entityClass.newInstance());
			} else {
				return queryNavigator(sql, agrs.toArray(), 
						Integer.valueOf(parameter.get("currentPage").toString()),
						entityClass.newInstance());
			}
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<Object> list = query(sql,agrs.toArray(), entityClass);
		returnMap.put("data", list);
		return returnMap;
	}
	
	private List<Object> queryListByComplex(Map<String, Object> parameter) throws Exception {
		Map<String, Object> map = ModelToSelect.convertMoreModelSelectSql(entityClass.newInstance(), parameter, getSelectSqlLinkNum());
		String sql = map.get("sql").toString();
		@SuppressWarnings("unchecked")
		List<Object> agrs = (List<Object>) map.get("para");
		List<Object> list = query(sql,agrs.toArray(), entityClass);
		return list;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public T queryModel(Object parameter) throws Exception {
		Map<String, Object> mapObj = null;
		if (parameter instanceof Map) {
			mapObj = (Map<String, Object>) parameter;
		}else {
			mapObj = BeanToMapUtil.objectToPreciseQueryMap(parameter);
		}
		//只用于精确查找
		
		List<Object> list = queryListByComplex(mapObj);
		if (list != null){
			if (list.size() == 1)
				return (T)list.get(0);
			else if (list.size() > 1)
				throw new ModelException("size more than 1!");
			else return null;
		} else {
			return null;
		}
	}
	
	public T queryModelById(Long id) throws Exception {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		mapObj.put("id", id);
		return queryModel(mapObj);
	}
	
	public T queryModelById(Map<String, Object> parameter) throws Exception {
		Long id = StringUtil.getMapStringValue(parameter, "id", -1L);
		if (id>=0) {
			return queryModelById(id);
		} else {
			return null;
		}
	}
	
	@Override
	public Map<String, Object> querySimple(Map<String, Object> parameter) throws Exception {
		parameter.remove("babyid");//日记和宝宝没关系，只和用户有关
		//parameter.put("orderBy", MapUtil.getOrderByMapValue("recorddate", true));
		Map<String, Object> map = ModelToSelect.convertSelectSql(entityClass.newInstance(), parameter);
		
		String sql = map.get("sql").toString();
		
		@SuppressWarnings("unchecked")
		List<Object> agrs = (List<Object>) map.get("para");
		// 判断是否分页查询
		if (parameter.containsKey("currentPage") ) {
			if (parameter.containsKey("pageSize")) {
				return queryNavigator(sql,agrs.toArray(),Integer.valueOf(parameter.get("currentPage").toString()),
						Integer.valueOf(parameter.get("pageSize").toString()),
						entityClass.newInstance());
			} else {
				return queryNavigator(sql, agrs.toArray(), 
						Integer.valueOf(parameter.get("currentPage").toString()),
						entityClass.newInstance());
			}
		}
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<Object> list = query(sql,agrs.toArray(), entityClass);
		returnMap.put("data", list);
		return returnMap;
	}
	
	@SuppressWarnings("unchecked")
	public  List<Object> query(Object parameter) throws Exception {
		if (parameter instanceof Map) {
			return super.query(entityClass.newInstance(), (Map<String, Object>) parameter);
		} else {
			return super.query(entityClass.newInstance(), BeanToMapUtil.objectToMapWithNotNull(parameter));
		}	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int insert(Object parameter) throws Exception {
		if (parameter instanceof Map) {
			return super.insert(entityClass.newInstance(), (Map<String, Object>) parameter);
		} else {
			return super.insert(entityClass.newInstance(), BeanToMapUtil.objectToMapWithNotNull(parameter));
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Long insertOfSequence(Object parameter) throws Exception {
		Map<String, Object> mapObj = null;
		if (parameter instanceof Map) {
			mapObj = (Map<String, Object>) parameter;
		} else {
			mapObj = BeanToMapUtil.objectToMapWithNotNull(parameter);
		}
		Long iSeq = this.querySequence();
		mapObj.put("NextVal", iSeq);
		int i = super.insert(entityClass.newInstance(), mapObj);
		if (i>=0) {
			return iSeq;
		} else {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int update(Object parameter) throws Exception {
		if (parameter instanceof Map) {
			return super.update(entityClass.newInstance(), (Map<String, Object>) parameter);
		}else {
			return super.update(entityClass.newInstance(), BeanToMapUtil.objectToMapWithNotNull(parameter));
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public int delete(Object parameter) throws Exception {
		if (parameter instanceof Map) {
			return super.delete(entityClass.newInstance(), (Map<String, Object>) parameter);
		}else {
			return super.delete(entityClass.newInstance(), BeanToMapUtil.objectToMapWithNotNull(parameter));
		}
	}
	
	@Override
	public List<Object> queryEx(Map<String, Object> parameter) throws Exception {
		return query(entityClass.newInstance(), parameter);
	}
	
	@Override
	public Long querySequence() throws Exception {
		return querySequence(entityClass.newInstance());
	}
	
}