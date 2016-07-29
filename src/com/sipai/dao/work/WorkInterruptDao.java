package com.sipai.dao.work;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.msg.Msg;
import com.sipai.entity.work.WorkInterrupt;
import com.sipai.entity.work.WorkOrder;
import com.sipai.entity.work.Workstation;

@Repository
public class WorkInterruptDao extends CommDaoImpl<WorkInterrupt>{
	public WorkInterruptDao() {
		super();
		this.setMappernamespace("work.WorkInterruptMapper");
	}
		
}
