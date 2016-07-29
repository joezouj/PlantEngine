package com.sipai.service.msg;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sipai.dao.msg.MsgRoleDao;
import com.sipai.dao.msg.MsgTypeDao;
import com.sipai.dao.msg.MsguserDao;
import com.sipai.dao.msg.SmsuserDao;
import com.sipai.entity.msg.Msg;
import com.sipai.entity.msg.MsgRecv;
import com.sipai.entity.msg.MsgRole;
import com.sipai.entity.msg.MsgType;
import com.sipai.entity.msg.Msguser;
import com.sipai.entity.msg.Smsuser;
import com.sipai.tools.CommUtil;



@Service
public class MsgTypeServiceImpl implements MsgTypeService{
	@Resource
	private MsgTypeDao msgtypeDao;
	@Resource
	private MsgRoleDao msgroleDao;
	@Resource
	private MsguserDao msguserDao;
	@Resource
	private SmsuserDao smsuserDao;
	
	@Override
	public MsgType getMsgTypeById(String msgtypeId) {
		// TODO Auto-generated method stub
		return this.msgtypeDao.selectByPrimaryKey(msgtypeId);
	}

	@Override
	public int saveMsgType(MsgType msgtype) {
		// TODO Auto-generated method stub
		return this.msgtypeDao.insert(msgtype);
	}

	@Override
	public int updateMsgTypeById(MsgType msgtype) {
		// TODO Auto-generated method stub
		return this.msgtypeDao.updateByPrimaryKeySelective(msgtype);
	}

	@Override
	public int deleteMsgTypeById(String msgtypeId) {
		// TODO Auto-generated method stub
		int res=this.msgtypeDao.deleteByPrimaryKey(msgtypeId);
		if(res==1){
			//级联删除3个表数据
			MsgRole msgrole = new MsgRole();
			msgrole.setWhere(" where masterid='"+msgtypeId+"'");			
			this.msgroleDao.deleteByWhere(msgrole);
			
			Msguser msguser = new Msguser();
			msguser.setWhere(" where masterid='"+msgtypeId+"'");			
			this.msguserDao.deleteByWhere(msguser);
			
			Smsuser smsuser = new Smsuser();
			smsuser.setWhere(" where masterid='"+msgtypeId+"'");			
			this.smsuserDao.deleteByWhere(smsuser);
		}
		return res;		
	}

	@Override
	public int deleteMsgTypeByWhere(String whereStr,String msgtypeIds) {
		// TODO Auto-generated method stub
		MsgType msgtype = new MsgType();
		msgtype.setWhere(whereStr);
		System.out.println(msgtype.getWhere());
		int res=this.msgtypeDao.deleteByWhere(msgtype);
		if(res==1){
			//级联删除3个表数据
			MsgRole msgrole = new MsgRole();
			msgrole.setWhere(" where masterid in('"+msgtypeIds+"')");			
			this.msgroleDao.deleteByWhere(msgrole);
			
			Msguser msguser = new Msguser();
			msguser.setWhere(" where masterid in('"+msgtypeIds+"')");			
			this.msguserDao.deleteByWhere(msguser);
			
			Smsuser smsuser = new Smsuser();
			smsuser.setWhere(" where masterid in('"+msgtypeIds+"')");			
			this.smsuserDao.deleteByWhere(smsuser);
		}
		return res;
	}

	@Override
	public List<MsgType> selectList() {
		// TODO Auto-generated method stub
		MsgType msgtype = new MsgType();
		return this.msgtypeDao.selectList(msgtype);
	}

	@Override
	public List<MsgType> selectListByWhere(String wherestr) {
		// TODO Auto-generated method stub
		MsgType msgtype = new MsgType();
		msgtype.setWhere(wherestr);
		return this.msgtypeDao.selectListByWhere(msgtype);
	}
	@Override
	public boolean checkNotOccupied(String msgtypeId){
		MsgType msgtype = new MsgType();
		msgtype.setWhere(" where id='"+msgtypeId+"' order by insdt");
		List<MsgType> list=this.msgtypeDao.selectListByWhere(msgtype);
		if(list.size()!=0){
			return false;
		}else{
			return true;
		}
	}
	@Override
	public List<MsgType> getMsgType(String wherestr){
		MsgType msgtype = new MsgType();
		msgtype.setWhere(wherestr);
		return this.msgtypeDao.getMsgType(msgtype);
	}
	@Override
	public int saveMsgRole(String msgroleids, String masterid){		
		//* 更新接受者的函数，在新建或更新接受者的时候都要先删除所有的原来的接受者,不带消息提示
				MsgRole msgrole = new MsgRole();
				msgrole.setWhere(" where masterid='"+ masterid + "'");				
				this.msgroleDao.deleteByWhere(msgrole);
				
				String[] msgroleid = msgroleids.split(",");
				int res=0;
				for (int i = 0; i <= msgroleid.length - 1; i++) {
					if (msgroleid[i].length() > 0) {
						MsgRole cr = new MsgRole();
						cr.setId(CommUtil.getUUID());
						cr.setMasterid(masterid);						
						cr.setRoleid(msgroleid[i]);						
						cr.setInsdt(new Date());
						res=this.msgroleDao.insert(cr);
						cr = null;
					}
				}
				return res;
	}
	@Override
	public int saveMsguser(String msguserids, String masterid){
		Msguser msguser = new Msguser();
		msguser.setWhere(" where masterid='"+ masterid + "'");				
		this.msguserDao.deleteByWhere(msguser);
		
		String[] msguserid = msguserids.split(",");
		int res=0;
		for (int i = 0; i <= msguserid.length - 1; i++) {
			if (msguserid[i].length() > 0) {
				Msguser cr = new Msguser();
				cr.setId(CommUtil.getUUID());
				cr.setMasterid(masterid);						
				cr.setUserid(msguserid[i]);						
				cr.setInsdt(new Date());
				res=this.msguserDao.insert(cr);
				cr = null;
			}
		}
		return res;
	}
	@Override
	public int saveSmsuser(String smsuserids, String masterid){
		Smsuser smsuser = new Smsuser();
		smsuser.setWhere(" where masterid='"+ masterid + "'");				
		this.smsuserDao.deleteByWhere(smsuser);
		
		String[] smsuserid = smsuserids.split(",");
		int res=0;
		for (int i = 0; i <= smsuserid.length - 1; i++) {
			if (smsuserid[i].length() > 0) {
				Smsuser cr = new Smsuser();
				cr.setId(CommUtil.getUUID());
				cr.setMasterid(masterid);						
				cr.setUserid(smsuserid[i]);						
				cr.setInsdt(new Date());
				res=this.smsuserDao.insert(cr);
				cr = null;
			}
		}
		return res;
	}
}
