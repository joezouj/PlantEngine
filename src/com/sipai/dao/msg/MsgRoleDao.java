package com.sipai.dao.msg;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.msg.MsgRole;

@Repository
public class MsgRoleDao extends CommDaoImpl<MsgRole>{
	public MsgRoleDao() {
		super();
		this.setMappernamespace("msg.MsgRoleMapper");
	}

}
