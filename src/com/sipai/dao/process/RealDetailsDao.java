package com.sipai.dao.process;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.process.RealDetails;


@Repository
public class RealDetailsDao extends CommDaoImpl<RealDetails>{
	public RealDetailsDao() {
		super();
		this.setMappernamespace("process.RealDetailsMapper");
	}

}