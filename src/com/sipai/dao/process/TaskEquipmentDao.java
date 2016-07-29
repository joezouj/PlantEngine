package com.sipai.dao.process;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.process.TaskEquipment;
@Repository
public class TaskEquipmentDao extends CommDaoImpl<TaskEquipment> {
	
	public TaskEquipmentDao() {
		super();
		this.setMappernamespace("process.TaskEquipmentMapper");
	}

	public List<TaskEquipment> selectListByWhere1(TaskEquipment taskEquipment){
		return this.getSqlSession().selectList("process.TaskEquipmentMapper.selectListByWhere1", taskEquipment);
	}
}
