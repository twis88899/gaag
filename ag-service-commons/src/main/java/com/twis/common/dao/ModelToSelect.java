package com.twis.common.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;

import com.twis.model.Column;
import com.twis.model.JoinTable;
import com.twis.model.Table;

/**
 * 
 * @author yxm
 *
 */
public abstract class ModelToSelect {

	/**
	 * 把单model 转化成可执行的查询语句
	 * 返回map对象 :key:sql->sql语句,key:para->参数(Object数组)
	 * sql:select * from table where column=? and column=? ……
	 * para:Object[]{String,Integer……}
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static Map<String, Object> convertSelectSql(Object model) throws Exception {
		return convertSelectSql(model,null);
	}

	/**
	 * 把单model 转化成可执行的查询语句
	 * 返回map对象 :key:sql->sql语句,key:para->参数(Object数组)
	 * sql:select * from table where column=? and column=? ……
	 * para:Object[]{String,Integer……}
	 * 数据以parameter数据为准
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static  Map<String, Object> convertSelectSql(Object model, Map<String, Object> parameter) throws Exception {
		return convertMoreModelSelectSql(model,false,parameter,1);
	}
	
	/**
	 * 把关联 model 转化成可执行的查询语句
	 * 返回map对象 :key:sql->sql语句,key:para->参数(Object数组)
	 * sql:select * from table where column=? and column=? ……
	 * para:Object[]{String,Integer……}
	 * 数据以parameter数据为准
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static  Map<String, Object> convertMoreModelSelectSql(Object model,Map<String, Object> parameter) throws Exception {
		return convertMoreModelSelectSql(model,true,parameter,2);
	}
	
	/**
	 * 把关联 model 转化成可执行的查询语句 
	 * 返回map对象 :key:sql->sql语句,key:para->参数(Object数组)
	 * sql:select * from table where column=? and column=? ……
	 * para:Object[]{String,Integer……}
	 * 数据以json数据为准
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static  Map<String, Object> convertMoreModelSelectSql(Object model) throws Exception {
		return convertMoreModelSelectSql(model,true,null,2);
	}
	
	/**
	 * 把关联 model 转化成可执行的查询语句,并指定查询级别
	 * 返回map对象 :key:sql->sql语句,key:para->参数(Object数组)
	 * sql:select * from table where column=? and column=? ……
	 * para:Object[]{String,Integer……}
	 * 数据以parameter数据为准
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static  Map<String, Object> convertMoreModelSelectSql(Object model,Map<String, Object> parameter,int level) throws Exception {
		return convertMoreModelSelectSql(model,true,parameter,level);
	}
	
	/**
	 * 把关联 model 转化成可执行的查询语句 并指定查询级别
	 * 返回map对象 :key:sql->sql语句,key:para->参数(Object数组)
	 * sql:select * from table where column=? and column=? ……
	 * para:Object[]{String,Integer……}
	 * 数据以json数据为准
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public static  Map<String, Object> convertMoreModelSelectSql(Object model,int level) throws Exception {
		return convertMoreModelSelectSql(model,true,null,level);
	}
	
	/**
	 * 把关联 model 转化成可执行的查询语句
	 * 返回map对象 :key:sql->sql语句,key:para->参数(Object数组)
	 * sql:select * from table where column=? and column=? ……
	 * para:Object[]{String,Integer……}
	 * 数据以parameter数据为准
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	private static  Map<String, Object> convertMoreModelSelectSql(Object model,Boolean isMoreModelSelect, Map<String, Object> parameter,int level) throws Exception {
		return modelToSelect(model,isMoreModelSelect,parameter,level);
	}
	
	
	

	
	/**
	 * model生成单表查询sql
	 * @param model
	 * @param isMoreModelSelect
	 * @param map
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException 
	 */
	private static synchronized Map<String, Object> modelToSelect(Object model,Boolean isMoreModelSelect,Map<String,Object> parameter,int level) throws Exception {
		StringBuffer sqlSelect = new StringBuffer();
		
		List<Object> para= new ArrayList<Object>();
		Map<String, Object> convertSql = new HashMap<String, Object>();
		
		Table table = model.getClass().getAnnotation(Table.class);
		Field[] fields = model.getClass().getDeclaredFields();
		
		/*设置查询字段 */
		sqlSelect.append(" select ");
		setSelectColumn(model,sqlSelect,"t");
		if(isMoreModelSelect){
//			for (Field field : fields) {
//				JoinTable joinTable = field.getAnnotation(JoinTable.class);
//				if(joinTable!=null){
//					setSelectColumn(field.getType().newInstance(),sqlSelect,joinTable.prefix());
//				}
//			}
			setMoreModelSelectColumn(fields,sqlSelect,1,level);
		}
		sqlSelect.append(" \n from ");
		/*设置查询字段 */
		
		/*设置查询表 */
		sqlSelect.append( table.name() + " t ");
		if(isMoreModelSelect){
//			for (Field field : fields) {
//				JoinTable joinTable = field.getAnnotation(JoinTable.class);
//				if(joinTable!=null){
//					sqlSelect.append(" \n left join " + field.getType().getAnnotation(Table.class).name());
//					sqlSelect.append(" "+joinTable.prefix() +" " );
//					sqlSelect.append(" on t."+joinTable.joinMasterColumn() +"=" + joinTable.prefix()+"."+joinTable.joinExteriorColumn() + (joinTable.joinExteriorWhere()) );
//				}
//			}
			
			setMoreModelSelectTable(fields,sqlSelect,"t",1,level);
		}
		sqlSelect.append(" \n where 1=1 ");
		/*设置查询表 */
		
		/*设置查询条件 */
		setSelectWhere( model,sqlSelect,true,isMoreModelSelect,"t", "",parameter,para,1);
		/*设置查询条件 */
		
		
		/*设置order by*/
		setSelectOrderBy(model,sqlSelect,parameter,"t","",true,1);
		/*设置order by*/
		
		
		
		
		/*设置group by*/
//		setSelectGroupBy(model,sqlSelect,map,"t","",true);
		/*设置group by*/
		convertSql.put("sql", sqlSelect.toString());
		convertSql.put("para", para);
		
		
		
		return convertSql;
	}
	
	private static synchronized void setMoreModelSelectColumn(Field[] fields,StringBuffer sqlSelect ,int i,int level) throws Exception{
		++i;
		for (Field field : fields) {
			JoinTable joinTable = field.getAnnotation(JoinTable.class);
			if(joinTable!=null){
				setSelectColumn(field.getType().newInstance(),sqlSelect,joinTable.prefix());
				if(i<=level-1){
					setMoreModelSelectColumn(field.getType().newInstance().getClass().getDeclaredFields(),sqlSelect ,i,level);
				}
			}
		}
	}
	
	private static synchronized void setMoreModelSelectTable(Field[] fields,StringBuffer sqlSelect,String prefix,int i,int level) throws Exception{
		++i;
		for (Field field : fields) {
			JoinTable joinTable = field.getAnnotation(JoinTable.class);
			if(joinTable!=null){
				sqlSelect.append(" \n left join " + field.getType().getAnnotation(Table.class).name());
				sqlSelect.append(" "+joinTable.prefix() +" " );
				sqlSelect.append(" on "+prefix+"."+joinTable.joinMasterColumn() +"=" + joinTable.prefix()+"."+joinTable.joinExteriorColumn() + (joinTable.joinExteriorWhere()) );
				if(i<=level-1){
					setMoreModelSelectTable(field.getType().newInstance().getClass().getDeclaredFields(),sqlSelect,joinTable.prefix(),i,level);
				}
			}
		}
	}
	
	/**
	 * 设置order by 条件
	 * @param model
	 * @param sqlSelect
	 * @param map
	 * @param columnPrefix
	 * @param modelPrefix
	 * @param isRecursion
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private static void setSelectOrderBy(Object model,StringBuffer sqlSelect,Map<String,Object> parameter,String columnPrefix,String modelPrefix,Boolean isRecursion,int i) throws Exception{
		++i;
//		{"orderBy":{"userId":"desc"}}
		if(parameter==null) {return;}
		
		Map<String ,Object>  orderBy = null ;
//		if(orderBy==null){return;}
		
		String orderField = "" ;
		String orderSort="";

		if(parameter.containsKey("orderBy")){
			orderBy = (Map<String, Object>) parameter.get("orderBy");
		}
		if(parameter.containsKey("orderField")){
			orderField = parameter.get("orderField").toString();
		}
		if(parameter.containsKey("orderSort")){
			orderSort = parameter.get("orderSort").toString();
		}
		
		Field[] fields = model.getClass().getDeclaredFields();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			@SuppressWarnings("unchecked")
			String key = modelPrefix.equals("")?field.getName():((modelPrefix+".")+field.getName());
			
			if(orderBy!=null&&orderBy.containsKey(key)){
				if(sqlSelect.toString().indexOf("order")<=-1){
					sqlSelect.append(" \n order by "+columnPrefix+"." + column.name() +" "+orderBy.get(key));
				}else{
					sqlSelect.append(" ,"+columnPrefix+"." + column.name() +" "+orderBy.get(key));
				}
			}
			if(orderField.equals(key)){
				if(sqlSelect.toString().indexOf("order")<=-1){
					sqlSelect.append(" \n order by "+columnPrefix+"." + column.name() +" "+orderSort);
				}else{
					sqlSelect.append(" ,"+columnPrefix+"." + column.name()+" "+orderSort );
				}
			}
			
			JoinTable joinTable = field.getAnnotation(JoinTable.class);
			if(joinTable!=null&&isRecursion){
				if(i<=2){
					setSelectOrderBy( field.getType().newInstance(),sqlSelect, parameter,joinTable.prefix(), field.getName(),true,i);
				}
			}
		}
		
	}
	
	private static void setSelectGroupBy(Object model,StringBuffer sqlSelect,Map<String,Object> map,String columnPrefix,String modelPrefix,Boolean isRecursion) throws Exception{
//		Object groupBy = map.get("groupBy");
//		if(groupBy==null){return;}
//		
//		Field[] fields = model.getClass().getDeclaredFields();
//		for (Field field : fields) {
//			Column column = field.getAnnotation(Column.class);
//			@SuppressWarnings("unchecked")
//			Map<String ,Object> groupByMap = (Map<String, Object>) groupBy;
//			String key = modelPrefix.equals("")?field.getName():((modelPrefix+".")+field.getName());
//			if(groupByMap.containsKey(key)){
//				if(sqlSelect.toString().indexOf("order")<=-1){
//					sqlSelect.append(" \n order by "+columnPrefix+"." + column.name() +" "+groupByMap.get(key));
//				}else{
//					sqlSelect.append(" ,"+columnPrefix+"." + column.name() +" "+groupByMap.get(key));
//				}
//			}
//			
//			
//			
//			JoinTable joinTable = field.getAnnotation(JoinTable.class);
//			if(joinTable!=null&&isRecursion){
//				setSelectOrderBy( field.getType().newInstance(),sqlSelect, map,joinTable.prefix(), field.getName(),false);
//			}
//		}
	}
	
	/**
	 * 组装where查询条件
	 * @param model
	 * @param sqlSelect
	 * @param isRecursion
	 * @param isMoreModelSelect
	 * @param prefix
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException 
	 */
	private static void setSelectWhere(Object model,StringBuffer sqlSelect,Boolean isRecursion,Boolean isMoreModelSelect,String columnPrefix,String modelPrefix,Map<String,Object> parameter,List<Object> para,int i) throws Exception{
		++i;
		Field[] fields = model.getClass().getDeclaredFields();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				field.setAccessible(true);
				Class<?> type = field.getType();
				
				if (type.equals(String.class)) {
					setStringWhere(model,field,sqlSelect,columnPrefix+"." + column.name(),modelPrefix.equals("")?field.getName():((modelPrefix+".")+field.getName()),parameter,para);
				} else {
					setOtherWhere(model,column,field,sqlSelect,columnPrefix+"." + column.name(),modelPrefix.equals("")?field.getName():((modelPrefix+".")+field.getName()),parameter,para);
				}
			}
			JoinTable joinTable = field.getAnnotation(JoinTable.class);
			if(joinTable!=null&&isRecursion&&isMoreModelSelect){
				if(i<=2){
					setSelectWhere( field.getType().newInstance(),sqlSelect,true,isMoreModelSelect,joinTable.prefix(),field.getName(),parameter,para,i);
				}
			}
		}
	}
	
	/**
	 * 设置字符串查询
	 * @param sqlSelect
	 * @param whereColumn
	 * @param mapKey
	 * @param map
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@SuppressWarnings("unchecked")
	private static void setStringWhere(Object model,Field field,StringBuffer sqlSelect,String whereColumn,String mapKey,Map<String,Object> parameter,List<Object> para) throws Exception{
//		{"name":"刘方"}  //模糊查询
//		{"name":{"fuzzy":"刘方"}}  //模糊查询
//		{"name":{"precise":"刘方"}} //精确查询
		if(parameter!=null){
			if(parameter.containsKey(mapKey)){
				Object obj = parameter.get(mapKey);
				if(obj.getClass().equals(LinkedHashMap.class))
				{
					//模糊查询
					if(((LinkedHashMap<String ,Object>)obj).containsKey("fuzzy")){
						sqlSelect.append(" and "+whereColumn + " like ? ");
						para.add((((LinkedHashMap<String ,Object>)obj).get("fuzzy")).toString());
					}
					//精确查询
					else if(((LinkedHashMap<String ,Object>)obj).containsKey("precise")) {
						sqlSelect.append(" and "+whereColumn + " = ? ");
						para.add((((LinkedHashMap<String ,Object>)obj).get("precise")).toString());
						
					}
				}else{
					sqlSelect.append(" and "+whereColumn + " like ? ");
					para.add("%"+obj+"%");
				}
			}
		}else{
			Object value = field.get(model);
			if(value!=null){
				sqlSelect.append(" and "+whereColumn + " like ? ");
				para.add("%"+value+"%");
			}
		}
		
		
	}
	
	/**
	 * 
	 * @param sqlSelect
	 * @param whereColumn
	 * @param mapKey
	 * @param map
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	@SuppressWarnings("unchecked")
	private static void setOtherWhere(Object model,Column column,Field field,StringBuffer sqlSelect,String whereColumn,String mapKey,Map<String,Object> parameter,List<Object> para) throws Exception{
		if(parameter!=null){
			if(parameter.containsKey(mapKey)){
				Object obj = parameter.get(mapKey);
				//范围查询
//				{"age":["10","20"]} 范围查询  >= and <=
//				{"age":["10",""]} 范围查询    >= 
//				{"age":["","20"]} 范围查询    <=
//				{"age":"10"} 非范围查询 
				if(obj.getClass().equals(ArrayList.class))
				{
					if(StringUtils.hasText((((ArrayList<?>)obj).get(0)).toString())){
						sqlSelect.append(" and "+whereColumn + " >= ? ");
						Object value = ModelToSqlValue.convertParameter(column,field, ((ArrayList<?>)obj).get(0));
						para.add(value);
					}
					if(StringUtils.hasText((((ArrayList<?>)obj).get(1)).toString())){
						sqlSelect.append(" and "+whereColumn + " <= ? ");
						Object value = ModelToSqlValue.convertParameter(column,field, ((ArrayList<?>)obj).get(1));
						para.add(value);
					}
				}
				//in/not in 查询
//				{"id":{"in":"1,2,3,4"}}  //模糊查询
//				{"id":{"notIn":"1,2,3,4"}}  //模糊查询
				else if(obj.getClass().equals(LinkedHashMap.class)){
					//in查询
					if(((LinkedHashMap<String ,Object>)obj).containsKey("in")){
						sqlSelect.append(" and "+whereColumn + " in ("+(((LinkedHashMap<String ,Object>)obj).get("in")).toString()+")  ");
					}
					//notIn查询
					else if(((LinkedHashMap<String ,Object>)obj).containsKey("notIn")) {
						sqlSelect.append(" and "+whereColumn + " in ("+(((LinkedHashMap<String ,Object>)obj).get("notIn")).toString()+")  ");
						
					}
				}
				//精确查询
				else{
					Object value = ModelToSqlValue.convertParameter(column,field, obj);
					sqlSelect.append(" and "+whereColumn + " = ? ");
					if (java.util.Date.class.equals(value.getClass())){
						//sqlSelect.append(" and "+whereColumn + " = to_date(?,'yyyy-mm-dd') ");
						para.add(value);
					}else{
						if(value.getClass().equals(Boolean.class)){
							para.add(ModelToSqlValue.convertParameterBoolean(value));
						}else{
							para.add(obj);
						}
					}
					
				}
			}
			
		}else{
			Object value = field.get(model);
			if(value!=null){
				sqlSelect.append(" and "+whereColumn + " = ? ");
				if(value.getClass().equals(Boolean.class)){
					para.add(ModelToSqlValue.convertParameterBoolean(value));
				}else{
					para.add(value);
				}
			}
		}
	}
	
	
	
	
	/**
	 * 设置查询字段
	 * @param model
	 * @param sqlSelect
	 * @param prefix
	 */
	private static void setSelectColumn(Object model,StringBuffer sqlSelect,String prefix){
		sqlSelect.append(sqlSelect.length()>10?"\n":"");
		Field[] fields = model.getClass().getDeclaredFields();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				sqlSelect.append((sqlSelect.length()>10 ? ",":"") +prefix+"."+column.name() +  (" "+( prefix.equals("t")?"":prefix+column.name())));
			}
		}
	}
	
	public static String  getSelectColumnSqlStr(Object model, String prefix) {
		StringBuffer sqlSelect = new StringBuffer();
		Field[] fields = model.getClass().getDeclaredFields();
		for (Field field : fields) {
			Column column = field.getAnnotation(Column.class);
			if (column != null) {
				sqlSelect.append("," + prefix + column.name());
			}
		}
		String sStr = sqlSelect.toString();
		if ((sStr != null) && (sStr.length() > 0))
			return sStr.substring(1);
		else 
			return sStr;
	}

	public static void main(String[] ags) throws Exception{
		//Map<String, Object>  map =ModelToSelect.convertMoreModelSelectSql(new CheckInocPlanFrameModel(),3);
		//String sql = getSelectColumnSqlStr(new CompanyModelVo(), "c.");
		//System.out.println(sql);
	}
}
