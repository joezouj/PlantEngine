package com.sipai.dao.msg;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.msg.Msguser;

@Repository
public class MsguserDao extends CommDaoImpl<Msguser>{
	public MsguserDao() {
		super();
		this.setMappernamespace("msg.MsguserMapper");
	}
}


