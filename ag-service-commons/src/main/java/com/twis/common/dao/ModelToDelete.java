package com.twis.common.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.twis.model.Column;
import com.twis.model.Table;
import com.twis.common.exception.ModelException;

/**
 * 
 * @author yxm
 *
 */
public abstract class ModelToDelete  {

	/**
	 * 把 model 转化成可执行的删除语句
	 * 返回map对象 :key:sql->sql语句,key:para->参数(Object数组)
	 * sql:delete table where id = ?
	 * para:Object[]{Integer}
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static Map<String, Object> convertDeleteSql(Object model) throws Exception {
		return convertDeleteSql(model,null);
	}

	/**
	 * 把 model 转化成可执行的删除语句
	 * 返回map对象 :key:sql->sql语句,key:para->参数(Object数组)
	 * sql:delete table where id = ?
	 * para:Object[]{Integer}
	 * 数据以parameter数据为准
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static synchronized Map<String, Object> convertDeleteSql(Object model, Map<String, Object> parameter) throws Exception {
		StringBuffer sqlDelete = new StringBuffer();
		List<Object> para= new ArrayList<Object>();
		Map<String, Object> convertSql = new HashMap<String, Object>();
		
		
		Table table = model.getClass().getAnnotation(Table.class);
		sqlDelete.append(" delete ");
		sqlDelete.append(table.name());
		StringBuffer sqlWhere = new StringBuffer();
		Field[] fields = model.getClass().getDeclaredFields();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				if (column.isId().equals("true")) {
					sqlWhere.append(" where " + column.name() + " = ? ");
					if(parameter!=null){
						if(parameter.containsKey(field.getName())){
							Object obj = ModelToSqlValue.convertParameter(column,field, parameter.get(field.getName()));
							para.add(obj);
						}else{
							throw new ModelException("ID不能为空！");
						}
					}else{
						Object value = field.get(model);
						para.add(value);
					}
				}
			}
		}
		sqlDelete.append(sqlWhere);
		
		convertSql.put("sql", sqlDelete.toString());
		convertSql.put("para", para);
		return convertSql;
	}

}
