package com.sipai.dao.msg;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.msg.EmppSend;

@Repository
public class EmppSendDao extends CommDaoImpl<EmppSend>{
	public EmppSendDao() {
		super();
		this.setMappernamespace("msg.EmppSendMapper");
	}

}
