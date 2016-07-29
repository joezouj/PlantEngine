package com.sipai.dao.msg;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.msg.MsgType;

@Repository
public class MsgTypeDao extends CommDaoImpl<MsgType>{
	public MsgTypeDao() {
		super();
		this.setMappernamespace("msg.MsgTypeMapper");
	}
	public List<MsgType> getMsgType(MsgType msgtype) {
	// TODO Auto-generated method stub
		List<MsgType> list = getSqlSession().selectList("msg.MsgTypeMapper.getMsgType", msgtype);
		return list;
	}
}
