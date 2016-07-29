package com.sipai.dao.work;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.work.Workstation;

@Repository
public class WorkstationDao extends CommDaoImpl<Workstation>{
	public WorkstationDao() {
		super();
		this.setMappernamespace("work.WorkstationMapper");
	}
}