package com.sipai.service.msg;

import java.util.List;

import com.sipai.entity.msg.Msg;

public interface MsgService {
	public Msg getMsgById(String msg);
	
	public int saveMsg(Msg msg);
	
	public int updateMsgById(Msg msg);
	
	public int deleteMsgById(String msgId);
	
	public int deleteMsgByWhere(String whereStr);

	public List<Msg> selectList();

	public List<Msg> selectListByWhere(String wherestr);
	
	public List<Msg> getMsgrecv(String wherestr);

	public int updateMsgBySetAndWhere(String setandwhere);
}
