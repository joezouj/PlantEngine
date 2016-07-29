package com.sipai.dao.base;

import java.util.List;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Repository;

@Repository
public class CommDaoImpl<T> implements CommDao<T> {
	@Resource
	private SqlSessionFactory sqlSessionFactory;
	@Resource
	private SqlSession sqlSession;
	private String mappernamespace; // 需要操作的mappernamespace名(也是数据库表名)
	private boolean isAuto = true;// 默认自动提交

	public CommDaoImpl() {
		super();
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public boolean isAuto() {
		return isAuto;
	}

	public void setAuto(boolean isAuto) {
		this.isAuto = isAuto;
	}

	public String getMappernamespace() {
		return mappernamespace;
	}

	public void setMappernamespace(String mappernamespace) {
		this.mappernamespace = mappernamespace;
	}

	public T selectByPrimaryKey(String t){
		T o = getSqlSession().selectOne(mappernamespace+"."+Thread.currentThread().getStackTrace()[1].getMethodName(), t);
		return o;
	}

	public int deleteByPrimaryKey(String t) {
		return getSqlSession().delete(mappernamespace+"."+Thread.currentThread().getStackTrace()[1].getMethodName(), t);
	}

	public int insert(T t){
		return getSqlSession().insert(mappernamespace+"."+Thread.currentThread().getStackTrace()[1].getMethodName(), t);
	}

//	public int insertSelective(T t){
//		return getSqlSession().insert(mappernamespace+"."+Thread.currentThread().getStackTrace()[1].getMethodName(), t);
//	}

	public int updateByPrimaryKeySelective(T t){
		return getSqlSession().update(mappernamespace+"."+Thread.currentThread().getStackTrace()[1].getMethodName(), t);
	}

//	public int updateByPrimaryKey(T t){
//		return getSqlSession().update(mappernamespace+"."+Thread.currentThread().getStackTrace()[1].getMethodName(), t);
//	}

	public T selectByWhere( T t) {
		List<T> list = getSqlSession().selectList(mappernamespace+"."+Thread.currentThread().getStackTrace()[1].getMethodName(), t);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public List<T> selectList(T t) {
		List<T> list = getSqlSession().selectList(mappernamespace+"."+Thread.currentThread().getStackTrace()[1].getMethodName(), t);
		return list;
	}
	
	public List<T> selectListByWhere( T t) {
		List<T> list = getSqlSession().selectList(mappernamespace+"."+Thread.currentThread().getStackTrace()[1].getMethodName(), t);
		return list;
	}

	public List<T> selectListByObj(T t) {
		List<T> list = getSqlSession().selectList(mappernamespace+"."+Thread.currentThread().getStackTrace()[1].getMethodName(), t);
		return list;
	}
	public List<T> selectListByt( T t) {
		List<T> list = getSqlSession().selectList(mappernamespace+"."+Thread.currentThread().getStackTrace()[1].getMethodName(), t);
		return list;
	}

	public List<T> selectListBySql( T t) {
		List<T> list = getSqlSession().selectList(mappernamespace+"."+Thread.currentThread().getStackTrace()[1].getMethodName(), t);
		return list;
	}

	public T selectValueBySql( T t) {
		List<T> list = getSqlSession().selectList(mappernamespace+"."+Thread.currentThread().getStackTrace()[1].getMethodName(), t);
		if (list != null && list.size() > 0) {
			return list.get(0);
		} else {
			return null;
		}
	}

	public int deleteByWhere( T t) {
		return this.getSqlSession().delete(mappernamespace+"."+Thread.currentThread().getStackTrace()[1].getMethodName(), t);
	}

	public int updateByWhere( T t) {
		return this.getSqlSession().update(mappernamespace+"."+Thread.currentThread().getStackTrace()[1].getMethodName(), t);
	}

	public int executeSql( T t) {
		return this.getSqlSession().update(mappernamespace+"."+Thread.currentThread().getStackTrace()[1].getMethodName(), t);
	}

	public void commit() {
		if (sqlSession != null)
			sqlSession.commit();
	}

	public SqlSession getSqlSession() {
		sqlSession = sqlSession == null ? sqlSessionFactory.openSession(isAuto) : sqlSession;
		return sqlSession;
	}

	public void close() {
		if (sqlSession != null)
			sqlSession.close();
	}

}
