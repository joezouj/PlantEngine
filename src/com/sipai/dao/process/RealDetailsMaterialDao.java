package com.sipai.dao.process;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.process.RealDetailsMaterial;


@Repository
public class RealDetailsMaterialDao extends CommDaoImpl<RealDetailsMaterial>{
	public RealDetailsMaterialDao() {
		super();
		this.setMappernamespace("process.RealDetailsMaterialMapper");
	}

}