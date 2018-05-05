/**

 * @Description:TODO

 * @author:ben

 * @time:2016年4月9日 上午9:54:36

 */

package com.twis.common.dao;

import java.util.List;
import java.util.Map;

public interface BaseExDao extends BaseDao {
	
	/*不包含分页*/
	public List<Object> queryEx(Map<String, Object> parameter) throws Exception;
	
	/*根据参数调用分页,没有嵌套*/
	public Map<String, Object> querySimple(Map<String, Object> parameter) throws Exception;
	
	/*根据参数调用分页,有嵌套*/
	public Map<String, Object> queryCorrelation(Map<String, Object> parameter) throws Exception;
	
	public  List<Object> query(Object parameter) throws Exception;
	
	public  int insert(Object parameter) throws Exception;
	
	public  Long insertOfSequence(Object parameter) throws Exception;
	
	public  int update(Object parameter) throws Exception;
	
	public  int delete(Object parameter) throws Exception;
	
	public Long querySequence() throws Exception;
	
	public Object queryModel(Object parameter) throws Exception;
	
	public Object queryModelById(Long id) throws Exception;
	
	public Object queryModelById(Map<String, Object> parameter) throws Exception;
}
