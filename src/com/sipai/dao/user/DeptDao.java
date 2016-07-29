package com.sipai.dao.user;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.user.Dept;

@Repository
public class DeptDao extends CommDaoImpl<Dept>{
	public DeptDao() {
		super();
		this.setMappernamespace("user.DeptMapper");
	}
}