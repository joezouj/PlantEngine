package com.sipai.dao.work;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.process.RealDetailsEquipment;
import com.sipai.entity.work.WorkTaskEquipment;


@Repository
public class WorkTaskEquipmentDao extends CommDaoImpl<WorkTaskEquipment>{
	public WorkTaskEquipmentDao() {
		super();
		this.setMappernamespace("work.WorkTaskEquipmentMapper");
	}

}