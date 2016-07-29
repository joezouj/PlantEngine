package com.sipai.dao.user;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.user.Unit;

@Repository
public class UnitDao extends CommDaoImpl<Unit>{
	public UnitDao() {
		super();
		this.setMappernamespace("user.UnitMapper");
	}
	
	public Unit getUnitById(String id){
		return this.getSqlSession().selectOne("user.UnitMapper.getUnitById",id);
	}
}