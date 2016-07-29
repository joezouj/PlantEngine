package com.sipai.dao.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.user.Job;

@Repository
public class JobDao extends CommDaoImpl<Job>{
	public JobDao() {
		super();
		this.setMappernamespace("user.JobMapper");
	}
	
	public List<Job> selectListByUnitid(String unitid){
		List<Job> list = this.getSqlSession().selectList("user.JobMapper.selectListByUnitid", unitid);
		return list;
	}
}