package com.sipai.service.msg;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.msg.MsgRecvDao;
import com.sipai.entity.msg.MsgRecv;
import com.sipai.tools.CommUtil;

@Service
public class MsgRecvServiceImpl implements MsgRecvService{
	@Resource
	private MsgRecvDao msgrecvDao;
	@Override
	public MsgRecv getMsgRecvById(String msgrecvId) {
		// TODO Auto-generated method stub
		return this.msgrecvDao.selectByPrimaryKey(msgrecvId);
	}

	@Override
	public int saveMsgRecv(MsgRecv msgrecv) {
		// TODO Auto-generated method stub
		return this.msgrecvDao.insert(msgrecv);
	}

	@Override
	public int updateMsgRecvById(MsgRecv msgrecv) {
		// TODO Auto-generated method stub
		return this.msgrecvDao.updateByPrimaryKeySelective(msgrecv);
	}

	@Override
	public int deleteMsgRecvById(String msgrecvId) {
		// TODO Auto-generated method stub
		return this.msgrecvDao.deleteByPrimaryKey(msgrecvId);
	}

	@Override
	public int deleteMsgRecvByWhere(String whereStr) {
		// TODO Auto-generated method stub
		MsgRecv msgrecv = new MsgRecv();
		msgrecv.setWhere(whereStr);
		System.out.println(msgrecv.getWhere());
		return this.msgrecvDao.deleteByWhere(msgrecv);
	}

	@Override
	public List<MsgRecv> selectList() {
		// TODO Auto-generated method stub
		MsgRecv msgrecv = new MsgRecv();
		return this.msgrecvDao.selectList(msgrecv);
	}

	@Override
	public List<MsgRecv> selectListByWhere(String wherestr) {
		// TODO Auto-generated method stub
		MsgRecv msgrecv = new MsgRecv();
		msgrecv.setWhere(wherestr);
		return this.msgrecvDao.selectListByWhere(msgrecv);
	}
	@Override
	public void saveRecv(String recvid, String masterid) {
		//* 更新接受者的函数，在新建或更新接受者的时候都要先删除所有的原来的接受者,不带消息提示
		MsgRecv msgrecv = new MsgRecv();
		msgrecv.setWhere(" where masterid='"+ masterid + "'");
		this.msgrecvDao.deleteByWhere(msgrecv);
		
		String[] recvids = recvid.split(",");

		for (int i = 0; i <= recvids.length - 1; i++) {
			if (recvids[i].length() > 0) {
				MsgRecv cr = new MsgRecv();
				cr.setId(CommUtil.getUUID());
				cr.setMasterid(masterid);
				cr.setStatus("U");
				cr.setUnitid(recvids[i]);
				cr.setDelflag("FALSE");
				cr.setInsdt(new Date());
				this.msgrecvDao.insert(cr);
				cr = null;
			}
		}
	}
	@Override
	public int updateMsgRecvBySetAndWhere(String setandwhere) {
		
		MsgRecv sMsgRecv =  new MsgRecv();
		sMsgRecv.setWhere(setandwhere);
		// TODO Auto-generated method stub
		return this.msgrecvDao.updateBySetAndWhere(sMsgRecv);
	}
}//end
