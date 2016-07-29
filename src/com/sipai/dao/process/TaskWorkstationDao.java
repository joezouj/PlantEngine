package com.sipai.dao.process;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.process.RealDetailsWorkstation;
import com.sipai.entity.process.TaskWorkstation;
@Repository
public class TaskWorkstationDao extends CommDaoImpl<TaskWorkstation> {
	
	public TaskWorkstationDao() {
		super();
		this.setMappernamespace("process.TaskWorkstationMapper");
	}

	public List<TaskWorkstation> selectListByWhere1(TaskWorkstation taskWorkstation){
		return this.getSqlSession().selectList("process.TaskWorkstationMapper.selectListByWhere1", taskWorkstation);
	}
}
