package com.sipai.dao.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.user.User;

@Repository
public class UserDao extends CommDaoImpl<User>{
	public UserDao() {
		super();
		this.setMappernamespace("user.UserMapper");
	}
	
	public List<User> getListByLoginName(String name){
		List<User> list = this.getSqlSession().selectList("user.UserMapper.getUserByName", name);
		return list;
	}

	public List<User> getListBySerial(String serial) {
		List<User> list = this.getSqlSession().selectList("user.UserMapper.getListBySerial", serial);
		return list;
	}

	public List<User> getListByCardid(String cardid) {
		List<User> list = this.getSqlSession().selectList("user.UserMapper.getListByCardid", cardid);
		return list;
	}
}