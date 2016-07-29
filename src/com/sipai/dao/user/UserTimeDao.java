package com.sipai.dao.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.user.User;
import com.sipai.entity.user.UserTime;

@Repository
public class UserTimeDao extends CommDaoImpl<UserTime>{
	public UserTimeDao() {
		super();
		this.setMappernamespace("user.UserTimeMapper");
	}
	
	public Double getTotaltimeByUserId(String userId){
		List<UserTime> list = this.getSqlSession().selectList("user.UserTimeMapper.getTotaltimeByUserId", userId);
		return list.get(0).getLogintime();
	}
}