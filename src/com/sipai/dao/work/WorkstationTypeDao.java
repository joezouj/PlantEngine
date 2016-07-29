package com.sipai.dao.work;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.work.WorkstationType;

@Repository
public class WorkstationTypeDao extends CommDaoImpl<WorkstationType>{
	public WorkstationTypeDao() {
		super();
		this.setMappernamespace("work.WorkstationTypeMapper");
	}

	public List<WorkstationType> getListBySerial(String serial) {
		List<WorkstationType> list = this.getSqlSession().selectList("work.WorkstationTypeMapper.getListBySerial", serial);
		return list;
	}
}