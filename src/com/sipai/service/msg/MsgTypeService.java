package com.sipai.service.msg;

import java.util.List;

import com.sipai.entity.msg.MsgType;

public interface MsgTypeService {
	public MsgType getMsgTypeById(String msgtype);
	
	public int saveMsgType(MsgType msgtype);
	
	public int updateMsgTypeById(MsgType msgtype);
	
	public int deleteMsgTypeById(String msgtypeId);
	
	public int deleteMsgTypeByWhere(String whereStr,String msgtypeIds);

	public List<MsgType> selectList();

	public List<MsgType> selectListByWhere(String wherestr);

	public boolean checkNotOccupied(String msgtypeId);
	
	public List<MsgType> getMsgType(String wherestr);
	public int saveMsgRole(String msgroleids, String masterid);
	public int saveMsguser(String msguserids, String masterid);
	public int saveSmsuser(String smsuserids, String masterid);
}
