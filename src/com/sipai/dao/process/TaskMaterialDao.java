package com.sipai.dao.process;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.process.TaskMaterial;
@Repository
public class TaskMaterialDao extends CommDaoImpl<TaskMaterial> {
	
	public TaskMaterialDao() {
		super();
		this.setMappernamespace("process.TaskMaterialMapper");
	}
	
	public List<TaskMaterial> selectListByWhere1(TaskMaterial taskMaterial){
		return this.getSqlSession().selectList("process.TaskMaterialMapper.selectListByWhere1", taskMaterial);
	}
}
