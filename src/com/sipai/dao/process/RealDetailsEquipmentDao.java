package com.sipai.dao.process;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.process.RealDetailsEquipment;


@Repository
public class RealDetailsEquipmentDao extends CommDaoImpl<RealDetailsEquipment>{
	public RealDetailsEquipmentDao() {
		super();
		this.setMappernamespace("process.RealDetailsEquipmentMapper");
	}

}