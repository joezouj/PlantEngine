package com.sipai.dao.msg;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.msg.EmppAdmin;

@Repository
public class EmppAdminDao extends CommDaoImpl<EmppAdmin>{
	public EmppAdminDao() {
		super();
		this.setMappernamespace("msg.EmppAdminMapper");
	}

}
