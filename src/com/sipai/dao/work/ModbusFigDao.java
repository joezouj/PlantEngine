package com.sipai.dao.work;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.work.ModbusFig;
import com.sipai.entity.work.Workstation;

@Repository
public class ModbusFigDao extends CommDaoImpl<ModbusFig>{
	public ModbusFigDao() {
		super();
		this.setMappernamespace("work.ModbusFigMapper");
	}
}