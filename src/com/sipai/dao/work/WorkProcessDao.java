package com.sipai.dao.work;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.process.RealDetails;
import com.sipai.entity.work.WorkProcess;

@Repository
public class WorkProcessDao extends CommDaoImpl<WorkProcess>{
	public WorkProcessDao() {
		super();
		this.setMappernamespace("work.WorkProcessMapper");
	}
	public List<RealDetails> getprocessdetails(RealDetails processdetails){
		List<RealDetails> list = getSqlSession().selectList("work.WorkProcessMapper.getprocessdetails", processdetails);
		return list;
	}
}
