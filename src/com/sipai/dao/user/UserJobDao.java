package com.sipai.dao.user;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.user.UserJob;

@Repository
public class UserJobDao extends CommDaoImpl<UserJob>{
	public UserJobDao() {
		super();
		this.setMappernamespace("user.UserJobMapper");
	}
	
}