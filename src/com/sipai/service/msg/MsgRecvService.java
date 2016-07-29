package com.sipai.service.msg;

import java.util.List;

import com.sipai.entity.msg.MsgRecv;

public interface MsgRecvService {
	public MsgRecv getMsgRecvById(String msgrecv);
	
	public int saveMsgRecv(MsgRecv msgrecv);
	
	public int updateMsgRecvById(MsgRecv msgrecv);
	
	public int deleteMsgRecvById(String msgrecvId);
	
	public int deleteMsgRecvByWhere(String whereStr);

	public List<MsgRecv> selectList();

	public List<MsgRecv> selectListByWhere(String wherestr);

	public void saveRecv(String recvid, String masterid);

	public int updateMsgRecvBySetAndWhere(String setandwhere);
		
}
