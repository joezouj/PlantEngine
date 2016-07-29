package com.sipai.dao.process;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.process.RealDetailsStep;


@Repository
public class RealDetailsStepDao extends CommDaoImpl<RealDetailsStep>{
	public RealDetailsStepDao() {
		super();
		this.setMappernamespace("process.RealDetailsStepMapper");
	}

}