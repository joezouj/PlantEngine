package com.sipai.dao.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.user.UserPower;

@Repository
public class UserPowerDao extends CommDaoImpl<UserPower>{
	
	public List<UserPower> selectListByUserId(String id){
		List<UserPower> list = this.getSqlSession().selectList("user.UserPowerMapper.selectListByUserId", id);
		return list;
	}
	
	public List<UserPower> selectMenuByUserId(String id){
		List<UserPower> list = this.getSqlSession().selectList("user.UserPowerMapper.selectMenuByUserId", id);
		return list;
	}
	
	public List<UserPower> selectFuncByUserId(String id){
		List<UserPower> list = this.getSqlSession().selectList("user.UserPowerMapper.selectFuncByUserId", id);
		return list;
	}
	
}