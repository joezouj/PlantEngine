package com.sipai.dao.msg;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.msg.Smsuser;

@Repository
public class SmsuserDao extends CommDaoImpl<Smsuser>{
	public SmsuserDao() {
		super();
		this.setMappernamespace("msg.SmsuserMapper");
	}

}
