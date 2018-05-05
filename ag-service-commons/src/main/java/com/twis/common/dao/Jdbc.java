package com.twis.common.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Component;

@Component
public abstract class Jdbc extends JdbcDaoSupport {

	private static Logger log = LoggerFactory.getLogger(Jdbc.class);

	@Resource
	private DataSource dataSource;

	@PostConstruct
	protected void initDataSource() {
		super.setDataSource(dataSource);
	}

	@PreDestroy
	public void destroy() {
		if (log.isDebugEnabled()) {
			//log.debug("destroy");
		}
	}

	@PostConstruct
	public void init() {
		if (log.isDebugEnabled()) {
			//log.debug("init");
		}
	}

	/**
	 * 事务回滚.
	 */
	public boolean rollback() {
		try {
			Connection con = super.getConnection();
			if (con == null) {
				return false;
			}
			con.rollback();
			return true;
		} catch (CannotGetJdbcConnectionException e) {
			log.error(e.getMessage(), e);
			return false;
		} catch (SQLException e) {
			log.error(e.getMessage(), e);
			return false;
		}
	}

	protected String printSQL(String sql, Object[] args) {
		StatementParameter param = new StatementParameter();
		try {
			for(Object o: args) {
				param.setObject(o);
			}
		} catch(Exception e) {
			
		}
		param.getParameters();
		String sql1 = this.getSQL(sql, param);
		if (log.isDebugEnabled()) {
			log.debug(sql1);
		}
		return "";
	}
	
	protected String printDebugSQL(String sql, Object[] args) {
		StatementParameter param = new StatementParameter();
		try {
		for(Object o: args) {
		param.setObject(o);
		}
		} catch(Exception e) {

		}
		param.getParameters();
		String sql1 = this.getSQL(sql, param);
		if (log.isDebugEnabled()) {
			log.debug(sql1);
		}
		return sql1;
	}

	protected void printStackTrace(String sql, StatementParameter param, int updatedCount) {
		String str1 = this.getSQL(sql, param);
		if (log.isDebugEnabled()) {
			log.debug("sql:" + str1);
			log.debug("updatedCount:" + updatedCount);
		}
		Exception e = new Exception();
		if (log.isDebugEnabled()) {
			log.debug(e.getMessage(), e);
		}
		
	}

	protected String getSQL(String sql, StatementParameter param) {
		return SqlUtil.getSQL(sql, param);
	}

	protected int[] batchUpdate(String sql, BatchPreparedStatementSetter setter) {
		return this.getJdbcTemplate().batchUpdate(sql, setter);

	}

	protected <T> T query(String sql, Class<T> elementType) {

		try {
			return this.getJdbcTemplate().queryForObject(sql, new BeanPropertyRowMapper<T>(elementType));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	protected <T> T query(String sql, Class<T> elementType, StatementParameter param) {
		try {
			return this.getJdbcTemplate().queryForObject(sql, param.getArgs(), new BeanPropertyRowMapper<T>(elementType));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	private void log(List<?> list, String sql, StatementParameter param) {
		int size = ListUtil.size(list);
		String sql1;
		if (param == null) {
			sql1 = sql;
		} else {
			sql1 = this.getSQL(sql, param);
		}
		if (log.isDebugEnabled()) {
			log.debug("result size:" + size + " sql:" + sql1);
		}
		
	}

	protected List<Map<String, Object>> queryForMaps(String sql) {
		try {
			List<Map<String, Object>> list = this.getJdbcTemplate().queryForList(sql);
			return list;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	protected List<Map<String, Object>> queryForMaps(String sql, StatementParameter param) {
		try {
			List<Map<String, Object>> list = this.getJdbcTemplate().queryForList(sql,param.getArgs());
			return list;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	protected <T> List<T> queryForList(String sql, Class<T> elementType) {
		try {
			List<T> list = this.getJdbcTemplate().query(sql, new BeanPropertyRowMapper<T>(elementType));
			if (log.isDebugEnabled()) {
				this.log(list, sql, null);
			}
			return list;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}


	protected List<Long> queryForLongs(String sql, StatementParameter param) {
		List<Long> list = (List<Long>) super.getJdbcTemplate().query(sql, param.getArgs(), new RowMapper<Long>() {
			public Long mapRow(ResultSet rs, int index) {
				try {
					return rs.getLong(1);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		});
		if (log.isDebugEnabled()) {
			this.log(list, sql, param);
		}
		return list;
	}

	public List<Integer> queryForInts(String sql, StatementParameter param) {
		List<Integer> list = (List<Integer>) super.getJdbcTemplate().query(sql, param.getArgs(), new RowMapper<Integer>() {
			public Integer mapRow(ResultSet rs, int index) {
				try {
					return rs.getInt(1);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		});
		if (log.isDebugEnabled()) {
			this.log(list, sql, param);
		}
		return list;
	}

	protected List<String> queryForStrings(String sql, StatementParameter param) {
		List<String> list = (List<String>) super.getJdbcTemplate().query(sql, param.getArgs(), new RowMapper<String>() {
			public String mapRow(ResultSet rs, int index) {
				try {
					return rs.getString(1);
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			}
		});
		if (log.isDebugEnabled()) {
			this.log(list, sql, param);
		}
		return list;
	}

	protected <T> List<T> queryForList(String sql, Class<T> elementType, StatementParameter param) {
		try {
			List<T> list = this.getJdbcTemplate().query(sql, param.getArgs(), new BeanPropertyRowMapper<T>(elementType));
			if (log.isDebugEnabled()) {
				this.log(list, sql, param);
			}
			return list;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	protected long queryForLong(String sql) {
		try {
			long result = this.getJdbcTemplate().queryForLong(sql);
			if (log.isDebugEnabled()) {
				log.debug("result:" + result + " sql:" + sql);
			}
			return result;
		} catch (EmptyResultDataAccessException e) {
			return -1;
		}
	}

	protected long queryForLong(String sql, StatementParameter param) {
		Object[] args = param.getArgs();
		int[] argTypes = param.getArgTypes();
		try {
			long result = this.getJdbcTemplate().queryForLong(sql, args, argTypes);
			if (log.isDebugEnabled()) {
				log.debug("result:" + result + " sql:" + this.getSQL(sql, param));
			}
			return result;
		} catch (EmptyResultDataAccessException e) {
			return -1;
		}
	}

	public int queryForInt(String sql) {
		try {
			int result = this.getJdbcTemplate().queryForInt(sql);
			if (log.isDebugEnabled()) {
				log.debug("result:" + result + " sql:" + sql);
			}
			return result;
		} catch (EmptyResultDataAccessException e) {
			return -1;
		}
	}

	public int queryForInt(String sql, StatementParameter param) {
		Object[] args = param.getArgs();
		int[] argTypes = param.getArgTypes();
		try {
			int result = this.getJdbcTemplate().queryForInt(sql, args, argTypes);
			if (log.isDebugEnabled()) {
				log.debug("result:" + result + " sql:" + this.getSQL(sql, param));
			}
			return result;
		} catch (EmptyResultDataAccessException e) {
			return -1;
		}
	}

	protected java.util.Date queryForDate(String sql) {
		try {
			java.util.Date result = this.getJdbcTemplate().queryForObject(sql, java.util.Date.class);
			if (log.isDebugEnabled()) {
				log.debug("result:" + result + " sql:" + sql);
			}
			return result;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	protected java.util.Date queryForDate(String sql, StatementParameter param) {
		Object[] args = param.getArgs();
		int[] argTypes = param.getArgTypes();
		try {
			java.util.Date result = this.getJdbcTemplate().queryForObject(sql, args, argTypes, java.util.Date.class);
			if (log.isDebugEnabled()) {
				log.debug("result:" + result + " sql:" + this.getSQL(sql, param));
			}
			return result;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	protected String queryForString(String sql) {
		try {
			String result = this.getJdbcTemplate().queryForObject(sql, String.class);
			if (log.isDebugEnabled()) {
				log.debug("result:" + result + " sql:" + sql);
			}
			return result;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	protected String queryForString(String sql, StatementParameter param) {
		Object[] args = param.getArgs();
		int[] argTypes = param.getArgTypes();
		try {
			String result = this.getJdbcTemplate().queryForObject(sql, args, argTypes, String.class);
			if (log.isDebugEnabled()) {
				log.debug("result:" + result + " sql:" + this.getSQL(sql, param));
			}
			return result;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	public boolean updateForBoolean(String sql, StatementParameter param) {
		int updatedCount = this.updateForRecord(sql, param);
		return (updatedCount > 0);
	}

	protected int updateForRecord(String sql, StatementParameter param) {
		int updatedCount = this.getJdbcTemplate().update(sql, param.getParameters());
		if (log.isDebugEnabled()) {
			String sql1 = this.getSQL(sql, param);
			log.debug("updatedCount:" + updatedCount + " sql:" + sql1);
		}
		return updatedCount;
	}

	protected Long executeAndReturnKey(SimpleStatementParameter param) {
		SimpleJdbcInsert insertActor = new SimpleJdbcInsert(this.getJdbcTemplate().getDataSource()).withTableName(param.getTable()).usingGeneratedKeyColumns(param.getKeyColumn());
		Number newId = insertActor.executeAndReturnKey(param.getParameters());
		return newId.longValue();
	}
	
	protected Boolean execute(SimpleStatementParameter param) {
		SimpleJdbcInsert insertActor = new SimpleJdbcInsert(this.getJdbcTemplate().getDataSource()).withTableName(param.getTable());
		Integer row = insertActor.execute(param.getParameters());
		if(row>0){
			return true;
		}
		return false;
	}

	protected int execute(String sql) {
		int updatedCount = this.getJdbcTemplate().update(sql);
		if (log.isDebugEnabled()) {
			log.debug("updatedCount:" + updatedCount + " sql:" + sql);
		}
		return updatedCount;
	}

	protected int batchUpdate(List<String> sqlList) {
		int updatedCount = 0;

		for (String sql : sqlList) {
			updatedCount += this.execute(sql);
		}

		return updatedCount;
	}

	public String beanName() {
		return this.getClass().getName();
	}
}
