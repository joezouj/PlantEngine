package com.sipai.dao.msg;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.sipai.dao.base.CommDaoImpl;
import com.sipai.entity.msg.Msg;

@Repository
public class MsgDao extends CommDaoImpl<Msg> {
	public MsgDao() {
		super();
		this.setMappernamespace("msg.MsgMapper");
	}

	public List<Msg> getMsgsendlist(Msg msg) {
		// TODO Auto-generated method stub
		List<Msg> list = getSqlSession().selectList("msg.MsgMapper.getMsgsendlist", msg);
		return list;
	}

	public List<Msg> getMsgsend(Msg msg) {
		List<Msg> list = getSqlSession().selectList("msg.MsgMapper.getMsgrecv",msg);
		return list;
	}

	public List<Msg> getMsgrecv(Msg msg) {
		// TODO Auto-generated method stub
		List<Msg> list = getSqlSession().selectList("msg.MsgMapper.getMsgrecv",msg);
		return list;
	}

	public List<Msg> getMsgrecvTop1(Msg msg) {
		// TODO Auto-generated method stub
		List<Msg> list = getSqlSession().selectList("msg.MsgMapper.getMsgrecvTop1", msg);
		return list;
	}

	public int updateBySetAndWhere(Msg msg) {
		return getSqlSession().update("msg.MsgMapper.updateBySetAndWhere", msg);
	}
}
