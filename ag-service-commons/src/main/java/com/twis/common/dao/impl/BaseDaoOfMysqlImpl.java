package com.twis.common.dao.impl;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.twis.model.Column;
import com.twis.model.Table;
import com.twis.common.IDFactory;
import com.twis.common.SnowflakeIdWorker;
import com.twis.common.dao.BaseDao;
import com.twis.common.dao.Jdbc;
import com.twis.common.dao.ModelToDelete;
import com.twis.common.dao.ModelToInsert;
import com.twis.common.dao.ModelToSelect;
import com.twis.common.dao.ModelToUpdate;
import com.twis.common.dao.ResultSetToModel;
import com.twis.common.exception.ModelException;
import com.twis.common.pageHelper.IPage;
import com.twis.common.pageHelper.impl.PageHelper;

/**
 * 
 * @author yswh
 *
 */
public class BaseDaoOfMysqlImpl extends Jdbc implements BaseDao {
	@Autowired
	protected JdbcTemplate jdbcTemplateOfMysql;
	private final static IDFactory worker = new SnowflakeIdWorker(2095,999);
	/**
	 * 获取总条数
	 *  
	 * @param sql
	 * @param args
	 * @return
	 */
	protected long getCount(String sql, Object[] args) {
		sql = " select count(1) as count from ( " + sql + " )";
		Long count = (Long) jdbcTemplateOfMysql.query(sql, args, new ResultSetExtractor<Long>() {
			@Override
			public Long extractData(ResultSet rs) throws SQLException, DataAccessException {
				rs.next();
				return rs.getLong(1);
			}
		});
		return count;

	}
	
	protected String getFirstValue(String sql, String defVaule) {
	    String value = (String) jdbcTemplateOfMysql.query(sql,new ResultSetExtractor<String>() {
		      @Override
		      public String extractData(ResultSet rs) throws SQLException, DataAccessException {
			      if (rs.next())
			    	  return rs.getString(1);
			      else 
			    	  return defVaule;
		      }
		});
	    return value;
	}
	
	
	protected String getFirstValueByArgs(String sql,Object[] args, String defVaule) {
	    String value = (String) jdbcTemplateOfMysql.query(sql, args ,new ResultSetExtractor<String>() {
		      @Override
		      public String extractData(ResultSet rs) throws SQLException, DataAccessException {
			      if (rs.next())
			    	  return rs.getString(1);
			      else 
			    	  return defVaule;
		      }
		});
	    return value;
	}

	/**
	 * 获取分页数据
	 *
	 * @param _totalrows
	 * @param _currentPage
	 * @param _pageSize
	 * @return
	 */
	protected IPage getPage(int _totalrows, int _currentPage, int _pageSize) {
		return new PageHelper(_totalrows, _currentPage, _pageSize).getPage();
	}

	/**
	 * 获取分页数据
	 *
	 * @param _totalrows
	 * @param _currentPage
	 * @return
	 */
	protected IPage getPage(int _totalrows, int _currentPage) {
		return new PageHelper(_totalrows, _currentPage).getPage();
	}

	@Override
	public int insert(Object model) throws Exception {
		return insert(model, null);
	}

	@Override
	public int insert(Object model, Map<String, Object> parameter) throws Exception {
		if (parameter != null) {
			TimeZone tz = TimeZone.getTimeZone("Asia/Shanghai");
			TimeZone.setDefault(tz);
			if (!parameter.containsKey("createDate")) {
				parameter.put("createDate", new Date(System.currentTimeMillis()));// 创建日期
			}
			if (!parameter.containsKey("version")) {
				parameter.put("version", 1);// 版本号
			}
		}
		Map<String, Object> map = ModelToInsert.convertInsertSql(model, parameter);
		return insert(map.get("sql").toString(), ((ArrayList<?>) map.get("para")).toArray());
	}

	@Override
	public int insert(String sql) {
		return insert(sql, new Object[] {});
	}

	@Override
	public int insert(String sql, Object[] agrs) {
		return update(sql, agrs);
	}

	@Override
	public int update(Object model) throws Exception {
		Field[] fields = model.getClass().getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if (field.getName().equals("version")) {
				int value = (int) field.get(model);
				field.set(model, value++);
			}
		}
		return update(model, null);
	}

	@Override
	public int update(Object model, Map<String, Object> parameter) throws Exception {
		if (parameter != null && parameter.containsKey("version")) {
			int version = Integer.valueOf(parameter.get("version").toString());
			if (version >= 99) {
				version = 1;
			} else {
				version += 1;
			}
			parameter.put("version", version);
			checkVersion(model, parameter);
		}
		Map<String, Object> map = ModelToUpdate.convertUpdateSql(model, parameter);
		/*
		 * 去掉分区直接修改,改用先删除再插入
		 * */
		/*if (map.containsKey("is_table_partitioning")){
			String enableSql = "ALTER TABLE FB_USER ENABLE ROW MOVEMENT";
			String disableSql = "ALTER TABLE FB_USER DISABLE ROW MOVEMENT";
			int i=0;
			update(enableSql);
			i = update(map.get("sql").toString(), ((ArrayList<?>) map.get("para")).toArray());
			update(disableSql);
			return i;
		}else{
			return update(map.get("sql").toString(), ((ArrayList<?>) map.get("para")).toArray());
		}*/
		
		return update(map.get("sql").toString(), ((ArrayList<?>) map.get("para")).toArray());
	}
	
	@Override
	public int updateEx(Object model, Map<String, Object> parameter) throws Exception {
		Map<String, Object> map = ModelToUpdate.convertUpdateSql(model, parameter);
		return update(map.get("sql").toString(), ((ArrayList<?>) map.get("para")).toArray());
	}

	@Override
	public int update(String sql) {
		return update(sql, new Object[] {});
	}

	@Override
	public int update(String sql, Object[] args) {
		printSQL(sql,args);
//		Long startTime = StatisticalTime.startTime();// 统计执行时间 开始
//		Long time1 = System.currentTimeMillis();
		int results = this.jdbcTemplateOfMysql.update(sql, args);
//		String argsString ="";
//		for(Object item:args){
//			argsString+=item.toString()+",";
//		}
//		System.out.println("更新操作语句："+sql+"\n,参数:"+argsString+"\n更新操作时间："+(System.currentTimeMillis()-time1)+"ms");
//		Long endTime = StatisticalTime.endTime();// 结束
//		StatisticalTime.seveTimeLogToMongo(startTime, endTime, sql);// 记录
		return results;
	}

	@Override
	public int delete(Object model) throws Exception {
		return delete(model, null);
	}

	@Override
	public int delete(Object model, Map<String, Object> parameter) throws Exception {
		if (!parameter.containsKey("id")) {
			return 0;
		}
		Map<String, Object> map = ModelToDelete.convertDeleteSql(model, parameter);
		return delete(map.get("sql").toString(), ((ArrayList<?>) map.get("para")).toArray());
	}

	@Override
	public int delete(String sql) {
		return delete(sql, new Object[] {});
	}

	@Override
	public int delete(String sql, Object[] agrs) {
		return update(sql, agrs);
	}

	@Override
	public List<Object> query(Object model) throws Exception {
		return query(model, null);
	}

	@Override
	public List<Object> query(Object model, Map<String, Object> parameter) throws Exception {
		Map<String, Object> map = ModelToSelect.convertSelectSql(model, parameter);
		return query(map.get("sql").toString(), ((ArrayList<?>) map.get("para")).toArray(), model.getClass());
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Object> query(String sql, Object[] args, Class<?> classz) {
		printSQL(sql,args);
//		Long startTime = StatisticalTime.startTime();// 统计执行时间 开始
		List<Object> list = new ArrayList<Object>();
		if (classz.equals(HashMap.class)) {
			list = (List<Object>) jdbcTemplateOfMysql.query(sql, args, new ResultSetExtractor() {
				@Override
				public List<Map<String, Object>> extractData(ResultSet rs) throws SQLException {

					List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
					ResultSetMetaData resultSetMetaData = rs.getMetaData();
					int colNum = 0;
					colNum = resultSetMetaData.getColumnCount();
					while (rs.next()) {
						Map<String, Object> resultMap = new HashMap<String, Object>();
						for (int i = 1; i <= colNum; i++) {
							String name = resultSetMetaData.getColumnName(i);
							resultMap.put(name.toLowerCase(), rs.getString(name));
						}
						list.add(resultMap);
					}
					return list;
				}
			});
		}
		else {

			list = (List<Object>) jdbcTemplateOfMysql.query(sql, args, new ResultSetExtractor() {
				@Override
				public List<Object> extractData(ResultSet rs) throws SQLException {
					List<Object> list = new ArrayList<Object>();
					while (rs.next()) {
						try {
							Object obj = classz.newInstance();
							ResultSetToModel.resultSetForModel(rs, obj);
							list.add(obj);
						} catch (InstantiationException | IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return list;
				}
			});
		}
		return list;
	}
	
	protected Object queryObjectOfOnlyOneBySql(String sql, Object[] args, Class<?> classz) throws Exception {
		String csql = "select * from (" + sql + ") where rownum=1";
		return queryObjectOfOnlyOne(csql, args, classz);
	}
	
	protected Object queryObjectOfOnlyOne(String sql, Object[] args, Class<?> classz) throws Exception {
		List<Object> list = query(sql, args, classz);

		if (list != null){
			if (list.size() == 1)
				return list.get(0);
			else if (list.size() > 1)
				throw new ModelException("size more than 1!");
		}

		return null;
	}

	public Map<String, Object> queryByMapResultData(String sql, Object[] args, Class<?> classz) {
		List<Object> list = query(sql, args, classz);
		return this.getMapData(list);
	}

	@Override
	public Map<String, Object> queryNavigator(Object model, Map<String, Object> parameter, int _currentPage,
			int _pageSize) throws Exception, InstantiationException {
		Map<String, Object> map = ModelToSelect.convertSelectSql(model, parameter);
		StringBuffer sql = new StringBuffer(map.get("sql").toString());
		@SuppressWarnings("unchecked")
		List<Object> agrs = (ArrayList<Object>) map.get("para");
		return queryNavigator(sql.toString(), agrs.toArray(), _currentPage, _pageSize, model);

	}

	@Override
	public Map<String, Object> queryNavigator(Object model, Map<String, Object> parameter, int _currentPage)
			throws Exception, InstantiationException {
		return queryNavigator(model, parameter, _currentPage, 10);
	}

	@Override
	public Map<String, Object> queryNavigator(Object model, int _currentPage, int _pageSize) throws Exception {
		return queryNavigator(model, null, _currentPage, _pageSize);
	}

	@Override
	public Map<String, Object> queryNavigator(Object model, int _currentPage) throws Exception {
		return queryNavigator(model, _currentPage, 10);
	}

	@Override
	public Map<String, Object> queryNavigator(String sql, int _currentPage, int _pageSize, Object model)
			throws Exception {
		return queryNavigator(sql, new Object[] {}, _currentPage, _pageSize, model);
	}

	@Override
	public Map<String, Object> queryNavigator(String sql, int _currentPage, Object model) throws Exception {
		return queryNavigator(sql, new Object[] {}, _currentPage, 10, model);
	}

	@Override
	public Map<String, Object> queryNavigator(String sql, Object[] args, int _currentPage, int _pageSize, Object model)
			throws Exception {
		StringBuffer sqlStr = new StringBuffer();
		Long count = getCount(sql.toString(), args);
		IPage page = getPage(Integer.valueOf(count.toString()), _currentPage, _pageSize);

		sqlStr.append(" select table2.* from ( SELECT table1.* ,row_number() over(ORDER BY 1 )AS num FROM ( ");
		sqlStr.append(sql.toString());
		sqlStr.append(" ) table1 ) table2 ");
		sqlStr.append(" where  table2.num between ? and ? ");

		List<Object> listArgs = new ArrayList<Object>();
		for (Object obj : args) {
			listArgs.add(obj);
		}
		listArgs.add(page.getStartRow());
		listArgs.add(page.getEndRow());
		Map<String, Object> mapResult = new HashMap<String, Object>();
		List<Object> listResult = query(sqlStr.toString(), listArgs.toArray(), model.getClass());
		mapResult.put("data", listResult);
		mapResult.put("pageInfo", page);
		return mapResult;
	}
	
	
	protected String getFirstValueByString(String sql, Object[] args, String defVaule) {
		printSQL(sql,args);
	    String value = (String) jdbcTemplateOfMysql.query(sql, args, new ResultSetExtractor<String>() {
		      @Override
		      public String extractData(ResultSet rs) throws SQLException, DataAccessException {
			      if (rs.next())
			    	  return rs.getString(1);
			      else 
			    	  return defVaule;
		      }
		});
	    return value;
	}
	
	public String getSqlByPageSize(String sql, int _currentPage, int _pageSize) {
		IPage page = getPage(0, _currentPage, _pageSize);
		
		StringBuffer sqlStr = new StringBuffer();
		sqlStr.append(" select table2.* from (SELECT table1.* ,row_number() over(ORDER BY 1 )AS num FROM ( ");
		sqlStr.append(sql.toString());
		sqlStr.append(" ) table1 ) table2 ");
		sqlStr.append(" where  table2.num between " + page.getStartRow()+ " and " + page.getEndRow());
		return sqlStr.toString();
	}
	
	

	@Override
	public Map<String, Object> queryNavigator(String sql, Object[] args, int _currentPage, Object model)
			throws Exception {
		return queryNavigator(sql, args, _currentPage, 10, model);
	}

	/**
	 * 更新前检测版本号
	 * 
	 * @param model
	 * @param parameter
	 * @throws Exception
	 */
	protected int checkVersion(Object model, Map<String, Object> parameter) throws Exception {
		Table table = model.getClass().getAnnotation(Table.class);
		Field[] fields = model.getClass().getDeclaredFields();
		ModelException modelException ;
		for (Field field : fields) {
			field.setAccessible(true);
			Column column = field.getAnnotation(Column.class);
			long id = 0;
			if (column != null && column.isId().equals("id")) {
				id = (long) field.get(model);
			}
			if (field.getName().equals("version")) {
				if (parameter != null) {
					id = Long.valueOf(parameter.get("id").toString());
					if (!parameter.containsKey("version")) {
						modelException =new ModelException("版本号不能为空!");
						modelException.setCode(-4);
						throw modelException ;
					}
					String sql = "select  VERSION from " + table.name() + " where id = " + id;
					int vsersion = this.queryForInt(sql);
					int v = (Integer.valueOf(parameter.get("version").toString())) - 1;
					if (vsersion == -1) {
						return vsersion;
					}
					if ((vsersion != v && vsersion != 99) || (vsersion != v && (vsersion == 99 && v != 0))) {
						modelException =new ModelException("版本号不一致!");
						modelException.setCode(-4);
						throw modelException ;
					}
					return vsersion;
				} else {
					int oldVersion = (int) field.get(model);
					String sql = "select  VERSION from " + table.name() + " where id = " + id;
					int vsersion = this.queryForInt(sql);
					if (vsersion != (oldVersion - 1)) {
						modelException =new ModelException("版本号不一致!");
						modelException.setCode(-4);
						throw modelException ;
					}
					return vsersion;
				}
			}
		}
		return 0;
	}

	private void showSql(String sql, Object[] args) {
//		for (Object obj : args) {
//			if (obj.getClass().equals(Long.class) || obj.getClass().equals(long.class)) {
//				sql = sql.replaceFirst("?", ((Long) obj).toString());
//			} else if (obj.getClass().equals(Integer.class) || obj.getClass().equals(int.class)) {
//				sql = sql.replaceFirst("?", ((Long) obj).toString());
//			} else if (obj.getClass().equals(String.class)) {
//				sql = sql.replaceFirst("?", "'" + obj.toString() + "'");
//			} else if (obj.getClass().equals(Date.class)) {
//				java.text.DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				sql = sql.replaceFirst("?", "to_date('" + format.format((Date) obj) + "','yyyy-mm-dd hh24:mi:ss')");
//			} else if (obj.getClass().equals(BigDecimal.class)) {
//				sql = sql.replaceFirst("?", ((BigDecimal) obj).toString());
//			} else if (obj.getClass().equals(Float.class) || obj.getClass().equals(float.class)) {
//				sql = sql.replaceFirst("?", ((Float) obj).toString());
//			} else if (obj.getClass().equals(Double.class) || obj.getClass().equals(double.class)) {
//				sql = sql.replaceFirst("?", ((Double) obj).toString());
//			} else if (obj.getClass().equals(Boolean.class) || obj.getClass().equals(boolean.class)) {
//				if ((Boolean) obj) {
//					sql = sql.replaceFirst("?", "1");
//				} else if (!(Boolean) obj) {
//					sql = sql.replaceFirst("?", "2");
//				}
//			}
//
//		}
//		System.out.println(sql);
//		for(Object obj : args){
//			System.out.print(","+String.valueOf(obj));
//		}
//		System.out.println("");
	}

	@Override
	public Long querySequence(Object model) throws Exception {
		return worker.nextId();
	}

	public Map<String, Object> getMapData(Object list) {
		Map<String, Object> mapObj = new HashMap<String, Object>();
		mapObj.put("data", list);
		return mapObj;
	}

	@Override
	public Map<String, Object> queryDataListByMap(String sql) throws Exception {		
		@SuppressWarnings({ "unchecked" })
		List<Map<String, Object>> list = (List<Map<String, Object>>) jdbcTemplateOfMysql.query(sql,
				new ResultSetExtractor<Object>() {
					@Override
					public List<Map<String, Object>> extractData(ResultSet rs) throws SQLException {

						List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
						ResultSetMetaData resultSetMetaData = rs.getMetaData();
						if (resultSetMetaData != null) {
							int colNum = 0;
							colNum = resultSetMetaData.getColumnCount();
							while (rs.next()) {
								Map<String, Object> resultMap = new HashMap<String, Object>();
								for (int i = 1; i <= colNum; i++) {
									String orgname = resultSetMetaData.getColumnName(i);
									String name = orgname.replaceAll("_", "");
									resultMap.put(name.toLowerCase(), rs.getString(orgname));
								}
								list.add(resultMap);
							}
						}
						return list;
					}
				});
		return getMapData(list);
	}
	
	@Override
	public Map<String, Object> queryAutoBySql(String sql, Map<String,Object> parameter, Class<?> classz) throws Exception {
		List<Object> args = new ArrayList<Object>();
		return queryAutoBySql(sql, args.toArray(), parameter, classz);
	}
	
	@Override
	public Map<String, Object> queryAutoBySql(String sql, Object[] args, Map<String,Object> parameter, Class<?> classz) throws Exception {
		if ((parameter != null) && parameter.containsKey("currentPage")) {
			if (parameter.containsKey("pageSize")) {
				return queryNavigator(sql, args, Integer.valueOf(parameter.get("currentPage").toString()),
						Integer.valueOf(parameter.get("pageSize").toString()),
						classz.newInstance());
			} else {
			    return queryNavigator(sql, args, 
						Integer.valueOf(parameter.get("currentPage").toString()),
						classz.newInstance());
			}
		} else {
			List<Object> list = query(sql.toString(), args, classz);
			return getMapData(list);
		}
	}
	
	@Override
	public Map<String, Object> queryAutoMapBySql(String sql, Map<String,Object> parameter)throws Exception {
		List<Object> args = new ArrayList<Object>();
		if ((parameter != null) && parameter.containsKey("currentPage")) {
			int _currentPage = Integer.valueOf(parameter.get("currentPage").toString());
			int _pageSize = 10;
			if (parameter.containsKey("pageSize")) {
				_pageSize = Integer.valueOf(parameter.get("pageSize").toString());
			}

			Long count = getCount(sql, args.toArray());
			IPage page = getPage(Integer.valueOf(count.toString()), _currentPage, _pageSize);
			
			StringBuffer sqlStr = new StringBuffer();
			sqlStr.append(" select table2.* from ( SELECT table1.* ,row_number() over(ORDER BY 1 )AS num FROM ( ");
			sqlStr.append(sql);
			sqlStr.append(" ) table1 ) table2 ");
			sqlStr.append(String.format(" where  table2.num between %d and %d", page.getStartRow(), page.getEndRow()));

			Map<String, Object> mapResult = queryDataListByMap(sqlStr.toString());			
			mapResult.put("pageInfo", page);
			return mapResult;	
		} else {
			return queryDataListByMap(sql);
		}
	}
	
	@Override
	public Object queryModelOfId(Long id, Object model) throws Exception {
		Map<String, Object> qryObj = new HashMap<String, Object>();
		qryObj.put("id", id);
		List<Object> list = this.query(model, qryObj);
		
		if (list != null){
			if (list.size() == 1)
				return list.get(0);
			else if (list.size() > 1)
				throw new ModelException("size more than 1!");
		}

		return null;
	}
}
