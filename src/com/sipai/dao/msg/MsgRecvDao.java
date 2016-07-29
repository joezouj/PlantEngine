package com.sipai.dao.msg;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.msg.MsgRecv;
@Repository
public class MsgRecvDao extends CommDaoImpl<MsgRecv>{
	public MsgRecvDao() {
		super();
		this.setMappernamespace("msg.MsgRecvMapper");
	}
	public int updateBySetAndWhere(MsgRecv msgRecv){
		return getSqlSession().update("msg.MsgRecvMapper.updateBySetAndWhere", msgRecv);
	}
}
