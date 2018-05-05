package com.twis.common.dao;

import java.util.List;
import java.util.Map;

public interface BaseDao {
	
	


	/**
	 * 通用插入
	 * @param model
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public  int insert(Object model) throws Exception;
	
	/**
	 * 通用插入 
	 * 参数以map对象为准
	 * @param model
	 * @return
	 * @throws IllegalArgumentException
	 * 
	 */
	public  int insert(Object model,Map<String,Object> parameter) throws Exception;
	
	/**
	 * 插入
	 * @param sql
	 * @return
	 */
	public  int insert(String sql);
	
	/**
	 * 插入
	 * @param sql
	 * @param agrs
	 * @return
	 */
	public  int insert(String sql,Object[] agrs);
	
	
	
	/**
	 * 通用通过model更新
	 * @param model
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public  int update(Object model) throws Exception;
	
	/**
	 * 通用通过model更新
	 * 参数以map对象为准
	 * @param model
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public  int update(Object model,Map<String,Object> parameter) throws Exception;
	
	/**
	 * 更新
	 * @param sql
	 * @return
	 */
	public  int update(String sql);
	
	/**
	 * 更新
	 * @param sql
	 * @param agrs
	 * @return
	 */
	public  int update(String sql,Object[] agrs);
	
	
	
	/**
	 * 通用通过model删除
	 * @param model
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public  int delete(Object model) throws Exception;
	
	/**
	 * 通用通过model删除
	 * 参数以map对象为准
	 * @param model
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public  int delete(Object model,Map<String,Object> parameter) throws Exception;
	
	/**
	 * 删除
	 * @param sql
	 * @return
	 */
	public  int delete(String sql);
	
	/**
	 * 删除
	 * @param sql
	 * @param agrs
	 * @return
	 */
	public  int delete(String sql,Object[] agrs);
	
	
	/**
	 * 通用通过莫德罗查询 不分页
	 * @param model
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public  List<Object> query(Object model) throws Exception;
	
	/**
	 * 通用通过莫德罗查询 不分页
	 * @param model
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	public  List<Object> query(Object model,Map<String,Object> parameter) throws Exception;
	
	/**
	 * 通用通过 model查询
	 * @param sql
	 * @param args
	 * @param classz
	 * @return
	 */
	public  List<Object> query(String sql, Object[] args, Class<?> classz);
	
	public Map<String, Object> queryByMapResultData(String sql, Object[] args, Class<?> classz);
	
	/**
	 * 通用通过model查询  分页
	 * @param model
	 * @param _currentPage
	 * @param _pageSize
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException 
	 */
	public  Map<String, Object> queryNavigator(Object model,Map<String,Object> parameter,int _currentPage, int _pageSize) throws Exception;
	
	/**
	 * 通用通过model查询  分页
	 * @param model
	 * @param _currentPage
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException 
	 */
	public  Map<String, Object> queryNavigator(Object model,Map<String,Object> map,int _currentPage) throws Exception;
	
	/**
	 * 通用通过model查询  分页
	 * @param model
	 * @param _currentPage
	 * @param _pageSize
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException 
	 */
	public  Map<String, Object> queryNavigator(Object model,int _currentPage, int _pageSize) throws Exception;
	
	/**
	 * 通用通过model查询  分页
	 * @param model
	 * @param _currentPage
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException 
	 */
	public  Map<String, Object> queryNavigator(Object model,int _currentPage) throws Exception;
	

	/**
	 * 分页查询
	 * @param sql
	 * @param _currentPage
	 * @param _pageSize
	 * @param model
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public  Map<String, Object> queryNavigator(String sql,int _currentPage,int _pageSize,Object model) throws Exception;
	
	/**
	 * 分页查询
	 * @param sql
	 * @param _currentPage
	 * @param model
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public  Map<String, Object> queryNavigator(String sql,int _currentPage,Object model) throws Exception;
	
	/**
	 * 分页查询
	 * @param sql
	 * @param args
	 * @param _currentPage
	 * @param _pageSize
	 * @param model
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public  Map<String, Object> queryNavigator(String sql, Object[] args,int _currentPage,int _pageSize,Object model) throws Exception;
	
	
	/**
	 * 无总数的分页查询
	 * @param sql
	 * @param args
	 * @param _currentPage
	 * @param _pageSize
	 * @param model
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	/**
	 * 分页查询
	 * @param sql
	 * @param args
	 * @param _currentPage
	 * @param model
	 * @return
	 * @throws IllegalArgumentException IllegalAccessException
	 * @throws InstantiationException
	 */
	public  Map<String, Object> queryNavigator(String sql, Object[] args,int _currentPage,Object model) throws Exception;
	/**
	 * 获取序列号
	 * @author xukai
	 */	
	public Long querySequence(Object model) throws Exception;
	
	/*
	 * 查询List数据到Map
	 */
	public Map<String, Object> queryDataListByMap(String sql) throws Exception;
	
	
	public Map<String, Object> queryAutoBySql(String sql, Map<String,Object> parameter, Class<?> classz) throws Exception;
	
	public Map<String, Object> queryAutoBySql(String sql, Object[] args, Map<String,Object> parameter, Class<?> classz) throws Exception;
	
	public Map<String, Object> queryAutoMapBySql(String sql, Map<String,Object> parameter)throws Exception;

	int updateEx(Object model, Map<String, Object> parameter) throws Exception;
	
	public Object queryModelOfId(Long id, Object model) throws Exception;
}
