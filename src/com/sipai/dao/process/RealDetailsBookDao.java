package com.sipai.dao.process;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.process.RealDetailsBook;


@Repository
public class RealDetailsBookDao extends CommDaoImpl<RealDetailsBook>{
	public RealDetailsBookDao() {
		super();
		this.setMappernamespace("process.RealDetailsBookMapper");
	}

}