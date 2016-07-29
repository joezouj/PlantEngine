package com.sipai.dao.quality;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.quality.LeaveFactoryCheck;
@Repository
public class LeaveFactoryCheckDao extends CommDaoImpl<LeaveFactoryCheck>{
	public LeaveFactoryCheckDao() {
		super();
		this.setMappernamespace("quality.LeaveFactoryCheckMapper");
	}
}
