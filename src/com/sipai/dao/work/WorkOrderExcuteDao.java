package com.sipai.dao.work;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.work.WorkOrderExcute;

@Repository
public class WorkOrderExcuteDao extends CommDaoImpl<WorkOrderExcute>{
	public WorkOrderExcuteDao() {
		super();
		this.setMappernamespace("work.WorkOrderExcuteMapper");
	}
}
