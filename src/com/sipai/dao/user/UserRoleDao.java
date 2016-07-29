package com.sipai.dao.user;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.user.UserRole;

@Repository
public class UserRoleDao extends CommDaoImpl<UserRole>{
	public UserRoleDao() {
		super();
		this.setMappernamespace("user.UserRoleMapper");
	}
	
	public List<UserRole> selectListByRole(String roleid){
		return this.getSqlSession().selectList("user.UserRoleMapper.selectListByRole", roleid);
	}
	
	public int deleteByRole(String roleid){
		return this.getSqlSession().delete("user.UserRoleMapper.deleteByRole", roleid);
	}
	
	public int deleteByUser(String userid){
		return this.getSqlSession().delete("user.UserRoleMapper.deleteByUser", userid);
	}
}