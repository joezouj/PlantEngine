package com.sipai.dao.work;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.work.WorkProcessOrder;

@Repository
public class WorkProcessOrderDao extends CommDaoImpl<WorkProcessOrder>{
	public WorkProcessOrderDao() {
		super();
		this.setMappernamespace("work.WorkProcessOrderMapper");
	}
}
