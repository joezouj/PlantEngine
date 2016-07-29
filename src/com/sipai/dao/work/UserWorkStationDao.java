package com.sipai.dao.work;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.work.Group;
import com.sipai.entity.work.UserWorkStation;

@Repository
public class UserWorkStationDao extends CommDaoImpl<UserWorkStation>{
	public UserWorkStationDao() {
		super();
		this.setMappernamespace("work.UserWorkStationMapper");
	}
}