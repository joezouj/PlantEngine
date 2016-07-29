package com.sipai.dao.msg;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.msg.EmppSendUser;

@Repository
public class EmppSendUserDao extends CommDaoImpl<EmppSendUser>{
	public EmppSendUserDao() {
		super();
		this.setMappernamespace("msg.EmppSendUserMapper");
	}

}
