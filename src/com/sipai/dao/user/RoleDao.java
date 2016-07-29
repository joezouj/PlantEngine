package com.sipai.dao.user;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.user.Role;

@Repository
public class RoleDao extends CommDaoImpl<Role>{
	public RoleDao() {
		super();
		this.setMappernamespace("user.RoleMapper");
	}
}