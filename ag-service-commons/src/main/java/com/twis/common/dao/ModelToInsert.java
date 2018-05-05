package com.twis.common.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.twis.model.Column;
import com.twis.model.Table;

/**
 * 
 * @author yxm
 *
 */
public abstract class ModelToInsert {

	/**
	 * 把 model 转化成可执行的插入语句
	 * 返回map对象 :key:sql->sql语句,key:para->参数(Object数组)
	 * sql:insert into table (column,column，column……) values(?,?,?……)
	 * para:Object[]{String,Integer……}
	 * @throws Exception 
	 */
	public static Map<String, Object> convertInsertSql(Object model) throws Exception {
		return convertInsertSql(model,null);
	}

	/**
	 * 把 model 转化成可执行的插入语句
	 * 返回map对象 :key:sql->sql语句,key:para->参数(Object数组)
	 * sql:insert into table (column,column，column……) values(?,?,?……)
	 * para:Object[]{String,Integer……}
	 * 数据以json数据为准
	 * @throws Exception 
	 */
	public static synchronized Map<String, Object> convertInsertSql(Object model, Map<String, Object> parameter) throws Exception {
		StringBuffer sqlInsert = new StringBuffer();
		List<Object> para= new ArrayList<Object>();
		Map<String, Object> convertSql = new HashMap<String, Object>();
		
		
		Table table = model.getClass().getAnnotation(Table.class);
		sqlInsert.append(" insert into ");
		sqlInsert.append(table.name());
		sqlInsert.append(" ( ");
		int isqlInsert = sqlInsert.toString().length();
		StringBuffer sqlValue = new StringBuffer();
		sqlValue.append(" values ( ");
		int isqlValue = sqlValue.toString().length();
		Field[] fields = model.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				if(parameter!=null){
					ModelToSqlValue.checkRequired(column, parameter.get(field.getName()));
					ModelToSqlValue.checkRequiredByNull(column, parameter.get(field.getName()));
					if(parameter.containsKey(field.getName())){
						convertSql(sqlInsert,sqlValue,column,isqlInsert,isqlValue);
						Object obj = ModelToSqlValue.convertParameter(column,field, parameter.get(field.getName()));
						if(obj.getClass().equals(Boolean.class)){
							para.add(ModelToSqlValue.convertParameterBoolean(obj));
						}else{
							para.add(obj);
						}
						
					}
				}else{
					Object value = field.get(model);
					ModelToSqlValue.checkRequired(column, value);
					ModelToSqlValue.checkRequiredByNull(column, value); //add by ben
					if (value != null) {
						convertSql(sqlInsert,sqlValue,column,isqlInsert,isqlValue);
						if(value.getClass().equals(Boolean.class)){
							para.add(ModelToSqlValue.convertParameterBoolean(value));
						}else{
							para.add(value);
						}
					}
				}
			}
		}
		sqlInsert.append(" ) ");
		sqlValue.append(" ) ");

		sqlInsert.append(sqlValue);
		sqlInsert.toString();
		
		convertSql.put("sql", sqlInsert.toString());
		convertSql.put("para", para);
		return convertSql;
	}
	

	private static void convertSql(StringBuffer sqlInsert,StringBuffer sqlValue,Column column,int isqlInsert,int isqlValue){
		sqlInsert.append(sqlInsert.toString().length() > isqlInsert ? ",": "");
		sqlInsert.append(column.name());
		sqlValue.append(sqlValue.toString().length() > isqlValue ? ",": "");
		sqlValue.append(" ? ");
	}
	
	
}
