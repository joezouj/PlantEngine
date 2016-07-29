package com.sipai.dao.base;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.entity.base.CommonFile;
@Repository
public class CommonFileDao extends CommDaoImpl<CommonFile> {

	public CommonFileDao() {
		super();
	}
	
	public List<CommonFile> selectByMasterId(String masterid){
		return this.getSqlSession().selectList(this.getMappernamespace()+".selectByMasterId",masterid);
	}
	
	public int deleteByMasterId(String masterid){
		return this.getSqlSession().delete(this.getMappernamespace()+".deleteByMasterId",masterid);
	}
}
