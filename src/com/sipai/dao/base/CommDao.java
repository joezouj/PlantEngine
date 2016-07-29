package com.sipai.dao.base;

import java.util.List;


public interface CommDao<T>{
	/**====基础的CURD操作====**/
	/**
	 * 根据id查找唯一对象
	 * @param mapper
	 * @param t
	 * @return
	 */
	public T selectByPrimaryKey(String t);
	/**
	 * 根据id删除唯一对象
	 * @param mapper
	 * @param t
	 * @return
	 */
	public int deleteByPrimaryKey(String t);
	/**
	 * 根据对象信息插入数据
	 * @param mapper
	 * @param t
	 * @return
	 */
	public int insert(T t);
	/**
	 * 根据对象信息插入数据，只插入不为null的字段,不会影响有默认值的字段
	 * @param mapper
	 * @param t
	 * @return
	 */
//	public int insertSelective(T t);
	/**
	 * 根据对象信息更新数据，只跟新不为null的字段
	 * @param mapper
	 * @param t
	 * @return
	 */
	public int updateByPrimaryKeySelective(T t);
	/**
	 * 根据对象id信息更新数据，只跟新不为null的字段
	 * @param mapper
	 * @param t
	 * @return
	 */
//	public int updateByPrimaryKey(T t);
	
	/**=====扩展的查询=====**/
	/**
	 * 根据where条件查找唯一对象
	 * @param mapper
	 * @param t
	 * @return
	 */
	public T selectByWhere(T t);
	
	/**
	 * 根据where条件查找对象列表
	 * @param mapper
	 * @param t
	 * @return
	 */
	public List<T> selectList(T t);
	/**
	 * 查找和t某些值相等的对象列表
	 * @param mapper
	 * @param t
	 * @return
	 */
	
	/**
	 * 根据where条件查找对象列表
	 * @param mapper
	 * @param t
	 * @return
	 */
	public List<T> selectListByWhere(T t);
	/**
	 * 查找和t某些值相等的对象列表
	 * @param mapper
	 * @param t
	 * @return
	 */
	public List<T> selectListByObj(T t);
	/**
	 * 根据sql语句查找对象列表
	 * @param mapper
	 * @param t
	 * @return
	 */
	public List<T> selectListBySql(T t);
	/**
	 * 根据sql语句查询唯一字段值
	 * @param mapper
	 * @param t
	 * @return
	 */
	public Object selectValueBySql(T t);
	/**
	 * 根据where条件删除
	 * @param mapper
	 * @param t
	 */
	public int deleteByWhere(T t);
	/**
	 * 根据where条件更新
	 * @param mapper
	 * @param t
	 */
	public int updateByWhere(T t);
	/**
	 * 执行sql语句 ：增，删，改
	 * @param mapper
	 * @param t
	 */
	public int executeSql(T t);
	public void commit();
	public void close();
	

}

