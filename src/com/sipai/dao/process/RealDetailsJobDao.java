package com.sipai.dao.process;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.process.RealDetailsJob;


@Repository
public class RealDetailsJobDao extends CommDaoImpl<RealDetailsJob>{
	public RealDetailsJobDao() {
		super();
		this.setMappernamespace("process.RealDetailsJobMapper");
	}

}