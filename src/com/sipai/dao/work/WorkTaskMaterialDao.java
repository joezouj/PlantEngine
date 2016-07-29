package com.sipai.dao.work;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.work.GroupMember;
import com.sipai.entity.work.WorkTaskMaterial;

@Repository
public class WorkTaskMaterialDao extends CommDaoImpl<WorkTaskMaterial>{
	public WorkTaskMaterialDao() {
		super();
		this.setMappernamespace("work.WorkTaskMaterialMapper");
	}
	public List<WorkTaskMaterial> batchinglistByWhere(WorkTaskMaterial workTaskMaterial){
		return this.getSqlSession().selectList("work.WorkTaskMaterialMapper.batchinglistByWhere", workTaskMaterial);
	}
}
